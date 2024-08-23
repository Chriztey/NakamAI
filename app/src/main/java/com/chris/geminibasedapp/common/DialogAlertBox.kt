package com.chris.geminibasedapp.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.chris.geminibasedapp.R

@Composable
fun ClearChatDialogConfirmation(
    clearChat: () -> Unit,
    cancelAction: () -> Unit
) {

    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(
                    id = R.drawable.baseline_delete_24),
                contentDescription = "Clear Chat",
                tint = Color.Red
            )
        },
        title = {
            Text(text = "Clear Chat")
        },
        text = {
            Text(text = "Are you sure you want to clear all the chat?")
        },
        onDismissRequest = { cancelAction() },
        confirmButton = {
            Button(onClick = {
                clearChat()
                cancelAction()

            }) {
                Text(text = "Clear Chat")
            }

        })

}

@Composable
fun SaveChatConfirmationDialog(
    confirm: () -> Unit,
    dismiss: () -> Unit
) {

    AlertDialog(
        icon = {
            Icon(painter = painterResource(
                id = R.drawable.baseline_save_24),
                contentDescription = "save chat")
        },
        title = {
            Text(text = "Save Chat")
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Do you want to save this chat ?")
        },
        onDismissRequest = {
            dismiss() },
        confirmButton = {
            TextButton(onClick = { confirm()  }) {
                Text(text = "YES")
            }
        })
}

@Composable
fun DeleteSavedChatDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = "Delete Selected Chat")
        },
        text = {
            Text(text = "Do you want to delete selected chat ?")
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            IconButton(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Icon(painter = painterResource(id = R.drawable.baseline_delete_24), contentDescription = "Delete")
            }
        }
    )
}