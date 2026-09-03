package com.example.smartcaretrial

import android.R.attr.fontFamily
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily.Companion.Monospace
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.R.attr.fontFamily
import android.R.attr.onClick
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController


@Composable
fun DoctorDashboard (navController: NavController) {
    Row(
    verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {


        Text ( fontFamily = Monospace ,
            text = "Hello Doctor, ", //add universal time to it so its not bland like a time analyzer for it to say Good Morning, Good Afternoon or Good evening
            fontSize = (24.sp),
            fontWeight = FontWeight.Medium,
            )
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        Button( onClick = { },
            ) {
            Text("Profile Picture of Doctor here")

        }

        Column() {}


    }
    }

