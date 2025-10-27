package com.skegworks.mobilepos.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.category.CategoryRepository
import com.skegworks.mobilepos.sync.LoadAllDataFromFireStoreUseCase
import com.skegworks.mobilepos.sync.SyncData
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val syncDataWithFireStore: SyncData,
    private val categoryRepository: CategoryRepository,
    private val loadAllDataUseCase: LoadAllDataFromFireStoreUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val supervisor = SupervisorJob()
    private val scope = CoroutineScope(supervisor + Dispatchers.IO)

    fun loadAllData() {
        viewModelScope.launch(Dispatchers.IO) {

            loadAllDataUseCase.invoke {
                _uiState.value = SplashUiState.Success
            }
//            if (result.isSuccess) {
//                for (category in result.getOrNull().orEmpty()) {
//                    categoryRepository.insertCategory(category)
//                }
//
//            } else {
//                _uiState.value = SplashUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
//            }
        }
    }
}

sealed class SplashUiState {
    object Loading : SplashUiState()
    object Success : SplashUiState()
    data class Error(val message: String) : SplashUiState()
}