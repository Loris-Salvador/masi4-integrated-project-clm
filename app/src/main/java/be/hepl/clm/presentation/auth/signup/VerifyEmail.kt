package be.hepl.clm.presentation.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import be.hepl.clm.domain.LoginMethod
import be.hepl.clm.presentation.auth.login.LoginViewModel
import be.hepl.clm.presentation.navigation.Destinations

@Composable
fun VerifyEmail(
    modifier: Modifier = Modifier,
    navController: NavController,
    signupViewModel: SignupViewModel
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 100.dp, end = 50.dp, start = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Vous avez reçu un email avec un code", textAlign = TextAlign.Center, fontSize = 24.sp, lineHeight = 40.sp)


        Spacer(modifier = Modifier.height(175.dp))

        Text("Veuillez entrer le code reçu", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        CodeInputField(signupViewModel)

        Spacer(modifier = Modifier.height(24.dp))

        if(signupViewModel.isChallengeErrorMessageVisible)
        {
            ErrorChallengeMessage(signupViewModel)
            Spacer(modifier = Modifier.height(20.dp))
        }

        Button(
            onClick = { signupViewModel.onValidateButtonClick(LoginMethod.EMAIL,
                { navController.navigate(Destinations.LOGIN)
                }) },
            enabled = signupViewModel.challenge.length == 6
        ) {
            Text("Valider")
        }
    }
}



@Composable
fun CodeInputField(signupViewModel: SignupViewModel) {
    OutlinedTextField(
        value = signupViewModel.challenge,
        onValueChange = {
            if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                signupViewModel.onChallengeChanged(it)
            }
        },
        placeholder = { Text("XXXXXX") },
        label = { Text("Code à 6 chiffres") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    )
}


@Composable
fun ErrorChallengeMessage(signupViewModel: SignupViewModel){
    Text(signupViewModel.challengeErrorMessage, color = Color.Red, )
}
