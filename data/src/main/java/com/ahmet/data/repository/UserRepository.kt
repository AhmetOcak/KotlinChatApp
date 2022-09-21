package com.ahmet.data.repository

import android.net.Uri
import android.util.Log
import com.ahmet.data.utils.Firebase
import com.ahmet.data.utils.UserKeys
import com.ahmet.domain.interfaces.IUserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class UserRepository @Inject constructor() : IUserRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun getCurrentUserEmail(): String? = auth.currentUser?.email

    override suspend fun deleteUser(): String? {
        var result: String? = null

        auth.currentUser!!.delete().addOnSuccessListener {
            result = "Successful"
        }.await()

        return result
    }

    override suspend fun deleteUserDoc(email: String): Boolean {
        var status = false

        db.collection(Firebase.USER_COLLECTION_PATH).document(email).delete().addOnSuccessListener {
            status = true
        }.await()

        return status
    }

    override suspend fun uploadImage(filePath: Uri): String? {
        var resultMessage: String? = null
        val imagesRef = FirebaseStorage.getInstance().reference.child(("${auth.currentUser?.email}/${auth.currentUser?.email}.png"))

        imagesRef.putFile(filePath).addOnCompleteListener {
            resultMessage = if (it.isSuccessful) {
                "Successful"
            } else {
                it.exception?.message
            }
        }.await()

        return resultMessage
    }

    override suspend fun getUserImage(email: String): String? {
        val imagesRef = FirebaseStorage.getInstance().reference.child(("${email}/${email}.png"))
        val localFile = File.createTempFile("userImage", "png")

        imagesRef.getFile(localFile).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("result", "successful")
            } else {
                Log.e("result", "fail")
            }
        }.await()

        return localFile.path
    }

    override suspend fun updateUserName(email: String, newUserName: String) {
        val userRef = db.collection(Firebase.USER_COLLECTION_PATH).document(email)

        userRef.update(UserKeys.USERNAME, newUserName).addOnCompleteListener {
            if(it.isSuccessful) {
                Log.e("update user name", "successful")
            }else {
                Log.e("update user name", "unsuccessful")
            }
        }.await()
    }

}

