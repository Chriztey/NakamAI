package com.chris.geminibasedapp.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chris.geminibasedapp.common.ChatLine
import com.chris.geminibasedapp.common.PromptState
import com.chris.geminibasedapp.common.UiState
import com.chris.geminibasedapp.source.AIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AIViewModel @Inject constructor(
    private val aiRepository: AIRepository
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    private val _imagePromptState: MutableStateFlow<Bitmap?> =
        MutableStateFlow(null)
    val imagePromptState: StateFlow<Bitmap?> =
        _imagePromptState.asStateFlow()

    private val _promptState: MutableStateFlow<PromptState> =
        MutableStateFlow(PromptState())
    val promptState: StateFlow<PromptState> =
        _promptState.asStateFlow()

    fun sendTextImagePrompt(
        bitmap: Bitmap,
        prompt: String
    ) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            aiRepository.sentTextImagePrompt(
                bitmap = bitmap,
                prompt = prompt,
                callback = {
                    _uiState.value = it
                },
                result = {
                    updateChat(
                        isUser = false,
                        chat = it
                    )
                }
            )
        }
    }

    fun sendTextPrompt(
        prompt: String
    ) {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            aiRepository.sendTextPrompt(
                prompt = prompt,
                callback = {
                    _uiState.value = it
                },
                result = {
                    _promptState.update {
                        current -> current.copy(
                            chat = current.chat +
                                    ChatLine(
                                        chat = it,
                                        isUser = false)
                        )
                    }
                },
                inquiry = {
                    _promptState.update {
                            current -> current.copy(
                        chat = current.chat +
                                ChatLine(
                                    chat = it,
                                    isUser = true)
                    )
                    }
                }
            )
        }

    }

    fun updateBitmap(
        bitmap: Bitmap
    ) {
        _uiState.value = UiState.Loading
        _imagePromptState.value = bitmap
        _uiState.value = UiState.Success("Image Loaded")
    }

    fun updateChat(
        isUser: Boolean,
        chat: String
    ) {

        _promptState.update {
                current -> current.copy(
            chat = current.chat +
                    ChatLine(
                        chat = chat,
                        isUser = isUser)
                )
        }
    }


    fun clearChat() {
        _promptState.update {
            current -> current.copy(
                chat = emptyList()
            )
        }
    }
}