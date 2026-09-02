package com.example.smartcaretrial

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun Login(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    val userInfo = viewModel.userInfo
    val loginError = viewModel.loginError

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Login", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userInfo.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            isError = userInfo.email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(userInfo.email)
                .matches(),
            supportingText = {
                if (userInfo.email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(userInfo.email)
                        .matches()
                ) {
                    Text("Please enter a valid email address")
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = userInfo.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Password") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        // Show a login error if there is one
        if (loginError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = loginError,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                viewModel.login { route ->
                    navController.navigate(route)
                }
            }) {
                Text("Login")
            }

            TextButton(onClick = {
                navController.navigate("register")
            }) {
                Text("Register here")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { navController.navigate("forgot") }) {
                Text("Forgot Password?")
            }
        }
    }
}