package com.chris.geminibasedapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.geminibasedapp.common.LoadingBox
import com.chris.geminibasedapp.common.TopBarApp
import com.chris.geminibasedapp.common.UiState
import com.chris.geminibasedapp.ui.viewmodel.AIViewModel
import com.chris.geminibasedapp.utils.Constants

@Composable
fun SavedMultiModalChatScreen(
    onItemClick: (String, String) -> Unit,
    onNavigateToHome: () -> Unit
) {

    val aiViewModel = hiltViewModel<AIViewModel>()
    val context = LocalContext.current

    val uiState by aiViewModel.uiState.collectAsState()
    val savedMultiModalChat by aiViewModel.savedChatList.collectAsState()

    var confirmDelete by remember {
        mutableStateOf(false)
    }

    var selectedChat by remember {
        mutableStateOf("")
    }


    var deleteImage : MutableList<String> = mutableListOf()



    fun readList() {
        aiViewModel.readUserSavedChatList(
            path = Constants.SAVED_MULTIMODAL,
            user = aiViewModel.currentUser!!)
    }

    LaunchedEffect(Unit) {
        readList()
    }

    Scaffold(
        topBar = {
            TopBarApp(
                onNavigateToHome = {onNavigateToHome()}
            )
        }
    ) { paddingValue ->
        Surface(
            modifier = Modifier
                .padding( paddingValues = paddingValue )
        ) {
            if (savedMultiModalChat.isEmpty() && uiState != UiState.Loading) {

                EmptyList()

            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 32.dp, horizontal = 16.dp)
                ) {
                    items(savedMultiModalChat) { chatTitle ->
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

                            }
                        )
                    }

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
                        Toast.makeText(context, "Chat Deleted", Toast.LENGTH_SHORT).show()
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

@Composable
fun EmptyList() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Empty List")
    }
}


