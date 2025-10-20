package com.skegworks.mobilepos.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.core.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


data class ListState(
    val list: List<SampleData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class ListIntent() {
    object FetchList : ListIntent()
}

class ListViewModel : ViewModel() {
    private val _state = MutableStateFlow(ListState())
    val state: MutableStateFlow<ListState> get() = _state

//    var list = mutableListOf<SampleData>(
//        SampleData("https://dummyjson.com/image/400x200/282828", "one")
//    )

    private val repository = SampleRepositoryImpl()

    fun handleIntent(intent: ListIntent) {
        when (intent) {
            is ListIntent.FetchList -> fetchContent()
        }
    }

    private fun fetchContent() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(isLoading = true, error = null)
            delay(1000)
            try {
                val data:List<SampleData> = repository.fetchData() as MutableList<SampleData>
                _state.value = _state.value.copy(list = data, isLoading = false)
            } catch (ex: Exception) {
                println("LVM crash ${ex.localizedMessage}")
            }
        }
    }
}

// News aggregator
//Click on news -> opens in webview
// title, description, url


// One Activity

