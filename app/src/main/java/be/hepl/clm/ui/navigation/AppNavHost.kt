package be.hepl.clm.ui.navigation



import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import be.hepl.clm.ui.login.LoginChoiceScreen
import be.hepl.clm.ui.login.LoginScreen
import be.hepl.clm.ui.login.LoginViewModel
import be.hepl.clm.ui.navigation.Destinations.LOGIN
import be.hepl.clm.ui.navigation.Destinations.LOGIN_CHOICE


@Composable
fun AppNavHost(modifier: Modifier) {
    val navController = rememberNavController()

    val loginViewModel: LoginViewModel = viewModel()

    NavHost(navController = navController, startDestination = LOGIN) {
        composable(LOGIN) {
            LoginScreen(modifier, navController, loginViewModel)
        }
        composable(LOGIN_CHOICE) {
            LoginChoiceScreen(modifier, navController, loginViewModel)
        }
    }
}