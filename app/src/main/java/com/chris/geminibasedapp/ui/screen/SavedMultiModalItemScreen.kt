package com.chris.geminibasedapp.ui.screen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.chris.geminibasedapp.common.LoadingBox
import com.chris.geminibasedapp.common.UiState
import com.chris.geminibasedapp.ui.viewmodel.AIViewModel
import com.chris.geminibasedapp.utils.DataConversion
import com.chris.geminibasedapp.utils.StorageUtil
import com.chris.geminibasedapp.utils.StorageUtil.Companion.downloadImage
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch

@Composable
fun SavedMultiModalItemScreen(
    documentId: String,
    title: String
) {

    val aiViewModel = hiltViewModel<AIViewModel>()
    val uiState by aiViewModel.uiState.collectAsState()

    val savedMultiModalChatItem by aiViewModel.savedMultiModalChatContent.collectAsState()

    val context = LocalContext.current

    val scope = rememberCoroutineScope()



    LaunchedEffect(Unit) {
        aiViewModel.readUserMultiModalChatData(aiViewModel.currentUser!!, documentId)
    }

    Scaffold { paddingValue ->
        Surface(
            modifier = Modifier.padding(paddingValue)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 32.dp, horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,

                )


                for (chat in savedMultiModalChatItem) {
                    if(!chat.imageId.isNullOrEmpty()) {

                        ImageDisplay(imageId = chat.imageId)

                    }

                    ChatBubble(chat = chat.chat, isUser = chat.isUser)
                }
            }
        }

        if(uiState == UiState.Loading) {
            LoadingBox()
        }
    }

}

@Composable
fun ImageDisplay(imageId: String) {
    // Load image URL asynchronously and update state
    val imageUrl by produceState<Uri?>(initialValue = null) {
        value = downloadImage(imageId)
    }

    // Display the image based on the state
    if (imageUrl != null) {
            AsyncImage(
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .padding(8.dp)
                    ,
                alignment = Alignment.CenterStart,
                contentScale = ContentScale.Fit,
                model = imageUrl,
                contentDescription = null,
            )

    } else {
        // Display a placeholder or loading UI while the image is loading
        CircularProgressIndicator()
    }
}