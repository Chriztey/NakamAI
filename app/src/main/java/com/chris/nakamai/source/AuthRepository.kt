package com.chris.nakamai.source

import android.content.Context
import androidx.credentials.CredentialManager
import com.chris.nakamai.common.AuthState
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    suspend fun googleSignIn(
        credentialManager: CredentialManager,
        context: Context,
        callback: (AuthState) -> Unit
    )

    fun getCurrentUser(): FirebaseUser?
    fun signOut()

}