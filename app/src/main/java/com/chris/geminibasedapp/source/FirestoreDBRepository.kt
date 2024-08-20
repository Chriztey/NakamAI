package com.chris.geminibasedapp.source

import com.chris.geminibasedapp.common.ChatLine
import com.chris.geminibasedapp.common.ImageChatLineInStorage
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
    fun fetchSavedChatList(
        user: FirebaseUser,
        callback: (UiState) -> Unit,
        path: String,
        result : (List<SavedChat>) -> Unit
    )

    fun fetchIndividualSavedTextGenerationChat(
        user: FirebaseUser,
        id: String,
        callback: (UiState) -> Unit,
        result: (List<ChatLine>) -> Unit
    )

    fun fetchIndividualSavedMultiModalChat(
        user: FirebaseUser,
        id: String,
        callback: (UiState) -> Unit,
        result: (List<ImageChatLineInStorage>) -> Unit
    )

    fun deleteSavedTextGenerationChat()

}