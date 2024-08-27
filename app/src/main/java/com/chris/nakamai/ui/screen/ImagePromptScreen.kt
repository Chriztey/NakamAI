package com.chris.nakamai.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.nakamai.R
import com.chris.nakamai.common.ChatTitleTextField
import com.chris.nakamai.common.ClearChatDialogConfirmation
import com.chris.nakamai.common.SaveChatConfirmationDialog
import com.chris.nakamai.common.UiState
import com.chris.nakamai.common.imageChatLineToStorage
import com.chris.nakamai.ui.viewmodel.AIViewModel
import com.chris.nakamai.utils.DataConversion
import kotlinx.coroutines.launch

@Composable
fun ImagePromptScreen() {
    val scope = rememberCoroutineScope()

    var clearChatConfirmation by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val aiViewModel = hiltViewModel<AIViewModel>()

    val uiState by aiViewModel.uiState.collectAsState()
    val promptState by aiViewModel.chatRoomStateMultiModal.collectAsState()
    val imagePromptState by aiViewModel.imagePromptState.collectAsState()

    var currentImage by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var textPrompt by rememberSaveable {
        mutableStateOf("")
    }

    val localFocusManager = LocalFocusManager.current

    var chatTitle by remember {
        mutableStateOf("")
    }

    var saveChatConfirmation by remember {
        mutableStateOf(false)
    }
    var saveChatTitle by remember {
        mutableStateOf(false)
    }

    var hasCameraPermission by remember { mutableStateOf(false) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    fun updateChatImage(): Bitmap? {
        return if (currentImage != imagePromptState) {
            imagePromptState
        } else null
    }

    fun selectedImage(){
        currentImage = imagePromptState
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            hasCameraPermission = true
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            aiViewModel.updateBitmap(bitmap)

        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                val bitmap = DataConversion.uriToBitmap(context, it)
                if (bitmap != null) {
                    aiViewModel.updateBitmap(bitmap)
                }
            }
        }
    )

    val listState = rememberLazyListState()

    val showButton by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    fun scrollToBottom() {
        scope.launch {
            listState.animateScrollToItem(promptState.chat.size)
        }
    }



    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
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

                            if (imagePromptState != null) {
                                aiViewModel.updateChatMultiModal(
                                    isUser = true,
                                    chat = textPrompt,
                                    image = null
                                )

                                aiViewModel.sendTextImagePrompt(
                                    bitmap = imagePromptState!!,
                                    prompt = textPrompt,
                                    selectedImage = updateChatImage())

                                selectedImage()

                            } else {
                                Toast.makeText(context, "You haven't add Any Image yet",
                                    Toast.LENGTH_SHORT).show()
                            }

                            scrollToBottom()
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
                
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 32.dp,
                            bottom = 8.dp
                        )
                ) {
                    item() {
                        if (imagePromptState != null) {
                            Text(
                                text = "Current Image:",
                                style = MaterialTheme.typography.titleLarge
                            )

                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .size(250.dp)
                                    .clip(RoundedCornerShape(32.dp))
                                ,
                                contentScale = ContentScale.Fit,
                                bitmap = imagePromptState!!.asImageBitmap(),
                                contentDescription = "image")

                            HorizontalDivider(thickness = 2.dp)
                            Text(
                                modifier = Modifier.padding(top = 8.dp),
                                text = "Prompt Recommendation:",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(onClick = {
                                    aiViewModel.updateChatMultiModal(
                                        isUser = true,
                                        chat = "What is This?"
                                    )

                                    if (imagePromptState != null) {
                                        aiViewModel.sendTextImagePrompt(
                                            imagePromptState!!,
                                            "What is This?",
                                            selectedImage = updateChatImage()
                                        )

                                        selectedImage()

                                    } else Toast.makeText(context, "You haven't choose any image yet", Toast.LENGTH_SHORT).show()

                                    scrollToBottom()

                                }) {
                                    Text(
                                        text = "What is This?")
                                }
                            }
                            HorizontalDivider(thickness = 2.dp)
                        } else {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                                    //.align(Alignment.CenterHorizontally)
                                    .size(250.dp)
                                    .clip(CircleShape)
                                ,
                                contentScale = ContentScale.Fit,
                                painter = painterResource(id = R.drawable.ai_bot_cs),
                                contentDescription = "placeholder")

                            Text (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                text = "Hello! This is a multimodal AI. " +
                                        "Please provide an image and text prompt to get started.",
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    if(promptState.chat.isNotEmpty()) {

                        items(promptState.chat.size) { index ->
                            val chat = promptState.chat[index]
                            if (chat.image != null) {
                                Image(
                                    modifier = Modifier
                                        .size(250.dp)
                                        .padding(16.dp)
                                        .align(Alignment.CenterStart),
                                    contentScale = ContentScale.Fit,
                                    bitmap = chat.image.asImageBitmap(),
                                    contentDescription = null
                                )
                            }

                            SelectionContainer {
                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    text = chat.chat,
                                    style = if (chat.isUser) {
                                        MaterialTheme.typography.bodyLarge
                                    } else {
                                        MaterialTheme.typography.bodyMedium
                                    }
                                )
                            }
                        }
                    }

                    item() {
                        Row(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            if(promptState.chat.isNotEmpty()) {
                                IconButton(onClick = {
                                    clearChatConfirmation = true
                                }) {
                                    Icon(painter = painterResource(
                                        id = R.drawable.baseline_delete_24),
                                        contentDescription = "clear chat")
                                }

                                IconButton(onClick = {
                                    saveChatConfirmation = true
                                }) {
                                    Icon(painter = painterResource(
                                        id = R.drawable.baseline_save_24),
                                        contentDescription = "save chat")
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            IconButton(onClick = {
                                if(hasCameraPermission) {
                                    cameraLauncher.launch()}
                                else {cameraPermissionLauncher
                                    .launch(Manifest.permission.CAMERA)}
                            }) {
                                Icon(painter = painterResource(
                                    id = R.drawable.baseline_photo_camera_24),
                                    contentDescription = "camera launch")
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            IconButton(onClick = {
                                galleryLauncher.launch(
                                    "image/*")
                            }) {
                                Icon(painter = painterResource(
                                    id = R.drawable.baseline_photo_library_24),
                                    contentDescription = "gallery launch")
                            }

                        }
                    }

                }


                if(showButton) {
                    FloatingActionButton(
                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp),
                        onClick = {
                            scope.launch {
                                listState.animateScrollToItem(0)
                            }
                        }) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.baseline_photo_library_24),
                            contentDescription = "image preview")
                    }
                }


                if (clearChatConfirmation) {
                    ClearChatDialogConfirmation(
                        clearChat = {
                            aiViewModel.clearChatMultiModal()
                            Toast.makeText(context, "Chat Cleared", Toast.LENGTH_SHORT).show()},
                        cancelAction = {clearChatConfirmation = false}
                        )
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

                            scope.launch {
                                aiViewModel.saveMultiModalChat(
                                    context = context,
                                    user = aiViewModel.currentUser!!,
                                    title = chatTitle,
                                    chatList = imageChatLineToStorage(
                                        title = chatTitle,
                                        imageChatLine = promptState.chat)
                                )
                                Toast.makeText(context, "Chat Saved", Toast.LENGTH_SHORT).show()
                            }

                        },
                        onDismiss = { saveChatTitle = false }
                    )
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

