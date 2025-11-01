package com.skegworks.mobilepos.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.dataStore
import com.skegworks.mobilepos.MainActivity
import com.skegworks.mobilepos.ui.theme.MobilePOSTheme
import com.skegworks.mobilepos.utils.preferences.UserPreferences
import com.skegworks.mobilepos.utils.preferences.UserPreferencesSerializer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


private val Context.dataStore by dataStore(
    fileName = "user-preferences",
    serializer = UserPreferencesSerializer
)

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        val isCreateUser = bundle?.getBoolean("isCreateUser") ?: false
        enableEdgeToEdge()
        setContent {
            MobilePOSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: LoginViewModel by viewModels<LoginViewModel>()
                    val scope = rememberCoroutineScope()
                    val context = LocalContext.current
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel,
                        isCreateUser
                    ) { email, role ->
                        // Navigate to next screen on successful login
                        scope.launch {
                            dataStore.updateData {
                                println("UserRole $role")
                                UserPreferences(email, role)
                            }
                        }
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }

                }
            }
        }
    }
}