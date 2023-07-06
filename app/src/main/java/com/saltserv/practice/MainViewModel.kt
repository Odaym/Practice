package com.saltserv.practice

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saltserv.practice.domain.YourGetDataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface MyState {
    object Loading : MyState
    data class Success(val data: String) : MyState
    data class Error(val throwable: Throwable) : MyState
}

class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle = SavedStateHandle(),
    private val getDataUseCase: YourGetDataUseCase
) : ViewModel() {

    // Use this to populate savedStateHandle when needed, and then read it here
    // savedStateHandle[SEARCH_QUERY] = myQuery

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchQuery: StateFlow<MyState> =
        savedStateHandle.getStateFlow("searchQuery", "")
            .debounce(500L)
            .distinctUntilChanged()
            .flatMapLatest {
                if (it.isEmpty()) {
                    flowOf(MyState.Loading)
                } else {
                    getYourData(it)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = MyState.Loading
            )

    private fun getYourData(query: String): Flow<MyState> {
        return getDataUseCase(query)
            .asResult()
            .map {
                when (it) {
                    is Result.Success -> MyState.Success(it.data.name)
                    is Result.Error -> MyState.Error(it.throwable!!)
                    is Result.Loading -> MyState.Loading
                }
            }
    }
}