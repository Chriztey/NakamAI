package com.chris.nakamai.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chris.nakamai.source.OnBoardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject




@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : ViewModel() {

    var _onboardingStatus : StateFlow<Boolean> =
            onBoardingRepository.readOnBoardingState().map {
                it
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    fun updateStatus() {
        _onboardingStatus.let {
            onBoardingRepository.readOnBoardingState().map {
                it
                Log.d("onboard", it.toString())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )
        }
    }

    val onboardingStatus: StateFlow<Boolean> = _onboardingStatus

    init {
        viewModelScope.launch {
            updateStatus()
        }
    }

    fun updateOnboardingStatus(status: Boolean) {
        viewModelScope.launch {
            onBoardingRepository.saveOnBoardingState(status)
        }
    }



}