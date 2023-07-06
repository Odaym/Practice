package com.saltserv.practice.data.api

import com.saltserv.practice.data.model.JsonResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YourApi {

    @GET("your/endpoint")
    suspend fun getYourData(
        @Query("your_query") yourQuery: String
    ): JsonResponse
}
