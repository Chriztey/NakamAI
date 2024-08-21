package com.chris.geminibasedapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.chris.geminibasedapp.common.UiState
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class StorageUtil {

    companion object {


        private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            return stream.toByteArray()
        }

        fun uploadToStorage(
            bitmap: Bitmap,
            context: Context,
            imageId: String
            ) {

            val storage = Firebase.storage

            var storageRef = storage.reference
            var spaceRef: StorageReference


            spaceRef = storageRef.child("images/$imageId.jpg")


            val byteArray: ByteArray? = bitmapToByteArray(bitmap)


            byteArray?.let {
                var uploadTask = spaceRef.putBytes(byteArray)
                uploadTask.addOnFailureListener{
                    Toast.makeText(
                        context,
                        "Upload Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnSuccessListener { taskSnapshot ->
                    Toast.makeText(
                        context,
                        "Upload Success",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        suspend fun downloadImage(imageId: String): Uri? {
            val storage = Firebase.storage
            val storageRef = storage.reference.child("images/$imageId.jpg")

            return try {
                storageRef.downloadUrl.await() // Await the result asynchronously
            } catch (e: Exception) {
                Log.e("DownloadImage", "Error downloading image: ${e.message}", e)
                null
            }
        }

        fun deleteImageFromStorage(imageId: String, callback: (UiState) -> Unit) {
            // Get a reference to the image in Firebase Storage
            val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageId.jpg")

            // Delete the image
            storageRef.delete()
                .addOnSuccessListener {
                    // File deleted successfully
                    callback(UiState.Success("Deleted"))
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                    exception.printStackTrace()
                    callback(UiState.Error(exception.message.toString()))
                }
        }

    }

}