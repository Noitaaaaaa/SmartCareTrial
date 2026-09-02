package com.example.smartcaretrial

import android.app.Application
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    var userInfo by mutableStateOf(UserInfo())
        private set

    var registerError by mutableStateOf<String?>(null)
        private set

    var currentStep by mutableIntStateOf(1)
        private set

    // Doctors get a 4th step (Specialty), Patients only need 3
    val totalSteps: Int
        get() = if (userInfo.role == "Doctor") 4 else 3

    fun updateUserInfo(update: UserInfo) {
        userInfo = update
    }

    fun goBack() {
        if (currentStep > 1) currentStep--
    }

    /** Validates the current step, then either advances to the next step or submits. */
    fun nextOrSubmit(onSuccess: () -> Unit) {
        val error = validateStep(currentStep, userInfo)
        registerError = error
        if (error != null) return

        if (currentStep < totalSteps) {
            currentStep++
        } else {
            submit(onSuccess)
        }
    }

    private fun submit(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val db = DatabaseProvider.getDatabase(getApplication())
            val existingUser = db.userDao().getUserByEmail(userInfo.email)

            if (existingUser != null) {
                registerError = "An account with that email already exists"
            } else {
                registerError = null
                db.userDao().insertUser(userInfo)
                onSuccess()
            }
        }
    }

    // ---------- Per-step validation ----------
    // Returns an error message if the step is incomplete, or null if it's OK to move on.
    private fun validateStep(step: Int, userInfo: UserInfo): String? {
        return when (step) {
            1 -> when {
                userInfo.firstName.isBlank() -> "Please enter your first name"
                userInfo.lastName.isBlank() -> "Please enter your last name"
                userInfo.gender.isBlank() -> "Please select a gender"
                userInfo.Birthdate.isBlank() -> "Please select your birth date"
                else -> null
            }
            2 -> when {
                userInfo.contactNumber.isBlank() -> "Please enter a contact number"
                userInfo.email.isBlank() -> "Please enter an email"
                !Patterns.EMAIL_ADDRESS.matcher(userInfo.email).matches() -> "Please enter a valid email address"
                else -> null
            }
            3 -> when {
                userInfo.securityQuestion.isBlank() -> "Please select a security question"
                userInfo.securityAnswer.isBlank() -> "Please answer the security question"
                userInfo.password.isBlank() -> "Please enter a password"
                else -> null
            }
            4 -> when {
                userInfo.specialty.isBlank() -> "Please enter your specialty"
                else -> null
            }
            else -> null
        }
    }
}