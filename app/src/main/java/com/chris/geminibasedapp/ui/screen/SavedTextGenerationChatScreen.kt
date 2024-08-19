package com.chris.geminibasedapp.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.geminibasedapp.ui.viewmodel.AIViewModel

@Composable
fun SavedTextGenerationChatScreen() {

    val aiViewModel = hiltViewModel<AIViewModel>()

    val uiState by aiViewModel.uiState.collectAsState()
    val savedTextGenChat by aiViewModel.savedTextGenerationChatList.collectAsState()

    LaunchedEffect(Unit) {
        aiViewModel.readUserSavedTextGenChat(aiViewModel.currentUser!!)
    }

    Scaffold { paddingValue ->
        Surface(
            modifier = Modifier
                .padding( paddingValues = paddingValue )
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(savedTextGenChat) {chatTitle ->
                    ListCard(title = chatTitle.id) {
                    }
                }

            }
        }
    }

}


@Composable
fun ListCard(
    title: String,
    itemOnClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { itemOnClick() }
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = title)
    }
}