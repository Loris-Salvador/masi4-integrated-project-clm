package be.hepl.clm.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import be.hepl.clm.domain.LoginMethod
import be.hepl.clm.presentation.MainScreenWithBottomNav
import be.hepl.clm.presentation.auth.login.ChallengeScreen
import be.hepl.clm.presentation.auth.login.LoginChoiceScreen
import be.hepl.clm.presentation.auth.login.LoginScreen
import be.hepl.clm.presentation.auth.login.LoginViewModel
import be.hepl.clm.presentation.auth.signup.SignupScreen
import be.hepl.clm.presentation.auth.signup.SignupViewModel
import be.hepl.clm.presentation.auth.signup.VerifyEmail
import be.hepl.clm.presentation.cart.CartScreen
import be.hepl.clm.presentation.chat.ChatScreen
import be.hepl.clm.presentation.home.HomeScreen
import be.hepl.clm.presentation.home.HomeViewModel
import be.hepl.clm.presentation.navigation.Destinations.LOGIN
import be.hepl.clm.presentation.navigation.Destinations.LOGIN_CHOICE
import be.hepl.clm.presentation.navigation.Destinations.SIGNUP
import be.hepl.clm.presentation.payment.PaymentScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(modifier: Modifier) {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val signupViewModel: SignupViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Destinations.LOGIN) {
        // Login related screens - no bottom navigation
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
        composable(SIGNUP) {
            SignupScreen(signupViewModel ,navController)
        }
        composable(Destinations.VERIFY_EMAIL) {
            VerifyEmail(navController = navController, signupViewModel = signupViewModel)
        }

        // Screens with bottom navigation
        composable(Destinations.HOME) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            MainScreenWithBottomNav(navController = navController) {
                HomeScreen(homeViewModel)
            }
        }
        composable(Destinations.CART) {
            MainScreenWithBottomNav(navController = navController) {
                CartScreen(navController = navController)
            }
        }
        composable(Destinations.CHAT) {
            MainScreenWithBottomNav(navController = navController) {
                ChatScreen()
            }
        }
        composable("payment") {
            PaymentScreen(navController)
        }
    }
}