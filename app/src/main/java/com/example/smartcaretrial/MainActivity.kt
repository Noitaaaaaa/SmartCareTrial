package com.example.smartcaretrial

import android.graphics.fonts.FontStyle
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartcaretrial.ui.theme.Cream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants

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
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Created once here, so it's scoped to the Activity rather than any
    // single screen — every composable below gets the SAME instance,
    // which is what makes "session" state actually work.
    val sessionViewModel: SessionViewModel = viewModel()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val isAuthRoute = currentRoute == "login" || currentRoute == "register" || currentRoute == "forgot"

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !isAuthRoute,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Cream),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.splash_iconhdpi),
                            contentDescription = "SmartCare Logo",
                            modifier = Modifier.size(90.dp)
                        )

                        Text(
                            text = "SmartCare",
                            fontFamily = FontFamily.Cursive,
                            fontSize = 30.sp
                        )
                    }
                }
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "My Profile", color = Color.Black) },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "My Profile",
                            tint = Color.Black
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navController.navigate("profile")
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Directory", color = Color.Black) },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person3,
                            contentDescription = "Directory",
                            tint = Color.Black
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        // Now correctly reads who's logged in from the session
                        // instead of the undefined `savedUser`. Left as a TODO
                        // navigate because "patientDirectory" / "doctorDirectory"
                        // don't have composable() destinations registered yet —
                        // add those to the NavHost below, then uncomment.
                        when (sessionViewModel.currentUser?.role) {
                            "Doctor" -> { /* TODO: navController.navigate("patientDirectory") */ }
                            "Patient" -> { navController.navigate("doctorDirectory")}
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Log-out", color = Color.Black) },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Log-out",
                            tint = Color.Black
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        sessionViewModel.logout()
                        navController.navigate("login") {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (!isAuthRoute) {
                    SmartCareTopBar(
                        currentRoute = currentRoute,
                        drawerState = drawerState,
                        coroutineScope = coroutineScope,
                        navController = navController,
                        userId = sessionViewModel.currentUser?.id ?: 0
                    )
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
                    Login(navController = navController, sessionViewModel = sessionViewModel)
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
                composable("forgot") {
                    Forgot(navController = navController)
                }
                composable("profile") {
                    Profile(navController = navController, sessionViewModel = sessionViewModel)
                }
                composable("doctorDirectory") {
                    DoctorDirectory(navController = navController, sessionViewModel = sessionViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartCareTopBar(
    currentRoute: String?,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
    navController: NavController,
    userId: Int
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0x80FFDEC48)
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open Drawer",
                    tint = Color.Black
                )
            }
        },
        title = {
            Text(
                text = "Smart Care",
                modifier = Modifier.clickable {
                    navController.navigate("patientDashboard/$userId")
                }
            )
        }
    )
}