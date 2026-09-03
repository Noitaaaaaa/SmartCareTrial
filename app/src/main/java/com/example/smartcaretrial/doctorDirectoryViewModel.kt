package com.example.smartcaretrial

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DoctorDirectoryViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = DatabaseProvider.getDatabase(application).userDao()

    val doctors: StateFlow<List<doctorDirectory>> = userDao.getDoctorDirectory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}