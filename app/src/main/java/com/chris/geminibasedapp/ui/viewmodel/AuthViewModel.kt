package com.chris.geminibasedapp.ui.viewmodel

import android.content.Context
import androidx.credentials.CredentialManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chris.geminibasedapp.common.AuthState
import com.chris.geminibasedapp.source.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _authState: MutableStateFlow<AuthState> = MutableStateFlow(
        AuthState.Unauthenticated)

    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val currentUser = authRepository.getCurrentUser()


    init {
        checkAuthStatus()
    }



    private fun checkAuthStatus() {
        if(currentUser == null) {
            _authState.value = AuthState.Unauthenticated
            Log.d("Logout", "Logout")
        } else {
            _authState.value = AuthState.Authenticated
            Log.d("Login", "Login")
        }
    }


    fun signInWithGoogle(
        credentialManager: CredentialManager,
        context: Context
    ) {

        viewModelScope.launch {
            authRepository.googleSignIn(
                credentialManager,
                context
            ) { _authState.value = it }
        }

    }

    fun signOut() {
        _authState.value = AuthState.Loading
        authRepository.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}