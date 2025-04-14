package be.hepl.clm.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.hepl.clm.data.auth.AuthRepository
import be.hepl.clm.data.token.TokenRepository
import be.hepl.clm.domain.LoginMethod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository, private val tokenRepository: TokenRepository) : ViewModel() {
    var email by mutableStateOf("loris3salvador@gmail.com")
    var password by mutableStateOf("Passw0rd!")

    var challenge by mutableStateOf("")

    fun onEmailChanged(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        password = newPassword
    }

    fun onSMSChoiceButtonClick() {
        viewModelScope.launch {
            val result = authRepository.phoneLogin(email, password)
            println(result)
        }
    }

    fun onEmailChoiceButtonClick() {
        viewModelScope.launch {
            val result =authRepository.emailLogin(email, password)
            println(result)
        }
    }

    fun onChallengeChanged(newChallenge: String) {
        challenge = newChallenge
    }


    fun onValidateButtonClick(loginMethod: LoginMethod, onSuccessNavigate: () -> Unit) {

        if(loginMethod == LoginMethod.EMAIL)
        {
            viewModelScope.launch {
                val result = authRepository.emailChallenge(email, challenge)

                result.onSuccess { response ->
                    tokenRepository.saveToken(response.accessToken)
                    onSuccessNavigate()
                }

                result.onFailure { e ->
                    println("Erreur de login: ${e.message}")
                }
            }
        }
        else if(loginMethod == LoginMethod.SMS)
        {
            viewModelScope.launch {
                val result = authRepository.phoneChallenge(email, challenge)

                result.onSuccess { response ->
                    tokenRepository.saveToken(response.accessToken)
                    onSuccessNavigate()
                }

                result.onFailure { e ->
                    println("Erreur de login: ${e.message}")
                }
            }
        }
    }

}

