package com.chris.geminibasedapp.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chris.geminibasedapp.common.ChatLine
import com.chris.geminibasedapp.common.ImageChatLine
import com.chris.geminibasedapp.common.ImageChatLineInStorage
import com.chris.geminibasedapp.common.ImagePromptState
import com.chris.geminibasedapp.common.PromptState
import com.chris.geminibasedapp.common.UiState
import com.chris.geminibasedapp.source.AIRepository
import com.chris.geminibasedapp.source.AuthRepository
import com.chris.geminibasedapp.source.FirestoreDBRepository
import com.chris.geminibasedapp.utils.Constants
import com.chris.geminibasedapp.utils.DataConversion
import com.chris.geminibasedapp.utils.StorageUtil
import com.google.firebase.auth.FirebaseUser
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
    private val authRepository: AuthRepository,
    private val aiRepository: AIRepository,
    private val firestoreDBRepository: FirestoreDBRepository,

): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    private val _imagePromptState: MutableStateFlow<Bitmap?> =
        MutableStateFlow(null)
    val imagePromptState: StateFlow<Bitmap?> =
        _imagePromptState.asStateFlow()

    private val _chatRoomStateTextGen: MutableStateFlow<PromptState> =
        MutableStateFlow(PromptState())
    val chatRoomStateTextGen: StateFlow<PromptState> =
        _chatRoomStateTextGen.asStateFlow()

    private val _chatRoomStateMultiModal: MutableStateFlow<ImagePromptState> =
        MutableStateFlow(ImagePromptState())
    val chatRoomStateMultiModal: StateFlow<ImagePromptState> =
        _chatRoomStateMultiModal.asStateFlow()

    val currentUser = authRepository.getCurrentUser()

    fun sendTextImagePrompt(
        bitmap: Bitmap,
        prompt: String,
        selectedImage: Bitmap?
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


                    updateChatMultiModal(
                        isUser = false,
                        chat = it,
                        image = selectedImage
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
                    _chatRoomStateTextGen.update {
                        current -> current.copy(
                            chat = current.chat +
                                    ChatLine(
                                        chat = it,
                                        isUser = false)
                        )
                    }
                },
//                inquiry = {
//                    _promptState.update {
//                            current -> current.copy(
//                        chat = current.chat +
//                                ChatLine(
//                                    chat = it,
//                                    isUser = true)
//                    )
//                    }
//                }
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

    fun updateChatTextGen(
        isUser: Boolean,
        chat: String,
    ) {

        _chatRoomStateTextGen.update {
                current -> current.copy(
            chat = current.chat +
                    ChatLine(
                        chat = chat,
                        isUser = isUser)
                )
        }
    }

    fun updateChatMultiModal(
        isUser: Boolean,
        chat: String,
        image: Bitmap? = null,
    ) {

        _chatRoomStateMultiModal.update {
                current -> current.copy(
            chat = current.chat +
                    ImageChatLine(
                        image = image,
                        chat = chat,
                        isUser = isUser)
        )
        }
    }

    fun clearChatMultiModal() {

        _uiState.value = UiState.Loading

        _chatRoomStateMultiModal.update {
                current -> current.copy(
            chat = emptyList()
        )
        }

        _uiState.value = UiState.Success("Cleared")
    }


    fun clearChatTextGen() {

        _uiState.value = UiState.Loading

        _chatRoomStateTextGen.update {
            current -> current.copy(
                chat = emptyList()
            )
        }
        _uiState.value = UiState.Success("Cleared")
    }


    fun saveTextGenerationChat(
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
    }

    fun saveMultiModalChat(
        context: Context,
        user: FirebaseUser,
        title: String,
        chatList: List<ImageChatLineInStorage>
    ) {
        val data = DataConversion.multiModalData(
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
            collection = Constants.SAVED_MULTIMODAL
        )

        for (chat in chatList) {
            if (chat.imageId != null) {
                StorageUtil.uploadToStorage(
                    chat.image!!,
                    context,
                    chat.imageId
                )
            }
        }
    }

}