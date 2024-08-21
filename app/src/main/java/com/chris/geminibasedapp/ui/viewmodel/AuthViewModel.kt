package com.chris.geminibasedapp.ui.viewmodel

import android.content.Context
import androidx.credentials.CredentialManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chris.geminibasedapp.common.AuthState
import com.chris.geminibasedapp.common.ChatLine
import com.chris.geminibasedapp.common.SavedChat
import com.chris.geminibasedapp.common.UiState
import com.chris.geminibasedapp.source.AuthRepository
import com.chris.geminibasedapp.source.FirestoreDBRepository
import com.chris.geminibasedapp.utils.Constants
import com.chris.geminibasedapp.utils.DataConversion
import com.google.firebase.auth.FirebaseUser
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

    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState.Initial
    )

    private val _savedTextGenerationChatList: MutableStateFlow<List<SavedChat>> = MutableStateFlow(
        emptyList()
    )
    val savedTextGenerationChatList: StateFlow<List<SavedChat>> = _savedTextGenerationChatList.asStateFlow()


    private val _savedTextGenerationChatContent: MutableStateFlow<List<ChatLine>> = MutableStateFlow(
        emptyList()
    )
    val savedTextGenerationChatContent: StateFlow<List<ChatLine>> = _savedTextGenerationChatContent.asStateFlow()

    val currentUser = authRepository.getCurrentUser()


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

    // Database



    fun createData(
        user: FirebaseUser,
        title: String,
        chatList: List<ChatLine>
    ) {


        val data = DataConversion.textGenerationData(
            title = title,
            chatList = chatList
        )

        _uiState.value = UiState.Loading



        firestoreDBRepository.saveChat(
            title = title,
            textGenerationChat = data,
            user = user,
            callback = {
                _uiState.value = it
            },
            collection = Constants.SAVED_TEXTGENERATION
        )




//        val chatLineMap = chatLine.map { chat ->
//            mapOf(
//                CHAT to chat.chat,      // Explicitly named as "chat"
//                IS_USER to chat.isUser   // Explicitly named as "isUser"
//            )
//        }
//
//        val chatTitle = "Testing Database"
//
//        val pack = hashMapOf(
//            TITLE to chatTitle,
//            CHAT to chatLineMap)
//
//
//
//        val firestore = FirebaseFirestore.getInstance()
//
//
//        firestore.collection(USER_PATH_FIRESTORE)
//            .document(user.email!!)
//            .collection(SAVED_TEXTGENERATION)
//            .add(pack)
//            .addOnSuccessListener {
//                Log.d("Success", "DocumentSnapshot successfully written!")
//                //callback(AuthState.Authenticated)
//            }
//
//            .addOnFailureListener { e ->
//                Log.e("Firestore Error", "Error writing document", e)
//                // callback(
//                //  AuthState.Error(e.message ?: "Something Went Wrong"))
//            }
//        Log.d("User", "End")

    }


    fun readUserData(
        user: FirebaseUser,
        id: String
    ) {


        firestoreDBRepository.fetchIndividualSavedTextGenerationChat(
            user = user,
            id = id,
            callback = {_uiState.value = it},
            result = {_savedTextGenerationChatContent.value = it}
        )


//        val firestore = FirebaseFirestore.getInstance()
//
//        firestore.collection(USER_PATH_FIRESTORE)
//            .document(email)
//            .collection(SAVED_TEXTGENERATION)
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                for (document in querySnapshot) {
//                    val chatTitle = document.get(TITLE)
//
//                    // Process each chatLine here
//                    if (chatTitle != null) {
//                        Log.d("Read Success", "Chat: ${chatTitle}")
//                    }
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.e("Firestore Error", "Error reading document", e)
//            }
    }

//    fun readUserListData(user: FirebaseUser) {
//
//        firestoreDBRepository.fetchSavedChatList(
//            user = user,
//            callback = {
//                _uiState.value = it
//            },
//            result = {
//                _savedTextGenerationChatList.value = it
//            }
//        )
//    }


}