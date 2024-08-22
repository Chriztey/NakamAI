package com.chris.geminibasedapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.geminibasedapp.common.AuthState
import com.chris.geminibasedapp.ui.viewmodel.AuthViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun HomeScreen(
    navigateToSavedMultiModal: () -> Unit,
    navigateToSavedTextGen: () -> Unit,
    navigateToLogin: () -> Unit,
    clearBackStack: () -> Unit
) {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Home", "Text Generation", "Multi Modal")
    val authViewModel = hiltViewModel<AuthViewModel>()
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(key1 = authState) {
        when (authState) {
            AuthState.Unauthenticated -> navigateToLogin()
            else -> Unit
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    ) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when (tabIndex) {

            0 -> DashboardScreen(
                navigateToSavedTextGen = { navigateToSavedTextGen() },
                navigateToSavedMultiModal = { navigateToSavedMultiModal() },
                clearBackStack = { clearBackStack() },
                navigateToLogin = { navigateToLogin() }
                )
            1 -> TextPromptScreen()
            2 -> ImagePromptScreen()
        }
    }
}