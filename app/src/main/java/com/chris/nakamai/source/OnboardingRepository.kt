package com.chris.nakamai.source

import kotlinx.coroutines.flow.Flow

interface OnBoardingRepository {
    suspend fun saveOnBoardingState(isCompleted: Boolean)
    fun readOnBoardingState(): Flow<Boolean>
}