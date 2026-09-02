package com.example.smartcaretrial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartcaretrial.ui.theme.DoctorBlue

@Composable
fun DoctorDirectory (navController: NavController, sessionViewModel: SessionViewModel) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text (
            text = "Doctor Directory",
            style = MaterialTheme.typography.headlineMedium
            )

        Spacer (modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors =  CardDefaults.cardColors(containerColor = DoctorBlue )
        ) {
        Text (text = ("Test") )
        }
    }
}