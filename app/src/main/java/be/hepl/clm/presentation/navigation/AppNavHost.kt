package be.hepl.clm.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import be.hepl.clm.domain.LoginMethod
import be.hepl.clm.presentation.login.ChallengeScreen
import be.hepl.clm.presentation.login.LoginChoiceScreen
import be.hepl.clm.presentation.login.LoginScreen
import be.hepl.clm.presentation.login.LoginViewModel
import be.hepl.clm.presentation.navigation.Destinations.LOGIN
import be.hepl.clm.presentation.navigation.Destinations.LOGIN_CHOICE


@Composable
fun AppNavHost(modifier: Modifier) {
    val navController = rememberNavController()

    val loginViewModel: LoginViewModel = hiltViewModel()


    NavHost(navController = navController, startDestination = LOGIN) {
        composable(LOGIN) {
            LoginScreen(modifier, navController, loginViewModel)
        }
        composable(LOGIN_CHOICE) {
            LoginChoiceScreen(modifier, navController, loginViewModel)
        }
        composable(Destinations.CHALLENGE_WITH_ARG) { backStackEntry ->
            val methodString = backStackEntry.arguments?.getString("method") ?: "EMAIL"
            val method = LoginMethod.valueOf(methodString)
            ChallengeScreen(modifier, method, navController, loginViewModel)
        }
    }
}