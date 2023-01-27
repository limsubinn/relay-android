package com.example.relay.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.relay.db.Run
import com.example.relay.repository.RunningRepository
import kotlinx.coroutines.launch

class RunningViewModel @ViewModelInject constructor(
    val runningRepository: RunningRepository
): ViewModel() {

    fun insertRun(run: Run) = viewModelScope.launch {
        runningRepository.insertRun(run)
    }
}