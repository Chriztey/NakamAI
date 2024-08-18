package com.chris.geminibasedapp.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.chris.geminibasedapp.common.AuthState
import com.chris.geminibasedapp.common.ChatLine
import com.chris.geminibasedapp.common.SavedChat
import com.chris.geminibasedapp.common.UiState
import com.chris.geminibasedapp.source.FirestoreDBRepository
import com.chris.geminibasedapp.utils.Constants.CHAT
import com.chris.geminibasedapp.utils.Constants.IS_USER
import com.chris.geminibasedapp.utils.Constants.SAVED_TEXTGENERATION
import com.chris.geminibasedapp.utils.Constants.TITLE
import com.chris.geminibasedapp.utils.Constants.USER_PATH_FIRESTORE
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirestoreDBRepoImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
): FirestoreDBRepository {


    override fun saveChat(
        title: String,
        textGenerationChat: HashMap<String,Any>,
        user: FirebaseUser,
        callback: (UiState) -> Unit,
        collection: String
    ) {


        firestore.collection(USER_PATH_FIRESTORE)
            .document(user.email!!)
            .collection(collection)
            .document(title)
            .set(textGenerationChat)
            .addOnSuccessListener {
                Log.d("Success", "DocumentSnapshot successfully written!")
                callback(UiState.Success("Success"))
            }

            .addOnFailureListener { e ->
                Log.e("Firestore Error", "Error writing document", e)
                callback(UiState.Error(e.message ?: "Something Went Wrong"))
            }
        Log.d("User", "End")
    }

    override fun fetchSavedTextGenerationChat(
        user: FirebaseUser,
        callback: (UiState) -> Unit,
        result: (List<SavedChat>) -> Unit
    ) {
        val savedList : MutableList<SavedChat> = mutableListOf()

        firestore.collection(USER_PATH_FIRESTORE)
            .document(user.email!!)
            .collection(SAVED_TEXTGENERATION)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    Log.d("Read Success", document.id)
                    val chatId = document.id
                    val chatTitle = document.get(TITLE) as String
                    Log.d("Read Success", chatTitle)
                    savedList.add(
                        SavedChat(
                            id = chatId,
                            title = chatTitle
                        )
                    )
                }

                result(savedList)
                callback(UiState.Success("Success"))
                Log.d("Read Success", savedList.toString())



            }
            .addOnFailureListener { e ->
                callback(UiState.Error(e.message.toString()))
                Log.e("Firestore Error", "Error reading document", e)
            }




    }

    override fun fetchIndividualSavedTextGenerationChat(
        user: FirebaseUser,
        id: String,
        callback: (UiState) -> Unit,
        result: (List<ChatLine>) -> Unit
    ) {

        val savedChatList : MutableList<ChatLine> = mutableListOf()

        firestore.collection(USER_PATH_FIRESTORE)
            .document(user.email!!)
            .collection(SAVED_TEXTGENERATION)
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    val chatData = document.get(CHAT) as List<HashMap<*,*>>

                    for (chatLine in chatData) {
                        val temp = ChatLine(
                            chat = chatLine[CHAT] as String,
                            isUser = chatLine[IS_USER] as Boolean
                        )

//                        val chat = chatLine[CHAT] as String
//                        val isUser = chatLine[IS_USER] as Boolean

                        Log.d("Read Success", "${temp.isUser} = ${temp.chat}")
                        savedChatList.add(temp)
                    }

                    result(savedChatList)

                    callback(UiState.Success("Success"))

                } else {
                    Log.d("Read Success", "No documents found or QuerySnapshot is null")
                    callback(UiState.Success("No Found"))
                }
            }
            .addOnFailureListener { e ->
                e.message?.let { UiState.Error(it) }?.let { callback(it) }
                Log.e("Firestore Error", "Error reading document", e)
                callback(UiState.Error(e.message.toString()))
            }

    }

    override fun deleteSavedTextGenerationChat() {
        TODO("Not yet implemented")
    }

}