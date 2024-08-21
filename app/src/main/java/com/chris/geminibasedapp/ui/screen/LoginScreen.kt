package com.chris.geminibasedapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.geminibasedapp.R
import com.chris.geminibasedapp.common.AuthState
import com.chris.geminibasedapp.ui.viewmodel.AuthViewModel

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
                Button(onClick = {
                    authViewModel.signInWithGoogle(
                        credentialManager,
                        context)
                    }
                ) {
                    Row {
                        Text(text = "Login with ")
                        Icon(painter = painterResource(id = R.drawable.google_color), contentDescription = "")
                    }
                }

            }
        }
    }



}
