package com.chris.geminibasedapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.chris.geminibasedapp.ui.screen.HomeScreen
import com.chris.geminibasedapp.ui.screen.SavedMultiModalChatScreen
import com.chris.geminibasedapp.ui.screen.SavedMultiModalItemScreen
import com.chris.geminibasedapp.ui.screen.SavedTextGenItemScreen
import com.chris.geminibasedapp.ui.screen.SavedTextGenerationChatScreen
import kotlinx.serialization.Serializable

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AppNavigationHost() {

    val navHost = rememberNavController()

    NavHost(
        navController = navHost,
        startDestination = HomeScreenRoute
    ) {

        composable<HomeScreenRoute> {
            HomeScreen(
                navigateToSavedMultiModal = { navHost.navigate(SavedMultiModalChatScreenRoute) },
                navigateToSavedTextGen = { navHost.navigate((SavedTextGenChatScreenRoute))}
            )
        }

        composable<SavedTextGenChatScreenRoute> {
            SavedTextGenerationChatScreen {  id, title ->
                navHost.navigate(SavedTextGenItemScreenRoute(
                    documentId = id,
                    title = title)
                )
            }
        }
        
        composable<SavedTextGenItemScreenRoute> {
            val args = it.toRoute<SavedTextGenItemScreenRoute>()
            SavedTextGenItemScreen(
                documentId = args.documentId,
                title = args.title
            )
        }

        composable<SavedMultiModalChatScreenRoute> {
            SavedMultiModalChatScreen {  id, title ->
                navHost.navigate( SavedMultiModalItemScreenRoute (
                    documentId = id,
                    title = title
                )
                )
            }
        }

        composable<SavedMultiModalItemScreenRoute> {
            val args = it.toRoute<SavedMultiModalItemScreenRoute>()
            SavedMultiModalItemScreen(
                documentId = args.documentId,
                title = args.title
            )
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
