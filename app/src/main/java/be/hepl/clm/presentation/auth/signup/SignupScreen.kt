package be.hepl.clm.presentation.auth.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import be.hepl.clm.domain.Gender
import be.hepl.clm.presentation.navigation.Destinations
import be.hepl.clm.presentation.theme.loginButtonColors
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.res.stringResource
import be.hepl.clm.R

@Composable
fun SignupScreen(signupViewModel: SignupViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 30.dp, end = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Text(stringResource(R.string.signup), fontSize = 40.sp)
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
        SignupButton(onClick = {
            signupViewModel.signup {
                navController.navigate(Destinations.VERIFY_EMAIL)
            }
        }, navController = navController)
    }
}

@Composable
fun EmailTextField(signupViewModel: SignupViewModel) {
    OutlinedTextField(
        label = { Text(stringResource(R.string.email)) },
        value = signupViewModel.email.value,
        onValueChange = signupViewModel::onEmailChange,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
        keyboardOptions = KeyboardOptions.Default
    )
}

@Composable
fun PhoneNumberTextField(signupViewModel: SignupViewModel) {
    OutlinedTextField(
        label = { Text(stringResource(R.string.phone_number)) },
        value = signupViewModel.phoneNumber.value,
        onValueChange = signupViewModel::onPhoneNumberChanged,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null) },
        keyboardOptions = KeyboardOptions.Default
    )
}

@Composable
fun FirstNameTextField(signupViewModel: SignupViewModel) {
    OutlinedTextField(
        label = { Text(stringResource(R.string.first_name)) },
        value = signupViewModel.firstName.value,
        onValueChange = signupViewModel::onFirstNameChanged,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
        keyboardOptions = KeyboardOptions.Default
    )
}

@Composable
fun LastNameTextField(signupViewModel: SignupViewModel) {
    OutlinedTextField(
        label = { Text(stringResource(R.string.last_name)) },
        value = signupViewModel.lastName.value,
        onValueChange = signupViewModel::onLastNameChanged,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
        keyboardOptions = KeyboardOptions.Default
    )
}

@Composable
fun PasswordTextFieldComponent(signupViewModel: SignupViewModel) {
    OutlinedTextField(
        label = { Text(stringResource(R.string.password)) },
        value = signupViewModel.password.value,
        onValueChange = signupViewModel::onPasswordChanged,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
        trailingIcon = {
            val iconImage = if (signupViewModel.isPasswordVisible.value)
                Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
            val description = if (signupViewModel.isPasswordVisible.value)
                stringResource(R.string.show_password) else stringResource(R.string.hide_password)
            IconButton(onClick = signupViewModel::onPasswordVisibilityChanged) {
                Icon(iconImage, contentDescription = description)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (signupViewModel.isPasswordVisible.value)
            VisualTransformation.None else PasswordVisualTransformation()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropDownMenu() {
    val itemList = listOf(
        GenderDropDownItem(Gender.M, Icons.Outlined.Male, stringResource(R.string.male)),
        GenderDropDownItem(Gender.F, Icons.Outlined.Female, stringResource(R.string.female)),
        GenderDropDownItem(Gender.O, Icons.Outlined.Person, stringResource(R.string.other))
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(itemList.first()) }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                value = selectedItem.title,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = stringResource(R.string.select_gender), color = Color.Gray) },
                leadingIcon = { Icon(selectedItem.icon, contentDescription = selectedItem.title) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
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
                                Icon(item.icon, contentDescription = item.title)
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
    val selectedDate = datePickerState.selectedDateMillis?.let { convertMillisToDate(it) } ?: ""

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = { Text(stringResource(R.string.birthdate)) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth().height(64.dp)
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
                        .shadow(4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(state = datePickerState, showModeToggle = false)
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
fun SignupButton(onClick: () -> Unit, isLoading: Boolean = false, navController: NavController) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(210.dp).height(50.dp),
        colors = loginButtonColors(),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
        } else {
            Text(text = stringResource(R.string.signup), fontSize = 18.sp)
        }
    }
}
