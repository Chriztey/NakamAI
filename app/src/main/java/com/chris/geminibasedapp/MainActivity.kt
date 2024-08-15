package com.chris.geminibasedapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.chris.geminibasedapp.ui.screen.AIScreen
import com.chris.geminibasedapp.ui.screen.BakingScreen
import com.chris.geminibasedapp.ui.screen.HomeScreen
import com.chris.geminibasedapp.ui.screen.ImagePromptScreen
import com.chris.geminibasedapp.ui.screen.TextPromptScreen
import com.chris.geminibasedapp.ui.theme.GeminiBasedAppTheme
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            GeminiBasedAppTheme {
                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background,
//                ) {
//                    AIScreen()
//                }

                HomeScreen()

//                Scaffold {
//                    TextPromptScreen(
//                        modifier = Modifier.padding(it)
//                    )
//                }
            }
        }
    }
}