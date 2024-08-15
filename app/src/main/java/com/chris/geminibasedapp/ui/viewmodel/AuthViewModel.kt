package com.chris.geminibasedapp.ui.viewmodel

import android.content.Context
import androidx.credentials.CredentialManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chris.geminibasedapp.common.AuthState
import com.chris.geminibasedapp.common.ChatLine
import com.chris.geminibasedapp.common.UiState
import com.chris.geminibasedapp.source.AuthRepository
import com.chris.geminibasedapp.source.FirestoreDBRepository
import com.chris.geminibasedapp.utils.Constants.CHAT
import com.chris.geminibasedapp.utils.Constants.SAVED_TEXTGENERATION
import com.chris.geminibasedapp.utils.Constants.TITLE
import com.chris.geminibasedapp.utils.Constants.USER_PATH_FIRESTORE
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreDBRepository: FirestoreDBRepository
): ViewModel() {

    private val _authState: MutableStateFlow<AuthState> = MutableStateFlow(
        AuthState.Unauthenticated
    )

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState.Initial
    )

    private val _savedTextGenerationChatList: MutableStateFlow<List<String>> = MutableStateFlow(
        emptyList()
    )
    val savedTextGenerationChatList: StateFlow<List<String>> = _savedTextGenerationChatList.asStateFlow()

    val currentUser = authRepository.getCurrentUser()


    init {
        checkAuthStatus()
    }



    private fun checkAuthStatus() {
        if(currentUser == null) {
            _authState.value = AuthState.Unauthenticated
            Log.d("Login", "Logout")
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
            )
        }

    }

    fun signOut() {
        _authState.value = AuthState.Loading
        authRepository.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    // Database

    fun createData(
        user: FirebaseUser,
        //callback: (AuthState) -> Unit
    ) {

        val chatLine =
            listOf(
                ChatLine("Hello", true),
                ChatLine("Yes?", false)
        )

        val chatTitle = "Testing Database"

        val pack = hashMapOf(
            TITLE to chatTitle,
            CHAT to chatLine)



        val firestore = FirebaseFirestore.getInstance()


        firestore.collection(USER_PATH_FIRESTORE)
            .document(user.email!!)
            .collection(SAVED_TEXTGENERATION)
            .add(pack)
            .addOnSuccessListener {
                Log.d("Success", "DocumentSnapshot successfully written!")
                //callback(AuthState.Authenticated)
            }

            .addOnFailureListener { e ->
                Log.e("Firestore Error", "Error writing document", e)
                // callback(
                //  AuthState.Error(e.message ?: "Something Went Wrong"))
            }
        Log.d("User", "End")

    }


    fun readUserData(email: String,
                     //callback: (AuthState, Map<String, Any>?) -> Unit
    ) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection(USER_PATH_FIRESTORE)
            .document(email)
            .collection(SAVED_TEXTGENERATION)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val chatTitle = document.get(TITLE)

                    // Process each chatLine here
                    if (chatTitle != null) {
                        Log.d("Read Success", "Chat: ${chatTitle}")
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore Error", "Error reading document", e)
            }
    }

    fun readUserListData(user: FirebaseUser) {

        firestoreDBRepository.fetchSavedTextGenerationChat(
            user = user,
            callback = {
                _uiState.value = it
            },
            result = {
                _savedTextGenerationChatList.value = it
            }
        )







        ///
//        val firestore = FirebaseFirestore.getInstance()
//
//        firestore.collection(USER_PATH_FIRESTORE)
//            .document(email)
//            .collection(SAVED_TEXTGENERATION)
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                if (querySnapshot != null && !querySnapshot.isEmpty) {
//
//                    for (document in querySnapshot.documents) {
//                        val chatData = document.get("Chat")
//                        val chatLine = chatData as List<HashMap<*,*>>
//                        for (i in chatLine) {
//                            val chat = i["chat"] as String
//                            val isUser = i["user"] as Boolean
//
//                            Log.d("Read Success", "$isUser = $chat")
//                        }
//
//                        // Process each chatLine here
//
//                        if (chatData != null) {
//                            //Log.d("Read Success", "Chat data: ${chatData}")
//                        } else {
//                           // Log.d("Read Success", "Chat data: null")
//                        }
//                    }
//                } else {
//                   // Log.d("Read Success", "No documents found or QuerySnapshot is null")
//                }
//            }
//            .addOnFailureListener { e ->
//              //  Log.e("Firestore Error", "Error reading document", e)
//            }
    }


}