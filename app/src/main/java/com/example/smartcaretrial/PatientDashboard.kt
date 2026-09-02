package com.example.smartcaretrial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun PatientDashboard(
    navController: NavController,
    userId: Int,
    viewModel: PatientDashboardViewModel = viewModel()
) {
    val userInfo = viewModel.userInfo

    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        if (userInfo != null) {
            Text(fontSize = (28.sp), text = "Welcome ${userInfo.firstName}")
        } else {
            Text(text = "Loading......")
        }
    }
}