package com.muhammad.echo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.muhammad.echo.app.navigation.AppNavigation
import com.muhammad.echo.core.presentation.designsystem.theme.EchoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EchoTheme {
                val navHostController = rememberNavController()
                AppNavigation(navHostController = navHostController)
            }
        }
    }
}