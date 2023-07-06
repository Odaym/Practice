package com.saltserv.practice.data.repository

import com.saltserv.practice.data.model.JsonResponse

interface YourRepository {

    suspend fun getYourData(
        yourQuery: String
    ): JsonResponse
}