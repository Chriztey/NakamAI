package com.chris.geminibasedapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chris.geminibasedapp.R

@Composable
fun DashboardScreen() {

    Scaffold { paddingValues ->

        Surface(modifier = Modifier
            .padding(paddingValues)) {

            Column(
                modifier = Modifier
                    .padding(vertical = 32.dp, horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    text = "Discover AI-powered creativity! " +
                            "Generate text from simple prompts or combine text and images for unique outputs.",
                    textAlign = TextAlign.Center
                    )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .paddingFromBaseline(bottom = 8.dp),
                            //.padding(bottom = 8.dp, end = 4.dp),
                        text = "Powered by",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Start
                    )


                    Image(
                        modifier = Modifier.size(64.dp),
                        contentScale = ContentScale.Fit,

                        painter = painterResource(
                            id = R.drawable.google_ai_gemini),
                        contentDescription = "gemini ai" )
                }
            }

        }

    }

}