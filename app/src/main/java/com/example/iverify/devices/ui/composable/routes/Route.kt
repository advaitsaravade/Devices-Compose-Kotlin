package com.example.iverify.devices.ui.composable.routes

sealed class Route(val route: String) {
    object HomeRoute: Route("homeScreen")
}