package com.chris.geminibasedapp.common

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

data class SavedChat(
    val id: String,
    val title: String
)



sealed interface AuthState {
    object Authenticated : AuthState
    object Unauthenticated : AuthState
    object Loading : AuthState
    data class Error(val message: String) : AuthState
}