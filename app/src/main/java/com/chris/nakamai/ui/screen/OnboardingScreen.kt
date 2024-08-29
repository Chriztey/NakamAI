package com.chris.nakamai.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chris.nakamai.R
import com.chris.nakamai.common.listOnboardingComponent
import com.chris.nakamai.ui.viewmodel.SplashViewModel
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen() {
    val aiViewModel = hiltViewModel<SplashViewModel>()
    val status by aiViewModel.onboardingStatus.collectAsState()
    val scope = rememberCoroutineScope()

    var onboardingText by remember { mutableStateOf("") }


    val pagerState = rememberPagerState(pageCount = {
        4
    })

    Box {
        Log.d("Onboard", status.toString())
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            // Our page content

            val component = listOnboardingComponent.get(page)
            onboardingText = component.text
            OnboardingITem(
                image = component.image,
                text = component.text)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp),
            verticalArrangement = Arrangement.Bottom,
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {



                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        //.align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(16.dp)
                        )
                    }
                }

                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                ) {

                    if (pagerState.currentPage != 0) {
                        TextButton(onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }) {
                            Text(text = "Back")
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = {

                        if (pagerState.canScrollForward) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            aiViewModel.updateOnboardingStatus(true)
                            Log.d("Onboard", status.toString())
                        }

                    }) {
                        Text(text = "Next")
                    }
                }
            }


        }

    }



//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = status.toString())
//        Button(onClick = { aiViewModel.updateOnboardingStatus(!status) }) {
//            Text(text = "Click")
//        }
//    }

}


@Composable
fun OnboardingITem(
    image: Int,
    text: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = 32.dp,
                horizontal = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .size(250.dp)
                .padding(bottom = 48.dp),
            painter = painterResource(id = image), contentDescription = null)

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge,
            text = text)

    }

}