package com.example.smartcaretrial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartcaretrial.ui.theme.SmartCareTrialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCareTrialTheme {
                   MainActivityNavigation()
                }
            }
        }
    }

@Composable
fun MainActivityNavigation () {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable ( "doctorDashboard" ) {
            DoctorDashboard(navController = navController)
        }
        composable ("login") {
            Login(navController = navController)
        }
        composable ("register") {
            Register(navController = navController)
        }
        composable(
            "patientDashboard/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            PatientDashboard(navController = navController, userId = userId)
        }
    }

}

