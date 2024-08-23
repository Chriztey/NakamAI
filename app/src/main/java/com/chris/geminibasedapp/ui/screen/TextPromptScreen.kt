package com.chris.geminibasedapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.geminibasedapp.R
import com.chris.geminibasedapp.common.ChatBubble
import com.chris.geminibasedapp.common.ChatTitleTextField
import com.chris.geminibasedapp.common.ClearChatDialogConfirmation
import com.chris.geminibasedapp.common.SaveChatConfirmationDialog
import com.chris.geminibasedapp.common.UiState
import com.chris.geminibasedapp.ui.viewmodel.AIViewModel

@Composable
fun TextPromptScreen(
) {

    val aiViewModel = hiltViewModel<AIViewModel>()

    val uiState by aiViewModel.uiState.collectAsState()
    val promptState by aiViewModel.chatRoomStateTextGen.collectAsState()
    val context = LocalContext.current

    var textPrompt by rememberSaveable {
        mutableStateOf("")
    }
    var chatTitle by remember {
        mutableStateOf("")
    }

    val localFocusManager = LocalFocusManager.current
    var saveChatConfirmation by remember {
        mutableStateOf(false)
    }
    var saveChatTitle by remember {
        mutableStateOf(false)
    }
    var clearChatConfirmation by remember {
        mutableStateOf(false)
    }



    Scaffold(
        bottomBar = {
            BottomAppBar(
                contentPadding = PaddingValues(8.dp),
                actions = {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        label = {
                            Text(text = "enter your prompt")
                                },
                        value = textPrompt,
                        onValueChange = {
                            textPrompt = it
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {
                            localFocusManager.clearFocus()
                            aiViewModel.sendTextPrompt(textPrompt)
                            aiViewModel.updateChatTextGen(
                                true,
                                textPrompt
                            )
                            textPrompt = ""
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_send_24),
                            contentDescription = "send")
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues),
            onClick = { /*TODO*/ }) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Ask Me Anything",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium)

                    Row {
                        IconButton(onClick = {
                            if (promptState.chat.isNotEmpty()) {
                                saveChatConfirmation = true
                            } else {
                                Toast.makeText(context, "Empty Chat!", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.baseline_save_24
                                ),
                                contentDescription = "save"
                            )
                        }

                        IconButton(onClick = {
                            if(promptState.chat.isNotEmpty()) {
                                clearChatConfirmation = true
                            } else {
                                Toast.makeText(context, "Empty Chat!", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_delete_24), contentDescription = "clear chat")
                        }

                    }


                    LazyColumn(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (promptState.chat.isNotEmpty()) {

                            items(promptState.chat) { chatLine ->

                                Row(

                                    verticalAlignment = Alignment.Top
                                ) {
                                    if (!chatLine.isUser) {
                                        Image(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .size(48.dp),
                                            painter = painterResource(
                                                id = R.drawable.ai_bot_cs), contentDescription = "",
                                            contentScale = ContentScale.Fit
                                            )
                                    }

                                    ChatBubble(
                                        chat = chatLine.chat,
                                        isUser = chatLine.isUser )
                                }
                            }
                        }

                        item {
                            Spacer(
                                modifier = Modifier.weight(1f)
                            )
                            HorizontalDivider(
                                thickness = 2.dp
                            )
                        }
                    }
                }
                

                if (uiState is UiState.Loading) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f)))
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                if (saveChatConfirmation) {
                    SaveChatConfirmationDialog(
                        confirm = {
                            saveChatTitle = true
                            saveChatConfirmation = false
                        },
                        dismiss = {saveChatConfirmation = false})
                }

                if(saveChatTitle) {
                    ChatTitleTextField(
                        modifier = Modifier.align(Alignment.Center),
                        chatTitle = chatTitle,
                        onValueChangeTitle = { chatTitle = it },
                        saveChat = {
                            aiViewModel.saveTextGenerationChat(
                                user = aiViewModel.currentUser!!,
                                title = chatTitle,
                                chatList = promptState.chat
                            )
                            Toast.makeText(context, "Chat Saved", Toast.LENGTH_SHORT).show()
                        },
                        onDismiss = { saveChatTitle = false }
                        ) 
                }

                if(clearChatConfirmation) {
                    ClearChatDialogConfirmation(
                        clearChat = {
                            aiViewModel.clearChatTextGen()
                            Toast.makeText(context, "Chat Cleared", Toast.LENGTH_SHORT).show()
                        }) {
                        clearChatConfirmation = false
                    }
                }
            }
        }
    }
}





