package be.hepl.clm.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")


    fun onEmailChanged(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        password = newPassword
    }

    fun onSMSChoiceClick() {
        println(email)
    }

    fun onEmailChoiceClick() {
        println("eddd $email")
    }
}