package com.chris.geminibasedapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.geminibasedapp.R
import com.chris.geminibasedapp.common.ChatLine
import com.chris.geminibasedapp.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun DashboardScreen() {

    val authViewModel = hiltViewModel<AuthViewModel>()
    val user = authViewModel.currentUser
    val savedTextGenerationSavedList by
        authViewModel.savedTextGenerationChatList.collectAsState()
    val savedTextGenerationChatContent by
    authViewModel.savedTextGenerationChatContent.collectAsState()

    val context = LocalContext.current
    val credentialManager = CredentialManager.create(context)
    val scope = rememberCoroutineScope()

    val chatLine =
        listOf(
            ChatLine("Hello", true),
            ChatLine("Yes?", false)
        )

    Scaffold { paddingValues ->

        Surface(modifier = Modifier
            .padding(paddingValues)) {

            Column(
                modifier = Modifier
                    .padding(vertical = 32.dp, horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    text = "Discover AI-powered creativity! " +
                            "Generate text from simple prompts or combine text and images for unique outputs.",
                    textAlign = TextAlign.Center
                    )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .paddingFromBaseline(bottom = 8.dp),
                            //.padding(bottom = 8.dp, end = 4.dp),
                        text = "Powered by",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Start
                    )


                    Image(
                        modifier = Modifier.size(64.dp),
                        contentScale = ContentScale.Fit,

                        painter = painterResource(
                            id = R.drawable.google_ai_gemini),
                        contentDescription = "gemini ai" )
                }
                
                Button(onClick = {

                    authViewModel.signInWithGoogle(
                        credentialManager,
                        context
                    )
                    }) {
                    Text(text = "Login")
                }

                Button(onClick = { authViewModel.signOut() }) {
                    Text(text = "Signout")
                }
                Button(onClick = { authViewModel.createData(
                    user!!,"Dashboard",chatLine
                ) }) {
                    Text(text = "write")
                }


//                Button(onClick = { authViewModel.readUserData(
//                    user!!,
//                    id = "title"
//                ) }) {
//                    Text(text = "firestore chat")
//                }
//                Button(onClick = { authViewModel.readUserListData(
//                    user!!
//                ) }) {
//                    Text(text = "firestore list")
//                }

//                if (savedTextGenerationSavedList.isNotEmpty()) {
//                    for (i in savedTextGenerationSavedList) {
//                        Text(text = i.title )
//                    }
//                }
//
//                if(savedTextGenerationChatContent.isNotEmpty()) {
//                    for (chatContent in savedTextGenerationChatContent) {
//                        Text(text = "${chatContent.isUser} : ${chatContent.chat}")
//                    }
//                }

            }

        }

    }

}