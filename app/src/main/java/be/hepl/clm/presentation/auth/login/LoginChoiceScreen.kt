package be.hepl.clm.presentation.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import be.hepl.clm.R
import be.hepl.clm.presentation.theme.loginButtonColors

@Composable
fun LoginChoiceScreen(modifier: Modifier = Modifier, navController: NavController, loginViewModel: LoginViewModel) {


    Column(
        modifier = modifier
            .padding(horizontal = 58.dp)
            .padding(bottom = 100.dp)
            .padding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource((R.string.choose_your_connection_method)),
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    loginViewModel.onSMSChoiceButtonClick(
                        onSuccessNavigate = {
                            navController.navigate("challenge/SMS")
                        },
                        onErrorNavigate = {
                            navController.popBackStack()
                        }
                    )
                },
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp),
                colors = loginButtonColors()
            ) {
                Text(text = stringResource((R.string.sms)), fontSize = 18.sp)
            }

            Button(
                onClick = {
                    loginViewModel.onEmailChoiceButtonClick(
                        onSuccessNavigate = {
                            navController.navigate("challenge/EMAIL")
                        },
                        onErrorNavigate = {
                            navController.popBackStack()
                        }
                    )
                },
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp),
                colors = loginButtonColors()
            ) {
                Text(text = stringResource((R.string.email)), fontSize = 18.sp)
            }
        }
    }
}