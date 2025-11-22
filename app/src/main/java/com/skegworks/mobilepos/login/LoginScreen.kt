package com.skegworks.mobilepos.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.skegworks.mobilepos.data.domain.UserRole
import com.skegworks.mobilepos.ui.component.SimpleTextField
import com.skegworks.mobilepos.utils.Dimens

@Composable
fun LoginScreen(
    modifier: Modifier,
    viewModel: LoginViewModel,
    isCreateUser: Boolean,
    loginSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(Dimens.LARGE_PADDING.dp))
        Spacer(modifier = Modifier.padding(Dimens.LARGE_PADDING.dp))
        Spacer(modifier = Modifier.padding(Dimens.LARGE_PADDING.dp))
        SimpleTextField(textState = state.textStateEmail, "Email") {
            viewModel.handleIntent(LoginIntent.UpdateUsername(it))
        }
        SimpleTextField(textState = state.textStatePassword, "Password") {
            viewModel.handleIntent(LoginIntent.UpdatePassword(it))
        }

        Spacer(modifier = Modifier.padding(Dimens.LARGE_PADDING.dp))

        Button(onClick = {
            viewModel.handleIntent(LoginIntent.SubmitLogin(isCreateUser))
        }) {
            Text(if (isCreateUser) "Create User" else "Login")
        }

        if (state.isLoading) {
            Text("Logging in...")
        }

        if (state.success) {
            viewModel.initialiseAppSettings()
            loginSuccess()
        }

        state.errorMessage?.let {
            Spacer(modifier = Modifier.padding(Dimens.MEDIUM_PADDING.dp))
            Text(it, color = Color.Red)
        }
    }

}