package com.example.smartcaretrial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DoctorDashboard (navController: NavController) {
    Column() {

        Spacer (modifier = Modifier.height(32.dp))

        Text (text = "hello")
    }
}