package com.skegworks.mobilepos.cashcounter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.data.domain.CashCounter
import com.skegworks.mobilepos.data.domain.CashCounterStatus
import com.skegworks.mobilepos.data.mapper.toFirestoreDto
import com.skegworks.mobilepos.data.remote.firestore.FirestoreHelper
import com.skegworks.mobilepos.utils.UUIDGenerator
import com.skegworks.mobilepos.utils.preferences.UserPreferenceHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CashCounterViewModel @Inject constructor(
    private val firestoreHelper: FirestoreHelper,
    private val uuidGenerator: UUIDGenerator,
    private val userPreferenceHandler: UserPreferenceHandler,
    private val cashCounterRepository: CashCounterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CashCounterState())
    val state: StateFlow<CashCounterState> = _state.asStateFlow()

    fun handleIntent(intent: CashCounterIntent) {
        when (intent) {
            is CashCounterIntent.OpenCashCounter -> {
                syncCashCounter(CashCounterStatus.OPEN)
            }

            is CashCounterIntent.CloseCashCounter -> {
                syncCashCounter(CashCounterStatus.CLOSED)
            }

            is CashCounterIntent.UpdateCash -> {
                _state.value = _state.value.copy(
                    textStateCashInCounter = intent.cash
                )
            }
        }
    }

    private fun syncCashCounter(status: CashCounterStatus) {
        _state.update {
            it.copy(
                isLoading = true,
                error = null,
                isSynced = false,
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val cashCounter = CashCounter(
                id = uuidGenerator.generateUUID(),
                date = LocalDateTime.now().toString(),
                balance = _state.value.textStateCashInCounter,
                status = status,
                createdBy = userPreferenceHandler.getUserEmail() ?: "",
                createdAt = System.currentTimeMillis(),
                isSynced = false
            )
            cashCounterRepository.insertCashCounter(cashCounter)
            val result = firestoreHelper.addDocument(
                collection = "cash_counter",
                id = "1",
                data = cashCounter.toFirestoreDto()
            )
            if (result.isSuccess) {
                cashCounterRepository.updateCashCounter(cashCounter.apply {
                    isSynced = true
                })
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        isSynced = true,
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to update cash counter",
                        isSynced = false,
                    )
                }
            }
        }
    }
}