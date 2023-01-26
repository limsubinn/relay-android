package com.example.relay.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.relay.repository.RunningRepository

class StatisticsViewModel @ViewModelInject constructor(
    val runningRepository: RunningRepository
): ViewModel() {
}