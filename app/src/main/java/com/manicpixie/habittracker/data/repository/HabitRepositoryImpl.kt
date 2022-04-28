package com.manicpixie.habittracker.data.repository

import android.util.Log
import com.google.gson.Gson
import com.manicpixie.habittracker.data.local.HabitDao
import com.manicpixie.habittracker.data.local.entity.HabitEntity
import com.manicpixie.habittracker.data.remote.HabitApi
import com.manicpixie.habittracker.data.remote.dto.DeleteDto
import com.manicpixie.habittracker.data.remote.dto.IncreaseCountDto
import com.manicpixie.habittracker.domain.repository.HabitRepository
import com.manicpixie.habittracker.domain.util.HabitOrder
import com.manicpixie.habittracker.util.setMidnight
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    val api: HabitApi,
    val dao: HabitDao
) : HabitRepository {


    override suspend fun insert(habit: HabitEntity) {
        val habitDto = habit.toHabitDto()
        try {
            val gsonInsert = Gson().toJson(habitDto)
            val postedHabit = api.postHabits(gsonInsert)
            habit.habitUid = postedHabit.uid
        } catch (e: HttpException) {
        } catch (e: IOException) {
        }
        dao.insert(habit)
    }


    override suspend fun update(habit: HabitEntity) {
        dao.update(habit)
        val habitDto = habit.toHabitDto()
        try {
            val updateGson = Gson().toJson(habitDto)
            api.postHabits(updateGson)
        } catch (e: HttpException) {
        } catch (e: IOException) {
        }
    }


    override suspend fun deleteHabit(habit: HabitEntity) {
        dao.deleteHabit(habit)
        delay(300)
        try {
            val delete = DeleteDto(
                uid = "${habit.habitUid}"
            )
            val gsonDelete = Gson().toJson(delete)
            api.deleteHabit(gsonDelete)
        } catch (e: HttpException) {
            Log.e("error", "HttpException")
        } catch (e: IOException) {
            Log.e("error", "IOException")
        }
    }


    override fun getHabits(
        habitOrder: HabitOrder,
        listSize: Int,
        query: String
    ): Flow<Result<List<HabitEntity>>> =
        flow {
            var localHabits = dao.getAllHabits()
            localHabits = when (habitOrder) {
                is HabitOrder.ByDate -> {
                    localHabits.sortedBy { it.dateOfCreation }.reversed()
                }
                is HabitOrder.ByPriority -> {
                    localHabits.sortedBy { it.priority }.reversed()
                }
            }
            if (query.isNotBlank()) localHabits = localHabits.filter {
                it.title.lowercase().contains(query) || it.description.lowercase()
                    .contains(query) ||
                        it.title.contains(query) || it.description.contains(query)
            }
            emit(Result.success(localHabits.subList(0, listSize)))
        }


    override fun loadNextHabits(
        habitOrder: HabitOrder,
        page: Int,
        pageSize: Int,
        shouldUpdateRemote: Boolean
    ): Flow<Result<List<HabitEntity>>> = flow {
        val localHabits = dao.getAllHabits()

        //Блок выполняется, если необходимо обновить данные на сервере
        if (shouldUpdateRemote) {
            val localHabitsUids = localHabits.map { it.habitUid }
            try {
                val remoteHabits = api.getHabits()
                //Находим в локальной базе данных привычки, которые редактировали в оффлайн-режиме
                val uidsToUpdate = remoteHabits.filter {
                    localHabitsUids.contains(it.uid)
                }.filter { habitDto ->
                    habitDto.date < localHabits.filter { it.habitUid == habitDto.uid }[0].dateOfCreation
                }.map { it.uid }

                //Загружаем на сервер привычки, которые отредактировали или создали оффлайн
                localHabits.filter {
                    uidsToUpdate.contains(it.habitUid) || it.habitUid == null
                }.onEach { habit ->
                    habit.dateOfCreation = GregorianCalendar.getInstance().also {
                        it.timeZone = TimeZone.getTimeZone("GMT")
                    }.timeInMillis
                    api.postHabits(Gson().toJson(habit.toHabitDto()))
                }


                //Очищаем локальную базу данных
                dao.clear()

                //Загружаем в локальную базу данных привычки с сервера
                api.getHabits().map { it.toHabitEntity() }.onEach { habit ->
                    dao.insert(habit)
                }

            } catch (e: HttpException) {
                Log.e("error", "HttpException")
            } catch (e: IOException) {
                Log.e("error", "IOException")
            }
        }

        var updatedLocalHabits = dao.getAllHabits()

        //Блок сортировки
        updatedLocalHabits = when (habitOrder) {
            is HabitOrder.ByDate -> {
                updatedLocalHabits.sortedBy { it.dateOfCreation }.reversed()
            }
            is HabitOrder.ByPriority -> {
                updatedLocalHabits.sortedBy { it.priority }.reversed()
            }
        }

        //Блок пагинации
        val startingIndex = page * pageSize
        if (pageSize >= updatedLocalHabits.size && startingIndex == 0) emit(
            Result.success(
                updatedLocalHabits
            )
        )
        else if (startingIndex + pageSize >= updatedLocalHabits.size && startingIndex != 0)
            emit(Result.success(updatedLocalHabits.slice(startingIndex until updatedLocalHabits.size)))
        else if (startingIndex + pageSize < updatedLocalHabits.size) emit(
            Result.success(
                updatedLocalHabits.slice(
                    startingIndex until startingIndex + pageSize
                )
            )
        )
        else emit(Result.success(emptyList()))
    }


    override suspend fun getHabitsInfo(): List<Any> {
        val totalNumberOfHabits = dao.totalNumberOfHabits()
        val numberOfNegativeHabits = dao.getAllNegativeHabits().size
        val numberOfPositiveHabits = dao.getAllPositiveHabits().size
        val totalAveragePerformance = dao.getAllHabits().map { it.toHabit() }.map { it.averagePerformance }.sum()/totalNumberOfHabits
        return listOf(totalNumberOfHabits, numberOfNegativeHabits, numberOfPositiveHabits, totalAveragePerformance )
    }


    override suspend fun increaseCount(habit: HabitEntity) {
        dao.update(habit)
        try {
            val increaseCount = IncreaseCountDto(
                date = GregorianCalendar.getInstance().also {
                    it.timeZone = TimeZone.getTimeZone("GMT")
                }.setMidnight().timeInMillis,
                habitUid = "${habit.habitUid}"
            )
            val increaseCountGson = Gson().toJson(increaseCount)
            api.increaseHabitCount(increaseCountGson)
        } catch (e: HttpException) {
        } catch (e: IOException) {
        }
    }


    override fun searchHabits(
        habitOrder: HabitOrder,
        query: String
    ): Flow<Result<List<HabitEntity>>> = flow {
        var localHabits = dao.getAllHabits()
        localHabits = when (habitOrder) {
            is HabitOrder.ByDate -> {
                localHabits.sortedBy { it.dateOfCreation }.reversed()
            }
            is HabitOrder.ByPriority -> {
                localHabits.sortedBy { it.priority }.reversed()
            }
        }
        val filteredHabits = localHabits.filter {
            it.title.lowercase().contains(query) || it.description.lowercase()
                .contains(query) ||
                    it.title.contains(query) || it.description.contains(query)
        }
        emit(Result.success(filteredHabits))
    }

    override suspend fun getHabitByDate(date: Long): HabitEntity {
        return dao.getHabitByDate(date)
    }

}