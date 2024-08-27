package com.chris.nakamai.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.chris.nakamai.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarApp(
    onNavigateToHome: () -> Unit,
    title: String = "NakamAI"
) {
    TopAppBar(
        title = {
            Text(text = title)
                },
        navigationIcon = {
            IconButton(onClick = { onNavigateToHome() }) {
                Icon(painter = painterResource(id = R.drawable.baseline_home_24), contentDescription = "Home")
            }
        },
        //colors = topAppBarColors(MaterialTheme.colorScheme.primary)
    )
}