package com.chris.nakamai.common

import android.graphics.Bitmap

/**
 * A sealed hierarchy describing the state of the text generation.
 */
sealed interface UiState {

    /**
     * Empty state when the screen is first shown
     */
    object Initial : UiState

    /**
     * Still loading
     */
    object Loading : UiState

    /**
     * Text has been generated
     */
    data class Success(val outputText: String) : UiState

    /**
     * There was an error generating text
     */
    data class Error(val errorMessage: String) : UiState
}

data class PromptState (
    val chat : List<ChatLine> = emptyList()
)

data class ImagePromptState(
    val chat: List<ImageChatLine> = emptyList()
)

data class ChatLine(
    val chat : String,
    val isUser : Boolean
)

data class ImageChatLine(
    val image: Bitmap?,
    val chat : String,
    val isUser : Boolean
)

data class ImageChatLineInStorage(
    val image: Bitmap?,
    val imageId : String?,
    val chat: String,
    val isUser: Boolean
)

data class SavedChat(
    val id: String,
    val title: String,
    val chat: Any
)



sealed interface AuthState {
    object Authenticated : AuthState
    object Unauthenticated : AuthState
    object Loading : AuthState
    data class Error(val message: String) : AuthState
}

fun imageChatLineToStorage(
    title: String,
    imageChatLine: List<ImageChatLine>,

): List<ImageChatLineInStorage> {

    val tempList : MutableList<ImageChatLineInStorage> = mutableListOf()
    var imageId = 1


    for (i in imageChatLine) {
        tempList.add(
            ImageChatLineInStorage(
                image = i.image,
                imageId = if (i.image != null) {
                    //UUID.randomUUID().toString()
                    "$title-$imageId"
                } else null,
                chat = i.chat,
                isUser = i.isUser
            )

        )

        imageId++



    }

    return tempList


}