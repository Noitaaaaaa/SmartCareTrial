package com.example.smartcaretrial

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(application: Application) : AndroidViewModel(application) {

    var email by mutableStateOf("")
        private set
    var securityAnswer by mutableStateOf("")
        private set
    var newPassword by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set

    var securityQuestion by mutableStateOf<String?>(null)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var isVerified by mutableStateOf(false)
        private set

    fun onEmailChange(value: String) { email = value }
    fun onSecurityAnswerChange(value: String) { securityAnswer = value }
    fun onNewPasswordChange(value: String) { newPassword = value }
    fun onConfirmPasswordChange(value: String) { confirmPassword = value }

    // Step 1: find account by email
    fun findAccount() {
        viewModelScope.launch {
            val db = DatabaseProvider.getDatabase(getApplication())
            val user = db.userDao().getUserByEmail(email)

            if (user == null) {
                errorMessage = "No account found with that email"
                securityQuestion = null
            } else {
                securityQuestion = user.securityQuestion
                errorMessage = null
            }
        }
    }

    // Step 2: answer security question
    fun verifyAnswer() {
        viewModelScope.launch {
            val db = DatabaseProvider.getDatabase(getApplication())
            val user = db.userDao().getUserByEmail(email)

            if (user != null && user.securityAnswer.equals(securityAnswer, ignoreCase = true)) {
                isVerified = true
                errorMessage = null
            } else {
                errorMessage = "Incorrect answer"
            }
        }
    }

    // Step 3: reset password
    fun resetPassword(onSuccess: () -> Unit) {
        if (newPassword.isEmpty()) {
            errorMessage = "Password cannot be empty"
            return
        }
        if (newPassword != confirmPassword) {
            errorMessage = "Passwords do not match"
            return
        }

        viewModelScope.launch {
            val db = DatabaseProvider.getDatabase(getApplication())
            val user = db.userDao().getUserByEmail(email)
            if (user != null) {
                db.userDao().updateUser(user.copy(password = newPassword))
                onSuccess()
            }
        }
    }
}