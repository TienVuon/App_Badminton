package com.example.app_badminton

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.example.app_badminton.firebase.FirebaseAuthManager
import com.example.app_badminton.ui.theme.AppBabmintonTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    companion object {
        const val GOOGLE_SIGN_IN_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppBabmintonTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavGraph() // ✅ Root navigation
                }
            }
        }
    }

    /**
     * ✅ Xử lý kết quả khi người dùng chọn tài khoản Google
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.result
                if (account != null) {
                    lifecycleScope.launch {
                        val result = FirebaseAuthManager().firebaseAuthWithGoogle(account)
                        if (result == "success") {
                            Toast.makeText(
                                this@MainActivity,
                                "🎉 Đăng nhập Google thành công!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "⚠️ Lỗi đăng nhập: $result",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Không lấy được tài khoản Google", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Đăng nhập thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
