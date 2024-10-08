package com.chris.nakamai.data

import android.content.Context
import androidx.credentials.CredentialManager
import android.util.Log
import android.widget.Toast
import androidx.credentials.GetCredentialRequest
import com.chris.nakamai.BuildConfig
import com.chris.nakamai.common.AuthState
import com.chris.nakamai.source.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class AuthRepoImplementation @Inject constructor(
    private val auth : FirebaseAuth,
): AuthRepository {


    override suspend fun googleSignIn(
        credentialManager: CredentialManager,
        context: Context,
        callback: (AuthState) -> Unit
    ) {
        val auth : FirebaseAuth = FirebaseAuth.getInstance()

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential

            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(credential.data)

            val googleIdToken = googleIdTokenCredential.idToken

            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)


            // _authState.value = AuthState.Loading

            auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        Log.d("Login", "Success")
                        callback(AuthState.Authenticated)
//                            _authState.value = AuthState.Authenticated
//                            createProfile(
//
//                            )
                    }
                }

        } catch (e: Exception) {
            callback(AuthState.Unauthenticated)
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }


    }


    override fun getCurrentUser(): FirebaseUser? = auth.currentUser
    override fun signOut() {
        auth.signOut()
    }


}