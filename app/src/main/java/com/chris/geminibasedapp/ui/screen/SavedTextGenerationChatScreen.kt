package com.chris.geminibasedapp.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.geminibasedapp.R
import com.chris.geminibasedapp.common.LoadingBox
import com.chris.geminibasedapp.common.UiState
import com.chris.geminibasedapp.ui.viewmodel.AIViewModel
import com.chris.geminibasedapp.utils.Constants

@Composable
fun SavedTextGenerationChatScreen(
    onItemClick: (String, String) -> Unit
) {

    val aiViewModel = hiltViewModel<AIViewModel>()

    val uiState by aiViewModel.uiState.collectAsState()
    val savedTextGenChat by aiViewModel.savedTextGenerationChatList.collectAsState()

    fun readList() {
        aiViewModel.readUserSavedChatList(
            path = Constants.SAVED_TEXTGENERATION,
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
                            onItemClick(chatTitle.id,chatTitle.title)
                        },
                        itemDelete = {
                            aiViewModel.deleteSavedTextGenChat(
                                user = aiViewModel.currentUser!!,
                                chatTitle.id)
                            readList()
                        }
                    )
                }

            }
        }


        if(uiState == UiState.Loading) {
            LoadingBox()
        }

    }

}


@Composable
fun ListCard(
    title: String,
    itemOnClick: () -> Unit,
    itemDelete: () -> Unit = {}
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { itemOnClick() }
    ) {
        Row (
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = title
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { itemDelete() }) {
                Icon(painter = painterResource(id = R.drawable.baseline_delete_24), contentDescription = "delete")
            }

        }
    }
}