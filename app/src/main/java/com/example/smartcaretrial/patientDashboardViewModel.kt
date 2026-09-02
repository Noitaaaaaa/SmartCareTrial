package com.example.smartcaretrial

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PatientDashboardViewModel(application: Application) : AndroidViewModel(application) {

    var userInfo by mutableStateOf<UserInfo?>(null)
        private set

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            val db = DatabaseProvider.getDatabase(getApplication())
            userInfo = db.userDao().getUserById(userId)
        }
    }
}