package com.chris.geminibasedapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.geminibasedapp.R
import com.chris.geminibasedapp.common.AuthState
import com.chris.geminibasedapp.ui.viewmodel.AuthViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun DashboardScreen(
    navigateToSavedMultiModal: () -> Unit,
    navigateToSavedTextGen: () -> Unit,
    clearBackStack: () -> Unit,
    navigateToLogin: () -> Unit
) {


    val authViewModel = hiltViewModel<AuthViewModel>()
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(key1 = authState) {
        when (authState) {
            AuthState.Unauthenticated -> navigateToLogin()
            else -> Unit
        }
    }


    Scaffold { paddingValues ->

        Surface(modifier = Modifier
            .padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .padding(vertical = 32.dp, horizontal = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Logout")
                    IconButton(
                        modifier = Modifier
                            .padding(end = 8.dp),

                        onClick = {
                            authViewModel.signOut()
                            clearBackStack()
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_logout_24),
                            contentDescription = "logout"
                        )
                    }
                }

                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(300.dp)
                        .padding(16.dp),
                    painter = painterResource(id = R.drawable.nakamai),
                    contentDescription = "logo")
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    text = "Your AI Nakama, Always by Your Side."
                )


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

                HorizontalDivider(thickness = 4.dp)

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.headlineSmall,
                        text = "Saved Chat"
                    )

                    Icon(painter = painterResource(id = R.drawable.baseline_save_24), contentDescription = "save")
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    Button(
                        onClick = { navigateToSavedTextGen() },
                        shape = RoundedCornerShape(8.dp)
                        ) {
                        Text(text = "Text-Generation")
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        modifier = Modifier.padding(start = 4.dp),
                        onClick = {navigateToSavedMultiModal()},
                        shape = RoundedCornerShape(8.dp)
                        ) {
                        Text(text = "Multi-Modal")
                    }
                }
            }

        }

    }

}