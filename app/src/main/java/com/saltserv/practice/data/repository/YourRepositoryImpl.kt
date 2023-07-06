package com.saltserv.practice.data.repository

import com.saltserv.practice.data.api.YourApi
import com.saltserv.practice.data.model.JsonResponse
import javax.inject.Inject

class YourRepositoryImpl @Inject constructor(
    private val yourApi: YourApi
) : YourRepository {

    override suspend fun getYourData(
        yourQuery: String
    ): JsonResponse {
        return yourApi.getYourData(yourQuery)
    }
}