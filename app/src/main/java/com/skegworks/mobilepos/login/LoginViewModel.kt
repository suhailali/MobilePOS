package com.skegworks.mobilepos.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.skegworks.mobilepos.appsettings.InitialiseAppSettingsUseCase
import com.skegworks.mobilepos.data.domain.UserRole
import com.skegworks.mobilepos.data.remote.firestore.FirestoreHelper
import com.skegworks.mobilepos.sync.LoadAllDataFromFireStoreUseCase
import com.skegworks.mobilepos.utils.preferences.UserPreferenceHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firestoreHelper: FirestoreHelper,
    private val userPreferenceHandler: UserPreferenceHandler,
    private val initialiseAppSettingsUseCase: InitialiseAppSettingsUseCase,
    private val loadAllDataUseCase: LoadAllDataFromFireStoreUseCase
) :
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

    private fun loadAllData() {
        _state.update {
            it.copy(
                loadingMessage = "Downloading Data. Please Wait..."
            )
        }
        loadAllDataUseCase.invoke {
            _state.update {
                it.copy(
                    success = true,
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

    private fun login(email: String, password: String) {
        // Implement login logic here
        _state.update {
            it.copy(isLoading = true, errorMessage = null, loadingMessage = "Logging In...")
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
                UserRole.fromRole(role)?.let { userRole ->
                    saveUser(id, userRole)
                    _state.update {
                        it.copy(
                            userRole = userRole,
                            errorMessage = null
                        )
                    }
                    // loadAllData()

                    _state.update {
                        it.copy(
                            success = true,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
            }
        }) {
            _state.update { state ->
                state.copy(
                    success = false,
                    isLoading = false,
                    errorMessage = "Failed to Fetch User"
                )
            }
            Log.e("Auth", "Get user role failed: ${it.message}")
        }
    }

    private fun saveUser(email: String, role: UserRole) {
        viewModelScope.launch {
            userPreferenceHandler.saveUser(_state.value.textStateEmail, role)
        }
    }

    fun initialiseAppSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            initialiseAppSettingsUseCase.invoke()
        }
    }
}