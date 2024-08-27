package com.chris.nakamai.ui.screen


import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.nakamai.R
import com.chris.nakamai.common.AuthState
import com.chris.nakamai.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navigateToHome: () -> Unit
) {
    val authViewModel = hiltViewModel<AuthViewModel>()
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current
    val credentialManager = CredentialManager.create(context)

    LaunchedEffect(key1 = authState) {
        when(
            authState
        ) {
            is AuthState.Authenticated -> navigateToHome()
            is AuthState.Error -> Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            AuthState.Loading -> Unit
            AuthState.Unauthenticated -> Unit
        }
    }

    Scaffold { paddingValues ->

        Surface(modifier = Modifier
            .padding(paddingValues)) {

            Column(
                modifier = Modifier
                    .padding(vertical = 32.dp, horizontal = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

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

                Button(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    border = BorderStroke(width = 4.dp, color = Color(17, 90, 172
                    )),
                    onClick = {
                        authViewModel.signInWithGoogle(
                        credentialManager,
                        context)
                    },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Login with ",
                            color = Color.Black
                            )
                        Icon(
                            modifier = Modifier.size(24.dp),
                            tint = Color.Black,
                            painter = painterResource(id = R.drawable.google_color),
                            contentDescription = "")
                    }
                }

            }
        }
    }



}
