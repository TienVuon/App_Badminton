package com.example.app_badminton.firebase

import com.google.firebase.auth.FirebaseAuth
import android.app.Activity
import kotlinx.coroutines.tasks.await
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
import com.google.firebase.auth.GoogleAuthProvider

class FirebaseAuthManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // 🧩 Đăng ký tài khoản Email + Password
    suspend fun registerUser(email: String, password: String): String? {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            auth.currentUser?.uid
        } catch (e: Exception) {
            e.message
        }
    }

    // 🔑 Đăng nhập bằng Email + Password
    suspend fun loginUser(email: String, password: String): String? {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            auth.currentUser?.uid
        } catch (e: Exception) {
            e.message
        }
    }

    // 🔐 Cấu hình Google Sign-in
    fun getGoogleSignInClient(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(DEFAULT_SIGN_IN)
            // ✅ Web Client ID thật từ Firebase (không phải Android client)
            .requestIdToken("920142346709-128ha039032gvcfbednps1mjnh11gpkv.apps.googleusercontent.com")
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(activity, gso)
    }

    // ☁️ Xác thực tài khoản Google với Firebase
    suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount): String? {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).await()
            "success"
        } catch (e: Exception) {
            e.message
        }
    }

    // 🚪 Đăng xuất
    fun logout() {
        auth.signOut()
    }
}
