package com.example.smartcaretrial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun Forgot(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    val email = viewModel.email
    val securityAnswer = viewModel.securityAnswer
    val newPassword = viewModel.newPassword
    val confirmPassword = viewModel.confirmPassword
    val securityQuestion = viewModel.securityQuestion
    val errorMessage = viewModel.errorMessage
    val isVerified = viewModel.isVerified

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Forgot Password", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Step 1: find account by email
        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email") },
            singleLine = true,
            enabled = !isVerified,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (!isVerified) {
            Button(onClick = { viewModel.findAccount() }) {
                Text("Find Account")
            }
        }

        // Step 2: answer security question
        securityQuestion?.let { question ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = question, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = securityAnswer,
                onValueChange = { viewModel.onSecurityAnswerChange(it) },
                label = { Text("Security Answer") },
                singleLine = true,
                enabled = !isVerified,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (!isVerified) {
                Button(onClick = { viewModel.verifyAnswer() }) {
                    Text("Verify Answer")
                }
            }
        }

        // Step 3: reset password
        if (isVerified) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { viewModel.onNewPasswordChange(it) },
                label = { Text("New Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChange(it) },
                label = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                viewModel.resetPassword {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }) {
                Text("Reset Password")
            }
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}