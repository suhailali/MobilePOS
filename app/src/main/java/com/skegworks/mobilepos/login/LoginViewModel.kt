package com.skegworks.mobilepos.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.skegworks.mobilepos.data.domain.UserRole
import com.skegworks.mobilepos.data.remote.firestore.FirestoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val firestoreHelper: FirestoreHelper) :
    ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.UpdatePassword -> _state.update {
                it.copy(
                    textStatePassword = intent.password
                )
            }

            is LoginIntent.UpdateUsername -> _state.update {
                it.copy(
                    textStateEmail = intent.username
                )
            }

            is LoginIntent.SubmitLogin -> {
                // Handle login submission logic here
                if (intent.isCreateUser) {
                    createUser(
                        _state.value.textStateEmail,
                        _state.value.textStatePassword
                    )
                } else {
                    login(
                        _state.value.textStateEmail,
                        _state.value.textStatePassword
                    )
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        // Implement login logic here
        _state.update {
            it.copy(isLoading = true, errorMessage = null)
        }
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid
                    Log.d("Auth", "User UID: $uid")
                    uid?.let {
                        getUserRole(uid)
                    }
                } else {
                    _state.update {
                        it.copy(
                            success = false,
                            isLoading = false,
                            errorMessage = "Failed to Login"
                        )
                    }
                    Log.e("Auth", "Sign-in failed: ${task.exception?.message}")
                }
            }
    }

    private fun createUser(email: String, password: String) {
        // Implement login logic here
        _state.update {
            it.copy(isLoading = true, errorMessage = null)
        }
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid
                    Log.d("Auth", "User UID: $uid")
                    _state.update {
                        it.copy(success = true, isLoading = false)
                    }
                } else {
                    Log.e("Auth", "Create user failed: ${task.exception?.message}")
                }
            }
    }

    private fun getUserRole(id: String) {
        firestoreHelper.getDocument("users", id, onSuccess = { document ->
            val role = document.getString("role")
            role?.let {
                _state.update {
                    it.copy(
                        userRole = UserRole.fromRole(role),
                        success = true,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }) {
            _state.update { state ->
                state.copy(
                    success = false,
                    isLoading = false,
                    errorMessage = "Failed to Login"
                )
            }
            Log.e("Auth", "Get user role failed: ${it.message}")
        }
    }
}