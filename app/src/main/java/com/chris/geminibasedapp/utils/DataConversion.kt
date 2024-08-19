package com.chris.geminibasedapp.utils

import android.graphics.Bitmap
import com.chris.geminibasedapp.common.ChatLine
import com.chris.geminibasedapp.common.ImageChatLine
import com.chris.geminibasedapp.common.ImageChatLineInStorage
import com.chris.geminibasedapp.utils.Constants.CHAT
import com.chris.geminibasedapp.utils.Constants.IMAGE
import com.chris.geminibasedapp.utils.Constants.IS_USER
import com.chris.geminibasedapp.utils.Constants.TITLE
import java.io.ByteArrayOutputStream

object DataConversion {

    fun textGenerationData(
        title: String,
        chatList: List<ChatLine>
    ): HashMap<String,Any> {
        val chatListMap = chatList.map { chat ->
            mapOf(
                CHAT to chat.chat,
                IS_USER to chat.isUser
            )
        }

        val chatData = hashMapOf(
            TITLE to title,
            CHAT to chatListMap
        )

        return chatData
    }




    fun multiModalData(
        title: String,
        chatList: List<ImageChatLineInStorage>
    ): HashMap<String,Any> {
        val chatListMap = chatList.map { chat ->
            mapOf(
                CHAT to chat.chat,
                IS_USER to chat.isUser,
                IMAGE to chat.imageId
            )
        }

        val chatData = hashMapOf(
            TITLE to title,
            CHAT to chatListMap
        )

        return chatData
    }

}