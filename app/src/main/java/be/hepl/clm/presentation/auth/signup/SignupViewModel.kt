package be.hepl.clm.presentation.auth.signup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.hepl.clm.data.auth.AuthRepository
import be.hepl.clm.domain.CustomerSignupDTO
import be.hepl.clm.domain.Gender
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

    // État pour gérer le processus d'inscription
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

    private fun getCustomerDto(): CustomerSignupDTO {
        return CustomerSignupDTO(
            email = email.value,
            password = password.value,
            phoneNumber = phoneNumber.value,
            name = firstName.value,
            lastName = lastName.value,
            gender = gender.value,
            birthday = birthdate.value
        )
    }

    // Nouvelle méthode pour gérer l'inscription
    fun signup(onSuccessNavigate: () -> Unit) = viewModelScope.launch {
        try {
            isLoading.value = true
            errorMessage.value = null


            val customerDto = getCustomerDto()
            val result = authRepository.signup(customerDto)

            result.fold(
                onSuccess = {
                    signupSuccess.value = true
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
}