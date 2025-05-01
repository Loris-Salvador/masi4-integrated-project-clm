package be.hepl.clm.presentation.auth.signup


import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import be.hepl.clm.domain.Gender
import be.hepl.clm.presentation.auth.login.LoginViewModel
import be.hepl.clm.presentation.navigation.Destinations
import be.hepl.clm.presentation.theme.loginButtonColors
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun SignupScreen(navController: NavController) {


    val signupViewModel: SignupViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 30.dp, end = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Text("Signup", fontSize = 40.sp)
        Spacer(modifier = Modifier.height(50.dp))
        EmailTextField(signupViewModel)
        Spacer(modifier = Modifier.height(5.dp))
        PasswordTextFieldComponent(signupViewModel)
        Spacer(modifier = Modifier.height(5.dp))
        PhoneNumberTextField(signupViewModel)
        Spacer(modifier = Modifier.height(5.dp))
        FirstNameTextField(signupViewModel)
        Spacer(modifier = Modifier.height(5.dp))
        LastNameTextField(signupViewModel)
        Spacer(modifier = Modifier.height(5.dp))
        GenderDropDownMenu()
        Spacer(modifier = Modifier.height(5.dp))
        DatePickerDocked()
        Spacer(modifier = Modifier.height(80.dp))
        SignupButton(onClick =  { signupViewModel.signup({ navController.navigate(Destinations.LOGIN) }) }, navController = navController)
    }
}

@Composable
fun EmailTextField(signupViewModel: SignupViewModel) {
    OutlinedTextField(
        label = {
            Text(text = "Email")
        },
        value = signupViewModel.email.value,
        onValueChange = {
            signupViewModel.onEmailChange(it)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = "profile"
            )
        },
        keyboardOptions = KeyboardOptions.Default
    )
}

@Composable
fun PhoneNumberTextField(signupViewModel: SignupViewModel) {
    OutlinedTextField(
        label = {
            Text(text = "Phone Number")
        },
        value = signupViewModel.phoneNumber.value,
        onValueChange = {
            signupViewModel.onPhoneNumberChanged(it)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Phone,
                contentDescription = "profile"
            )
        },
        keyboardOptions = KeyboardOptions.Default
    )
}

@Composable
fun FirstNameTextField(signupViewModel: SignupViewModel) {
    OutlinedTextField(
        label = {
            Text(text = "First Name")
        },
        value = signupViewModel.firstName.value,
        onValueChange = {
            signupViewModel.onFirstNameChanged(it)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "profile"
            )
        },
        keyboardOptions = KeyboardOptions.Default
    )
}

@Composable
fun LastNameTextField(signupViewModel: SignupViewModel) {
    OutlinedTextField(
        label = {
            Text(text = "Last Name")
        },
        value = signupViewModel.lastName.value,
        onValueChange = {
            signupViewModel.onLastNameChanged(it)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "profile"
            )
        },
        keyboardOptions = KeyboardOptions.Default
    )
}


@Composable
fun PasswordTextFieldComponent(signupViewModel: SignupViewModel) {

    OutlinedTextField(
        label = {
            Text(text = "Password")
        },
        value = signupViewModel.password.value,
        onValueChange = {
            signupViewModel.onPasswordChanged(it)
        },

        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = "profile"
            )
        },
        trailingIcon = {
            val iconImage =
                if (signupViewModel.isPasswordVisible.value) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
            val description = if (signupViewModel.isPasswordVisible.value) "Show Password" else "Hide Password"
            IconButton(onClick = {
                signupViewModel.onPasswordVisibilityChanged()
            }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (signupViewModel.isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation()
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropDownMenu() {
    val itemList = listOf(
        GenderDropDownItem(Gender.M, Icons.Outlined.Male, "Male"),
        GenderDropDownItem(Gender.F, Icons.Outlined.Female, "Female"),
        GenderDropDownItem(Gender.O, Icons.Outlined.Person, "Other")
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember {
        mutableStateOf(itemList.first())
    }

    Column {

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                value = selectedItem.title,
                onValueChange = {},
                readOnly = true,
                label = {     Text(
                    text = "Select gender",
                    color = Color.Gray
                ) },
                leadingIcon = {
                    Icon(
                        imageVector = selectedItem.icon,
                        contentDescription = selectedItem.title
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color.DarkGray,
                    cursorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp)
            )


            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                itemList.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                                Text(item.title)
                            }
                        },
                        onClick = {
                            selectedItem = item
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked() {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("Birthdate") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Composable
fun SignupButton(
    onClick: () -> Unit,
    isLoading: Boolean = false,
    navController: NavController
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(210.dp)
            .height(50.dp),
        colors = loginButtonColors(),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White
            )
        } else {
            Text(
                text = "Signup",
                fontSize = 18.sp
            )
        }
    }
}