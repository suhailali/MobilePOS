package com.skegworks.mobilepos.ui.component.clock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ClockViewModel : ViewModel() {

    private val _time = MutableStateFlow(LocalDateTime.now())
    val time = _time.asStateFlow()

    private val _showColon = MutableStateFlow(true)
    val showColon = _showColon.asStateFlow()

    private var job: Job? = null

    fun start() {
        if (job != null) return

        job = viewModelScope.launch {
            while (isActive) {
                val now = LocalDateTime.now()

                _time.value = now
                _showColon.value = now.second % 2 == 0  // blinking colon toggle

                delay(1000)
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }
}
