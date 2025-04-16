package be.hepl.clm.presentation.login

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import be.hepl.clm.domain.LoginMethod

@Composable
fun ChallengeScreen(
    modifier: Modifier = Modifier,
    method: LoginMethod,
    navController: NavController,
    loginViewModel: LoginViewModel
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 100.dp, end = 50.dp, start = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(method == LoginMethod.EMAIL)
            Text("Vous avez reçu un email avec un code", textAlign = TextAlign.Center, fontSize = 24.sp, lineHeight = 40.sp)
        else
            Text("Vous avez reçu un message avec un code", textAlign = TextAlign.Center, fontSize = 24.sp, lineHeight = 40.sp)

        Spacer(modifier = Modifier.height(175.dp))

        Text("Veuillez entrer le code reçu", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        CodeInputField(loginViewModel)

        Spacer(modifier = Modifier.height(24.dp))

        if(loginViewModel.isChallengeErrorMessageVisible)
        {
            ErrorChallengeMessage(loginViewModel)
            Spacer(modifier = Modifier.height(20.dp))
        }

        Button(
            onClick = { loginViewModel.onValidateButtonClick(method,
                { navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }}) },
            enabled = loginViewModel.challenge.length == 6
        ) {
            Text("Valider")
        }
    }
}



@Composable
fun CodeInputField(loginViewModel: LoginViewModel) {
    OutlinedTextField(
        value = loginViewModel.challenge,
        onValueChange = {
            if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                loginViewModel.onChallengeChanged(it)
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
fun ErrorChallengeMessage(loginViewModel: LoginViewModel){
    Text(loginViewModel.challengeErrorMessage, color = Color.Red, )
}
