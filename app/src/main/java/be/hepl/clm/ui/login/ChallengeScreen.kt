package be.hepl.clm.ui.login

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
    loginViewModel: LoginViewModel,
) {
    var code by remember { mutableStateOf("") }

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

        CodeInputField(code = code, onCodeChange = { code = it })

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick =
            {
                println("Code entré : $code")


            },
            enabled = code.length == 6
        ) {
            Text("Valider")
        }
    }
}



@Composable
fun CodeInputField(
    code: String,
    onCodeChange: (String) -> Unit
) {
    OutlinedTextField(
        value = code,
        onValueChange = {
            if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                onCodeChange(it)
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
