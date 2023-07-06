package com.saltserv.practice.domain

import com.saltserv.practice.data.model.JsonResponse
import com.saltserv.practice.data.repository.YourRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class YourGetDataUseCase @Inject constructor(
    private val yourRepository: YourRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(
        yourQuery: String
    ): Flow<JsonResponse> = flow {
        val response = yourRepository.getYourData(yourQuery)
        emit(response)
    }.flowOn(ioDispatcher)
}