package com.example.iverify.devices.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.iverify.devices.ui.composable.home.HomeComposable
import com.example.iverify.devices.ui.composable.routes.Route
import com.example.iverify.devices.ui.theme.IVerifyDevicesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Setup the View Model
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IVerifyDevicesTheme {
                viewModel.setupFirstTimeToken()
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Route.HomeRoute.route
                ) {
                    composable(route = Route.HomeRoute.route) {
                        HomeComposable(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}