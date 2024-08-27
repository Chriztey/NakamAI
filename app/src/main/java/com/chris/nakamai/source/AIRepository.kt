package com.chris.nakamai.source

import android.graphics.Bitmap
import com.chris.nakamai.common.UiState

interface AIRepository {
    suspend fun sendTextPrompt(
        prompt: String,
        callback: (UiState) -> Unit,
        result: (String) -> Unit,
        //inquiry: (String) -> Unit
    )
    suspend fun sentTextImagePrompt(
        bitmap: Bitmap,
        prompt: String,
        callback: (UiState) -> Unit,
        result: (String) -> Unit,

    )
}