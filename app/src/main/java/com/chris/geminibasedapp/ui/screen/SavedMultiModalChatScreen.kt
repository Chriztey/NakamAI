package com.chris.geminibasedapp.ui.screen

import android.media.Image
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.geminibasedapp.common.ImageChatLineInStorage
import com.chris.geminibasedapp.common.LoadingBox
import com.chris.geminibasedapp.common.UiState
import com.chris.geminibasedapp.ui.viewmodel.AIViewModel
import com.chris.geminibasedapp.utils.Constants

@Composable
fun SavedMultiModalChatScreen(
    onItemClick: (String, String) -> Unit
) {

    val aiViewModel = hiltViewModel<AIViewModel>()

    val uiState by aiViewModel.uiState.collectAsState()
    val savedTextGenChat by aiViewModel.savedTextGenerationChatList.collectAsState()

    var confirmDelete by remember {
        mutableStateOf(false)
    }

    var selectedChat by remember {
        mutableStateOf("")
    }

    var selectedChatLine : List<ImageChatLineInStorage> = emptyList()

    var deleteImage : MutableList<String> = mutableListOf()





    fun readList() {
        aiViewModel.readUserSavedChatList(
            path = Constants.SAVED_MULTIMODAL,
            user = aiViewModel.currentUser!!)
    }

    LaunchedEffect(Unit) {
        readList()
    }

    Scaffold { paddingValue ->
        Surface(
            modifier = Modifier
                .padding( paddingValues = paddingValue )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 32.dp, horizontal = 16.dp)
            ) {



                items(savedTextGenChat) {chatTitle ->


                    ListCard(
                        title = chatTitle.title,
                        itemOnClick = {
                            onItemClick(chatTitle.id, chatTitle.title)
                        },
                        itemDelete = {
                            confirmDelete = true
                            selectedChat = chatTitle.id

                            val temp = chatTitle.chat as List<HashMap<*,*>>

                            for (i in temp) {
                                if (i[Constants.IMAGE] != null) {
                                    deleteImage.add(i[Constants.IMAGE].toString())
                                }
                            }

                            Log.d("Test Image", deleteImage.toString())



                        }
                    )
                }

            }

            if (confirmDelete) {
                DeleteSavedChatDialog(
                    onDismiss = { confirmDelete = false},
                    onConfirm = {
                        aiViewModel.deleteSavedMultiModalChat(
                            user = aiViewModel.currentUser!!,
                            documentId = selectedChat,
                        )
                        aiViewModel.deleteMultiModalChatImage(deleteImage)
                        readList()
                    }
                )
            }

            if(uiState == UiState.Loading) {
                LoadingBox()
            }
        }
    }

}


