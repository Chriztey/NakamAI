package com.chris.geminibasedapp.ui.screen

import android.widget.Space
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.geminibasedapp.R
import com.chris.geminibasedapp.common.UiState
import com.chris.geminibasedapp.ui.component.AppTopBar
import com.chris.geminibasedapp.ui.viewmodel.AIViewModel

@Composable
fun TextPromptScreen(
) {

    val aiViewModel = hiltViewModel<AIViewModel>()

    val uiState by aiViewModel.uiState.collectAsState()
    val promptState by aiViewModel.promptState.collectAsState()
    val context = LocalContext.current

    var textPrompt by rememberSaveable {
        mutableStateOf("")
    }

    var localFocusManager = LocalFocusManager.current



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
