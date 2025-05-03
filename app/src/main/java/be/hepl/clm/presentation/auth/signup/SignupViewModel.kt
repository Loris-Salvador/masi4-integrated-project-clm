package be.hepl.clm.presentation.auth.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.hepl.clm.data.auth.AuthRepository
import be.hepl.clm.domain.CustomerSignup
import be.hepl.clm.domain.Gender
import be.hepl.clm.domain.LoginMethod
import be.hepl.clm.domain.VerifyRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    var email = mutableStateOf("")
        private set

    var password = mutableStateOf("")
    var phoneNumber = mutableStateOf("")
    var firstName = mutableStateOf("")
    var lastName = mutableStateOf("")
    var gender = mutableStateOf(Gender.M)
    var birthdate = mutableStateOf<Instant?>(null)

    var isPasswordVisible = mutableStateOf(false)

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var signupSuccess = mutableStateOf(false)

    fun onEmailChange(newEmail: String) {
        email.value = newEmail
    }

    fun onPhoneNumberChanged(newPhone: String) {
        phoneNumber.value = newPhone
    }

    fun onFirstNameChanged(newFirstName: String) {
        firstName.value = newFirstName
    }

    fun onLastNameChanged(newLastName: String) {
        lastName.value = newLastName
    }

    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
    }

    fun onGenderChanged(newGender: Gender) {
        gender.value = newGender
    }

    fun onBirthdateChanged(newBirthdate: Instant) {
        birthdate.value = newBirthdate
    }

    fun onPasswordVisibilityChanged() {
        isPasswordVisible.value = !isPasswordVisible.value
    }

    private fun getCustomerDto(): CustomerSignup {
        return CustomerSignup(
            email = email.value,
            password = password.value,
            phoneNumber = phoneNumber.value,
            name = firstName.value,
            lastName = lastName.value,
            gender = gender.value,
            birthday = birthdate.value
        )
    }

    fun signup(onSuccessNavigate: () -> Unit) = viewModelScope.launch {
        try {
            isLoading.value = true
            errorMessage.value = null


            val customerDto = getCustomerDto()
            val result = authRepository.signup(customerDto)

            result.fold(
                onSuccess = {
                    signupSuccess.value = true
                    authRepository.verifyEmail(VerifyRequest(email = email.value))
                    onSuccessNavigate()
                },
                onFailure = { exception ->
                    errorMessage.value = exception.message ?: "An error occurred during signup"
                }
            )
        } catch (e: Exception) {
            errorMessage.value = e.message ?: "An unexpected error occurred"
        } finally {
            isLoading.value = false
        }
    }


    var challenge by mutableStateOf("")

    var challengeErrorMessage by mutableStateOf("")
    var isChallengeErrorMessageVisible by mutableStateOf(false)


    fun onChallengeChanged(newChallenge: String) {
        challenge = newChallenge
    }


    fun onValidateButtonClick(loginMethod: LoginMethod, onSuccessNavigate: () -> Unit) {

        if(loginMethod == LoginMethod.EMAIL)
        {
            viewModelScope.launch {
                val result = authRepository.verifyEmailChallenge(email.value, challenge)

                result.onSuccess {
                    onSuccessNavigate()
                }
            }
        }
    }
}