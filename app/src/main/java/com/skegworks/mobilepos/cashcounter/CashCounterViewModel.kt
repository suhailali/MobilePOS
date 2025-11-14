package com.skegworks.mobilepos.cashcounter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skegworks.mobilepos.data.domain.CashCounter
import com.skegworks.mobilepos.data.domain.CashCounterStatus
import com.skegworks.mobilepos.data.mapper.toFirestoreDto
import com.skegworks.mobilepos.data.remote.firestore.FirestoreHelper
import com.skegworks.mobilepos.utils.Constants
import com.skegworks.mobilepos.utils.DateUtility
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
    private val cashCounterRepository: CashCounterRepository,
    private val dateUtility: DateUtility
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
                if (intent.cash == 0.0) {
                    _state.update {
                        it.copy(
                            error = "Cash can not be zero",
                            buttonEnabled = false
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            error = null,
                            buttonEnabled = true
                        )
                    }
                }
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
                id = cashCounter.id,
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

    fun fetchLatestCashCounter() {
        viewModelScope.launch(Dispatchers.IO) {
            val cashCounter = cashCounterRepository.getLatestCashCounter()
            val today = dateUtility.getDateForToday()
            if (cashCounter == null || cashCounter.status == CashCounterStatus.CLOSED) {
                _state.update {
                    it.copy(
                        state = CashCounterStatus.CLOSED,
                        cashCounterAction = CashCounterAction.ACTION_OPEN,
                        buttonText = "Open Cash Counter for Today $today"
                    )
                }
            } else {
                if (dateUtility.isDateToday(
                        cashCounter.date,
                        Constants.DateFormat.DATE_TIME_FORMAT
                    )
                ) {
                    _state.update {
                        it.copy(
                            state = CashCounterStatus.OPEN,
                            cashCounterAction = CashCounterAction.ACTION_CLOSE,
                            buttonText = "Close Cash Counter for Today $today"
                        )
                    }
                } else {
                    val formattedDate = dateUtility.formatDate(cashCounter.date, Constants.DateFormat.DATE_TIME_FORMAT, "dd/MM/yyyy")
                    _state.update {
                        it.copy(
                            state = CashCounterStatus.OPEN,
                            cashCounterAction = CashCounterAction.ACTION_CLOSE_AND_OPEN,
                            buttonText = "Close Cash Counter for $formattedDate and Open for Today $today"
                        )
                    }
                }
            }
        }
    }
}