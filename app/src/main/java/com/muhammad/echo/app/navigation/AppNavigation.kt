package com.muhammad.echo.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.muhammad.echo.app.widget.ACTION_CREATE_ECHO
import com.muhammad.echo.echos.presentation.create_echo.CreateEchoScreen
import com.muhammad.echo.echos.presentation.echos.EchoScreen
import com.muhammad.echo.echos.presentation.settings.SettingScreen
import com.muhammad.echo.echos.presentation.utils.toCreateEchoDestination

@Composable
fun AppNavigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Destinations.EchoScreen(startRecording = false)
    ){
        composable<Destinations.EchoScreen>(deepLinks = listOf(
            navDeepLink<Destinations.EchoScreen>(basePath = "https://echo.com/echos"){
                action = ACTION_CREATE_ECHO
            }
        )){
            EchoScreen(onNavigateToCreateEcho = {details ->
                navHostController.navigate(details.toCreateEchoDestination())
            }, onNavigateToSettings = {
                navHostController.navigate(Destinations.SettingScreen)
            })
        }
        composable<Destinations.CreateEchoScreen>{
            CreateEchoScreen(onConfirmLeave = {
                navHostController.navigateUp()
            })
        }
        composable<Destinations.SettingScreen>{
            SettingScreen(onGoBack = {
                navHostController.navigateUp()
            })
        }
    }
}