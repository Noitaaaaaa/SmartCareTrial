package com.example.smartcaretrial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PatientDashboard (navController: NavController, userId: Int) {

    var userInfo by remember { mutableStateOf<UserInfo?>(null) }
    val context = LocalContext.current

    LaunchedEffect(userId) {
        val db = DatabaseProvider.getDatabase(context)
        userInfo = db.userDao().getUserById(userId)
    }

    Column() {
        Spacer (modifier = Modifier.height(32.dp))
        if (userInfo != null){
        Text (text = "Welcome ${userInfo!!.firstName} ")

    } else{
        Text(text = " Loading......")
        }
}}