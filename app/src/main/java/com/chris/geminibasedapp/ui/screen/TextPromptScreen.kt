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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.geminibasedapp.R
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

    var localFocusManager = LocalFocusManager.current
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
    ) {
        Surface(
            modifier = Modifier.padding(it),
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
                        style = MaterialTheme.typography.headlineMedium
                        )

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
                                    if (chatLine.isUser == false) {
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
//                                        modifier = modifier
//                                            .align(
//                                            if(chatLine.isUser)
//                                                Alignment.End else Alignment.Start),
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




@Composable
fun ChatBubble(
    modifier: Modifier = Modifier,
    chat: String,
    isUser: Boolean
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = modifier
                .clip(
                    if (isUser) {
                        RoundedCornerShape(16.dp)
                    } else {
                        RoundedCornerShape(0.dp)
                    }
                )
                .background(
                    if (isUser) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.background
                )
                .fillMaxWidth(0.7f)
                .wrapContentSize()
                .align(
                    if (isUser)
                        Alignment.End else Alignment.Start
                ),
            contentAlignment = Alignment.CenterStart
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    //.align(Alignment.CenterStart),
                textAlign = if (isUser) TextAlign.End else TextAlign.Start,
                text = chat
            )

        }
    }

}

@Composable
fun SaveChatConfirmationDialog(
    confirm: () -> Unit,
    dismiss: () -> Unit
) {

    AlertDialog(
        icon = {
            Icon(painter = painterResource(
                id = R.drawable.baseline_save_24),
                contentDescription = "save chat")
        },
        title = {
            Text(text = "Save Chat")
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Do you want to save this chat ?")
        },
        onDismissRequest = {
            dismiss() },
        confirmButton = {
            TextButton(onClick = { confirm()  }) {
                Text(text = "YES")
            }
        })

}

@Composable
fun ChatTitleTextField(
    modifier: Modifier = Modifier,
    chatTitle: String,
    onValueChangeTitle: (String) -> Unit,
    saveChat: () -> Unit,
    onDismiss: () -> Unit
) {


    Box(
        modifier = modifier
            .wrapContentSize()
            .padding(32.dp)
            .clip(RoundedCornerShape(32.dp))
            //  .align(Alignment.Center)
            .background(Color.LightGray.copy(alpha = 0.9f))
        ,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                text = "CHAT TITLE")
            OutlinedTextField(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp),
                value = chatTitle,
                onValueChange = { onValueChangeTitle(it) })

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                IconButton(onClick = {
                    saveChat()
                    onDismiss()
                }) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.baseline_save_24
                        ),
                        contentDescription = "save"
                    )
                }
                IconButton(onClick = { onDismiss() }) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.baseline_cancel_24
                        ),
                        contentDescription = "cancel"
                    )
                }
            }
        }
    }

}
