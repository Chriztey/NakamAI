package com.chris.geminibasedapp.source

import com.chris.geminibasedapp.common.AuthState
import com.chris.geminibasedapp.common.UiState
import com.google.firebase.auth.FirebaseUser

interface FirestoreDBRepository {

    fun saveTextGenerationChat(
        textGenerationChat: HashMap<String,Any>,
        user: FirebaseUser,
        callback: (UiState) -> Unit
    )
    fun fetchSavedTextGenerationChat(
        user: FirebaseUser,
        callback: (UiState) -> Unit,
        result : (List<String>) -> Unit
    )

    fun fetchIndividualSavedTextGenerationChat(
        user: FirebaseUser,
        id: String,
        callback: (UiState) -> Unit
    )

    fun deleteSavedTextGenerationChat()

}