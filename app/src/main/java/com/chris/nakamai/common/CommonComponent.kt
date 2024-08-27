package com.chris.nakamai.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chris.nakamai.R

@Composable
fun ChatBubble(
    modifier: Modifier = Modifier,
    chat: String,
    isUser: Boolean
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = modifier
                .clip(
                    if (isUser) {
                        RoundedCornerShape(16.dp)
                    } else {
                        RoundedCornerShape(0.dp)
                    }
                )
                .background(
                    if (isUser) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.background
                )
                .fillMaxWidth(0.6f)
                .wrapContentSize()
                .align(
                    if (isUser)
                        Alignment.End else Alignment.Start
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            SelectionContainer {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    //.align(Alignment.CenterStart),
                    textAlign = if (isUser) TextAlign.End else TextAlign.Start,
                    text = chat
                )
            }



        }
    }

}



@Composable
fun ChatTitleTextField(
    modifier: Modifier = Modifier,
    chatTitle: String,
    onValueChangeTitle: (String) -> Unit,
    saveChat: () -> Unit,
    onDismiss: () -> Unit
) {


    Box(
        modifier = modifier
            .height(300.dp)
            //.wrapContentSize()
            .padding(32.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
                //color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
                text = "CHAT TITLE")
            OutlinedTextField(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                    //.padding(horizontal = 16.dp),
                value = chatTitle,
                onValueChange = { onValueChangeTitle(it) })

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                IconButton(onClick = {
                    saveChat()
                    onDismiss()
                }) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.baseline_save_24
                        ),
                        contentDescription = "save",
                        //tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                IconButton(onClick = { onDismiss() }) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.baseline_cancel_24
                        ),
                        contentDescription = "cancel",
                        //tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }

}

@Composable
fun EmptyList() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Empty List")
    }
}

@Composable
fun ListCard(
    title: String,
    itemOnClick: () -> Unit,
    itemDelete: () -> Unit = {}
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { itemOnClick() }
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = title.uppercase()
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { itemDelete() }) {
                Icon(painter = painterResource(id = R.drawable.baseline_delete_24), contentDescription = "delete")
            }

        }
    }
}