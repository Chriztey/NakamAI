package com.chris.geminibasedapp.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.geminibasedapp.ui.viewmodel.AIViewModel

@Composable
fun SavedTextGenItemScreen(
    documentId: String,
    title: String
) {
    val aiViewModel = hiltViewModel<AIViewModel>()

    val savedTextGenChatItem by aiViewModel.savedTextGenerationChatContent.collectAsState()

    LaunchedEffect(Unit) {
        aiViewModel.readUserData(aiViewModel.currentUser!!, documentId)
    }

    Scaffold { paddingValue ->
        Surface(
            modifier = Modifier.padding(paddingValue)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
                )

            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp, horizontal = 16.dp)) {
                items(savedTextGenChatItem) {chat ->

                    ChatBubble(chat = chat.chat, isUser = chat.isUser)

                }
            }
        }
    }

}