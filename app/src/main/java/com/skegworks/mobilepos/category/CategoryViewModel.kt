package com.skegworks.mobilepos.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.data.Category
import com.skegworks.mobilepos.product.AddProductState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repository: CategoryRepository): ViewModel() {

    private val _state = MutableStateFlow(AddCategoryState())
    val state: StateFlow<AddCategoryState> = _state.asStateFlow()

    fun handleIntent(intent: AddCategoryIntent) {
        when (intent) {
            is AddCategoryIntent.UpdateCategory -> _state.update {
                it.copy(
                    textStateCategoryName = intent.category,
                    isSaved = false
                )
            }

            is AddCategoryIntent.UpdateDescription -> _state.update {
                it.copy(
                    textStateDescription = intent.description,
                    isSaved = false
                )
            }

            is AddCategoryIntent.UpdateCreatedAt -> _state.update {
                it.copy(
                    textStateCreatedAt = intent.createdAt,
                    isSaved = false
                )
            }
            is AddCategoryIntent.UpdateUpdatedAt -> _state.update {
                it.copy(
                    textStateUpdatedAt = intent.updatedAt,
                    isSaved = false
                )
            }
            is AddCategoryIntent.Save -> {
                _state.update {
                    it.copy(
                        isSaved = true
                    )
                }
                viewModelScope.launch(Dispatchers.IO) {
                    repository.insertCategory(Category(
                        name = state.value.textStateCategoryName,
                        description = state.value.textStateDescription,
                        createdAt = state.value.textStateCreatedAt,
                        updatedAt = state.value.textStateUpdatedAt,
                    ))
                }
            }
        }
    }
}