package be.hepl.clm.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.hepl.clm.data.auth.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(authRepositoryImpl: AuthRepositoryImpl) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    private val _authRepository = authRepositoryImpl


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
        println(email)
        println(password)

        viewModelScope.launch {
            val result =_authRepository.login(email, password)
            println(result)
        }

    }


}