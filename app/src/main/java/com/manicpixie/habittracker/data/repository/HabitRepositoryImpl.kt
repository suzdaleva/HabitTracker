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
            Log.e("info", "HttpException")
        } catch (e: IOException) {
            Log.e("info", "IOException")
        }
    }


    override fun getHabits(habitOrder: HabitOrder, listSize: Int): Flow<Result<List<HabitEntity>>> =
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
            emit(Result.success(localHabits.subList(0, listSize)))
        }


    override fun loadNextHabits(
        habitOrder: HabitOrder,
        page: Int,
        pageSize: Int,
        shouldUpdateRemote: Boolean
    ): Flow<Result<List<HabitEntity>>> = flow {
        var localHabits = dao.getAllHabits()
        if (shouldUpdateRemote) {
            val localHabitsUids = localHabits.mapNotNull { it.habitUid }
            try {
                //First delete all undeleted habits from server
                val remoteHabitsUids = api.getHabits().map { it.uid }
                remoteHabitsUids.filterNot { localHabitsUids.contains(it) }.onEach { uid ->
                    val gsonDelete = Gson().toJson(DeleteDto(uid = uid!!))
                    api.deleteHabit(gsonDelete)
                }
                //Then upload on server all new habits and update all existing habits
                if(api.getHabits().isNotEmpty()) {
                    localHabits.onEach { habit ->
                        val postedHabit = api.postHabits(Gson().toJson(habit.toHabitDto()))
                        habit.habitUid = postedHabit.uid
                        dao.update(habit)
                    }
                }
                else {
                    localHabits.onEach { habit ->
                        habit.habitUid = null
                        val postedHabit = api.postHabits(Gson().toJson(habit.toHabitDto()))
                        habit.habitUid = postedHabit.uid
                        dao.update(habit)
                    }
                }
            } catch (e: HttpException) {
            } catch (e: IOException) {
            }
        }
        localHabits = when (habitOrder) {
            is HabitOrder.ByDate -> {
                localHabits.sortedBy { it.dateOfCreation }.reversed()
            }
            is HabitOrder.ByPriority -> {
                localHabits.sortedBy { it.priority }.reversed()
            }
        }
        val startingIndex = page * pageSize
        if (pageSize >= localHabits.size && startingIndex == 0) emit(Result.success(localHabits))
        else if (startingIndex + pageSize >= localHabits.size && startingIndex != 0)
            emit(Result.success(localHabits.slice(startingIndex until localHabits.size)))
        else if (startingIndex + pageSize < localHabits.size) emit(
            Result.success(
                localHabits.slice(
                    startingIndex until startingIndex + pageSize
                )
            )
        )
        else emit(Result.success(emptyList()))
    }


    override suspend fun increaseCount(habit: HabitEntity) {
        dao.update(habit)
        try {
            val increaseCount = IncreaseCountDto(
                date = Calendar.getInstance().timeInMillis.toInt(),
                habitUid = "${habit.habitUid}"
            )
            Log.i("info", "${habit.habitUid}")
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