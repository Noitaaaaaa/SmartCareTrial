package com.example.smartcaretrial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Shows the profile of whoever is currently logged in — no id passed in,
 * no database query here. It just reads sessionViewModel.currentUser,
 * which Login populated when the user signed in.
 */
@Composable
fun Profile(
    navController: NavController,
    sessionViewModel: SessionViewModel
) {
    val user = sessionViewModel.currentUser

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "My Profile",
                style = MaterialTheme.typography.headlineSmall,
                
            )

            Button(
                onClick = { },
            ) {
                Text("Profile Picture of Doctor here")

            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        if (user == null) {
            // Shouldn't normally be reachable while logged out, but guard
            // instead of crashing if it ever is.
            Text("Not logged in.")
        } else {
            ProfileRow("Name", "${user.firstName} ${user.lastName}")
            ProfileRow("Email", user.email)
            ProfileRow("Role", user.role)
            if (user.role == "Doctor") {
                ProfileRow("Specialty", user.specialty)
            }
            ProfileRow("Gender", user.gender)
            ProfileRow("Birthdate", user.Birthdate)
            ProfileRow("Contact Number", user.contactNumber)
            ProfileRow("Emergency Number", user.emergencyNumber)
        }
    }
}

@Composable
private fun ProfileRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(
            text = value.ifBlank { "—" },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}