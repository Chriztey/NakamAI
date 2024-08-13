package com.chris.geminibasedapp.data

import android.graphics.Bitmap
import com.chris.geminibasedapp.common.UiState
import com.chris.geminibasedapp.source.AIRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject

class AIRepoImplementation @Inject constructor(
    private val generativeModel: GenerativeModel,

): AIRepository {


    override suspend fun sendTextPrompt(
        prompt: String,
        callback: (UiState) -> Unit,
        result: (String) -> Unit,
        inquiry: (String) -> Unit
        ) {
        try {
            val response = generativeModel.generateContent(
                content {
                    text(prompt)
                }
            )
            inquiry(prompt)
            response.text?.let { outputContent ->
                callback(UiState.Success(outputContent))
                result(outputContent)
            }
        } catch (e: Exception) {
            callback(UiState.Error(e.localizedMessage ?: ""))
            e.localizedMessage?.let { result(it) }
        }
    }

    override suspend fun sentTextImagePrompt(
        bitmap: Bitmap,
        prompt: String,
        callback: (UiState) -> Unit,
        result: (String) -> Unit
        ) {

        try {
            val response = generativeModel.generateContent(
                content {
                    image(bitmap)
                    text(prompt)
                }
            )
            response.text?.let { outputContent ->
                callback(UiState.Success(outputContent))
                result(outputContent)
            }
        } catch (e: Exception) {
            callback(UiState.Error(e.localizedMessage ?: ""))
        }

    }
}