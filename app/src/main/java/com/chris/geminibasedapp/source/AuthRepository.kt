package com.chris.geminibasedapp.source

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    suspend fun googleSignIn(
        credentialManager: CredentialManager,
        context: Context
    )

    fun getCurrentUser(): FirebaseUser?
    fun signOut()

}