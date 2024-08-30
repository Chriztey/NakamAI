package com.chris.nakamai.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.chris.nakamai.ui.screen.HomeScreen
import com.chris.nakamai.ui.screen.LoginScreen
import com.chris.nakamai.ui.screen.OnboardingScreen
import com.chris.nakamai.ui.screen.SavedMultiModalChatScreen
import com.chris.nakamai.ui.screen.SavedMultiModalItemScreen
import com.chris.nakamai.ui.screen.SavedTextGenItemScreen
import com.chris.nakamai.ui.screen.SavedTextGenerationChatScreen
import com.chris.nakamai.ui.viewmodel.SplashViewModel
import kotlinx.serialization.Serializable

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AppNavigationHost() {

    val navHost = rememberNavController()

    val aiViewModel = hiltViewModel<SplashViewModel>()
    val status by aiViewModel.onboardingStatus.collectAsState()

    NavHost(
        navController = navHost,
        startDestination = if (status) {LoginScreenRoute} else OnboardingRoute
    ) {

        composable<LoginScreenRoute> {
            LoginScreen (
                navigateToHome = {
                    navHost.navigate(HomeScreenRoute)
                }
            )
        }

        composable<HomeScreenRoute> {
            HomeScreen(
                navigateToSavedMultiModal = { navHost.navigate(SavedMultiModalChatScreenRoute) },
                navigateToSavedTextGen = { navHost.navigate((SavedTextGenChatScreenRoute))},
                navigateToLogin = {navHost.navigate(LoginScreenRoute)},
                clearBackStack = {navHost.popBackStack(LoginScreenRoute, true)}
            )
        }

        composable<SavedTextGenChatScreenRoute> {
            SavedTextGenerationChatScreen(
                onItemClick = {
                              id, title ->
                    navHost.navigate(SavedTextGenItemScreenRoute(
                        documentId = id,
                        title = title)
                    )},
                onNavigateToHome = {navHost.navigate(HomeScreenRoute)}
                )
        }
        
        composable<SavedTextGenItemScreenRoute> {
            val args = it.toRoute<SavedTextGenItemScreenRoute>()
            SavedTextGenItemScreen(
                documentId = args.documentId,
                title = args.title
            )
        }

        composable<SavedMultiModalChatScreenRoute> {
            SavedMultiModalChatScreen(
                onItemClick = {  id, title ->
                    navHost.navigate( SavedMultiModalItemScreenRoute (
                        documentId = id,
                        title = title)
                    )
                              },
                onNavigateToHome = {navHost.navigate(HomeScreenRoute)}
            )
        }

        composable<SavedMultiModalItemScreenRoute> {
            val args = it.toRoute<SavedMultiModalItemScreenRoute>()
            SavedMultiModalItemScreen(
                documentId = args.documentId,
                title = args.title
            )
        }

        composable<OnboardingRoute> {
            OnboardingScreen()
        }

    }

}

@Serializable
object HomeScreenRoute
@Serializable
object SavedTextGenChatScreenRoute
@Serializable
data class SavedTextGenItemScreenRoute(
    val documentId: String,
    val title: String
)
@Serializable
object SavedMultiModalChatScreenRoute
@Serializable
data class SavedMultiModalItemScreenRoute(
    val documentId: String,
    val title: String
)

@Serializable
object LoginScreenRoute
@Serializable
object OnboardingRoute
