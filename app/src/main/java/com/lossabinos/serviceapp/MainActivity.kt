package com.lossabinos.serviceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.lossabinos.serviceapp.navigation.NavGraph
import com.lossabinos.serviceapp.screens.login.LoginScreen
import com.lossabinos.serviceapp.ui.theme.LosabosTheme
import com.lossabinos.serviceapp.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LosabosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation()
                    //LoginScreen()
                }
            }

        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = hiltViewModel()  // ← Inyectado por Hilt

    NavGraph(
        navController = navController,
        loginViewModel = loginViewModel
    )
}