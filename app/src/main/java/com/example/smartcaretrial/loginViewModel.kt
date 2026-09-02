package com.example.smartcaretrial

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var userInfo by mutableStateOf(UserInfo())
        private set

    var loginError by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(value: String) {
        userInfo = userInfo.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        userInfo = userInfo.copy(password = value)
    }

    /**
     * Attempts to log in. On success, calls [onSuccess] with the full
     * [UserInfo] row from the database — the caller decides what to do with
     * it (store it in the session, pick a route, etc).
     */
    fun login(onSuccess: (user: UserInfo) -> Unit) {
        viewModelScope.launch {
            val db = DatabaseProvider.getDatabase(getApplication())
            val savedUser = db.userDao().getUserByEmail(userInfo.email)

            when {
                savedUser == null -> loginError = "No account found with that email"
                savedUser.password != userInfo.password -> loginError = "Incorrect password"
                else -> {
                    loginError = null
                    onSuccess(savedUser)
                }
            }
        }
    }
}