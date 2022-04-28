package com.manicpixie.habittracker.domain.use_case


import com.manicpixie.habittracker.domain.repository.HabitRepository
import javax.inject.Inject

class GetInfoUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke() : List<Any> {
        return repository.getHabitsInfo()
    }
}