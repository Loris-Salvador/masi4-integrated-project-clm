package be.hepl.clm.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import be.hepl.clm.R
import be.hepl.clm.presentation.theme.Blue
import be.hepl.clm.presentation.theme.loginButtonColors


@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavController, loginViewModel: LoginViewModel) {


    Column(
        modifier = modifier
            .padding(horizontal = 58.dp)
            .padding(vertical = 36.dp)
            .padding(top = 50.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LoginTitleText()

        Spacer(modifier = Modifier.height(80.dp))

        LoginEmailTextField(loginViewModel)
        LoginPasswordTextField(loginViewModel)


        if(loginViewModel.isLoginErrorMessageVisible)
        {
            Spacer(modifier = Modifier.height(20.dp))

            ErrorLoginMessage(loginViewModel)

            Spacer(modifier = Modifier.height(20.dp))
        }
        else
            Spacer(modifier = Modifier.height(70.dp))


        LoginButton(navController)

        Spacer(modifier = Modifier.height(40.dp))

        LoginText("ou")

        Spacer(modifier = Modifier.height(40.dp))

        MasiIdLoginButton()

        Spacer(modifier = Modifier.height(50.dp))

        LoginText("Vous n'avez pas de compte ?")

        Spacer(modifier = Modifier.height(30.dp))

        LoginClickableText("Créer un compte ici", navController)
    }
}



@Composable
fun LoginTitleText() {
    Text(
        text = "Login",
        fontSize = 55.sp,
    )
}

@Composable
fun LoginEmailTextField(loginViewModel: LoginViewModel) {
    OutlinedTextField(
        value = loginViewModel.email,
        onValueChange = { newEmail ->
            loginViewModel.onEmailChanged(newEmail)
        },
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun LoginPasswordTextField(loginViewModel: LoginViewModel) {
    OutlinedTextField(
        value = loginViewModel.password,
        onValueChange = { newPassword ->
            loginViewModel.onPasswordChanged(newPassword)
        },
        label = { Text("Password") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun ErrorLoginMessage(loginViewModel: LoginViewModel){
    Text(loginViewModel.loginErrorMessage, color = Color.Red, )
}


@Composable
fun LoginButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate("login_choice")
        },
        modifier = Modifier
            .width(210.dp)
            .height(50.dp),
        colors = loginButtonColors()
    ) {
        Text(
            text = "Login",
            fontSize = 18.sp
        )
    }
}

@Composable
fun MasiIdLoginButton() {
    Button(onClick = {}, modifier = Modifier
        .width(190.dp)
        .height(60.dp),
        colors = loginButtonColors())
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        )
        {
            Image(
                painter = painterResource(id = R.drawable.masimart),
                contentDescription = "Logo MASI",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "MASI ID", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}


@Composable
fun LoginText(text: String) {
    Text(
        text = text,
        fontSize = 18.sp
    )
}

@Composable
fun LoginClickableText(text: String, navController: NavController) {
    val annotatedString = buildAnnotatedString {
        append(text)
    }

    BasicText(
        text = annotatedString,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    //navController.navigate("")
                }
            )
        },
        style = TextStyle(
            color = Blue,
            fontSize = 18.sp,
            textDecoration = TextDecoration.Underline
        )
    )
}
