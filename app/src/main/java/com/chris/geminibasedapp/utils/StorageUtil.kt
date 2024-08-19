package com.chris.geminibasedapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
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

    }

}