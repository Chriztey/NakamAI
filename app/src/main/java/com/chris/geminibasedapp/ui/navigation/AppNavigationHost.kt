package com.chris.geminibasedapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.chris.geminibasedapp.ui.screen.SavedTextGenItemScreen
import com.chris.geminibasedapp.ui.screen.SavedTextGenerationChatScreen
import kotlinx.serialization.Serializable

@Composable
fun AppNavigationHost() {

    val navHost = rememberNavController()

    NavHost(
        navController = navHost,
        startDestination = SavedTextGenChatScreenRoute ) {

        composable<SavedTextGenChatScreenRoute> {
            SavedTextGenerationChatScreen {  id, title ->
                navHost.navigate(SavedTextGenItemScreenRoute(
                    documentId = id,
                    title = title
                ))

                //navHost.navigate(SavedTextGenItemScreenRoute((title, id)))

            }
        }
        
        composable<SavedTextGenItemScreenRoute> {
            val args = it.toRoute<SavedTextGenItemScreenRoute>()
            SavedTextGenItemScreen(
                documentId = args.documentId,
                title = args.title
                )
        }

    }

}

@Serializable
object DashboardRoute
@Serializable
object SavedTextGenChatScreenRoute
@Serializable
data class SavedTextGenItemScreenRoute(
    val documentId: String,
    val title: String
)
