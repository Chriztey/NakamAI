package com.chris.nakamai.source

import com.chris.nakamai.common.ChatLine
import com.chris.nakamai.common.ImageChatLineInStorage
import com.chris.nakamai.common.SavedChat
import com.chris.nakamai.common.UiState
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

    fun deleteSavedChat(
        user: FirebaseUser,
        collection: String,
        documentId: String,
        callback: (UiState) -> Unit,
    )

}