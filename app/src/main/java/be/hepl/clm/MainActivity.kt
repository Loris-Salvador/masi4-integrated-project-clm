package be.hepl.clm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import be.hepl.clm.ui.login.LoginChoiceScreen
import be.hepl.clm.ui.login.LoginMainScreen
import be.hepl.clm.ui.login.LoginViewModel
import be.hepl.clm.ui.theme.CLMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CLMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Composable
fun AppNavHost(modifier: Modifier) {
    val navController = rememberNavController()

    val loginViewModel: LoginViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginMainScreen(modifier, navController, loginViewModel)
        }
        composable("login_choice") {
            LoginChoiceScreen(modifier, navController, loginViewModel)
        }
    }
}
