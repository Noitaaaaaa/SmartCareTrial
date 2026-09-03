package com.example.smartcaretrial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.smartcaretrial.DoctorDirectoryViewModel
import com.example.smartcaretrial.doctorDirectory

@Composable
fun DoctorDirectoryScreen(viewModel: DoctorDirectoryViewModel, navController: NavController, sessionViewModel: SessionViewModel) {
    val doctors by viewModel.doctors.collectAsState()

    val user = sessionViewModel.currentUser

    LazyColumn {
        items(doctors, key = { it.id }) { doc ->
            ListItem(
                headlineContent = { Card(
                    modifier = Modifier.fillMaxWidth()
                )  {
                    Column() {
                        Text("Dr. ${doc.firstName} ${doc.lastName}")
                        Text("${doc.specialty}")
                    }
                } }
            )
        }
    }
}