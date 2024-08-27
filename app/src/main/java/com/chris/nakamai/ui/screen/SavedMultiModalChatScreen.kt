package com.chris.nakamai.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.nakamai.common.DeleteSavedChatDialog
import com.chris.nakamai.common.EmptyList
import com.chris.nakamai.common.ListCard
import com.chris.nakamai.common.LoadingBox
import com.chris.nakamai.common.TopBarApp
import com.chris.nakamai.common.UiState
import com.chris.nakamai.ui.viewmodel.AIViewModel
import com.chris.nakamai.utils.Constants

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

    val deleteImage : MutableList<String> = mutableListOf()

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
                title = "Saved MULTIMODAL Chat",
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
                        
                        Spacer(modifier = Modifier.height(8.dp))
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




