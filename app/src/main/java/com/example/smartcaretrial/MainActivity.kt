package com.example.smartcaretrial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartcaretrial.ui.theme.SmartCareTrialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                MainActivityNavigation()
        }
    }
}

@Composable
fun MainActivityNavigation() {
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        topBar = {
            if (currentRoute != "login" && currentRoute != "register" && currentRoute != "forgot") {
                SmartCareTopBar(navController = navController, currentRoute = currentRoute)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("doctorDashboard") {
                DoctorDashboard(navController = navController)
            }
            composable("login") {
                Login(navController = navController)
            }
            composable("register") {
                Register(navController = navController)
            }
            composable(
                "patientDashboard/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.IntType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId") ?: 0
                PatientDashboard(navController = navController, userId = userId)
            }
            composable ("forgot") {
                Forgot(navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartCareTopBar(
    navController: NavHostController,
    currentRoute: String?
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0x80FFFDD0)
        ),
        title = {

            Text(
                text = when {
                    currentRoute == "doctorDashboard" -> "Doctor Dashboard"
                    currentRoute?.startsWith("patientDashboard") == true -> "Patient Portal"
                    else -> "Smart Care"
                }
            )
        },
        actions = {
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Open Navigation Menu",
                        tint = Color.Black
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {

                    DropdownMenuItem(
                        text = { Text("Doctor Dashboard") },
                        leadingIcon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                        onClick = {
                            expanded = false
                            navController.navigate("doctorDashboard")
                        }
                    )


                    DropdownMenuItem(
                        text = { Text("Patient Dashboard") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        onClick = {
                            expanded = false
                            navController.navigate("patientDashboard/101")
                        }
                    )


                    DropdownMenuItem(
                        text = { Text("Log Out") },
                        leadingIcon = { Icon(Icons.Default.Logout, contentDescription = null) },
                        onClick = {
                            expanded = false

                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        }//yes
                    )
                }
            }
        }
    )
}