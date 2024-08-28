package com.chris.nakamai

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.chris.nakamai.ui.navigation.AppNavigationHost
import com.chris.nakamai.ui.screen.OnboardingScreen

import com.example.compose.GeminiBasedAppTheme
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        installSplashScreen()
        setContent {
            GeminiBasedAppTheme {
                //AppNavigationHost()
                OnboardingScreen()
//                Onboard()
            }
        }
    }
}