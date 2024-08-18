package com.chris.geminibasedapp.source

import com.chris.geminibasedapp.common.ChatLine
import com.chris.geminibasedapp.common.SavedChat
import com.chris.geminibasedapp.common.UiState
import com.google.firebase.auth.FirebaseUser

interface FirestoreDBRepository {

    fun saveChat(
        title: String,
        textGenerationChat: HashMap<String,Any>,
        user: FirebaseUser,
        callback: (UiState) -> Unit,
        collection: String
    )
    fun fetchSavedTextGenerationChat(
        user: FirebaseUser,
        callback: (UiState) -> Unit,
        result : (List<SavedChat>) -> Unit
    )

    fun fetchIndividualSavedTextGenerationChat(
        user: FirebaseUser,
        id: String,
        callback: (UiState) -> Unit,
        result: (List<ChatLine>) -> Unit
    )

    fun deleteSavedTextGenerationChat()

}