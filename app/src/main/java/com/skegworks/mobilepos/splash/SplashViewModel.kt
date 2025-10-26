package com.skegworks.mobilepos.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.category.CategoryRepository
import com.skegworks.mobilepos.data.Category
import com.skegworks.mobilepos.sync.SyncData
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val syncDataWithFireStore: SyncData,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val result = syncDataWithFireStore.downloadAll("categories", Category::class.java)
            if (result.isSuccess) {
                for (category in result.getOrNull().orEmpty()) {
                    categoryRepository.insertCategory(category)
                }
                _uiState.value = SplashUiState.Success
            } else {
                _uiState.value = SplashUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}

sealed class SplashUiState {
    object Loading : SplashUiState()
    object Success : SplashUiState()
    data class Error(val message: String) : SplashUiState()
}