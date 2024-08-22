package com.chris.geminibasedapp.common

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.chris.geminibasedapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarApp(
    onNavigateToHome: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "NakamAI")
                },
        navigationIcon = {
            IconButton(onClick = { onNavigateToHome() }) {
                Icon(painter = painterResource(id = R.drawable.baseline_home_24), contentDescription = "Home")
            }
        },
        //colors = topAppBarColors(MaterialTheme.colorScheme.primary)
    )
}