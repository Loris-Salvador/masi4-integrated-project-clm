package be.hepl.clm.presentation.auth.signup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import be.hepl.clm.domain.CustomerSignupDTO
import be.hepl.clm.domain.Gender
import kotlinx.datetime.Instant

class SignupViewModel : ViewModel() {
    var email = mutableStateOf("")
        private set

    var password = mutableStateOf("")
    var phoneNumber = mutableStateOf("")
    var firstName = mutableStateOf("")
    var lastName = mutableStateOf("")
    var gender = mutableStateOf(Gender.M)
    var birthday = mutableStateOf<Instant?>(null)



    var isPasswordVisible = mutableStateOf(false)

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


    fun onPasswordVisibilityChanged() {
        isPasswordVisible.value = !isPasswordVisible.value
    }



    fun getCustomerDto(): CustomerSignupDTO {
        return CustomerSignupDTO(
            email = email.value,
            password = password.value,
            phoneNumber = phoneNumber.value,
            name = firstName.value,
            lastName = lastName.value,
            gender = gender.value,
            birthday = birthday.value
        )
    }
}
