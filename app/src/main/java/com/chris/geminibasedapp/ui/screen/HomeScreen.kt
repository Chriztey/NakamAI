package com.chris.geminibasedapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun HomeScreen(
    navigateToSavedMultiModal: () -> Unit,
    navigateToSavedTextGen: () -> Unit
) {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Home", "Text Generation", "Multi Modal")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when (tabIndex) {

            0 -> DashboardScreen(
                navigateToSavedTextGen = { navigateToSavedTextGen() },
                navigateToSavedMultiModal = { navigateToSavedMultiModal() })
            1 -> TextPromptScreen()
            2 -> ImagePromptScreen()
        }
    }
}