package be.hepl.clm.presentation.auth.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.DateRange
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
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun SignupScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 30.dp, end = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Text("Signup", fontSize = 40.sp)
        Spacer(modifier = Modifier.height(50.dp))
        MyTextFieldComponent("Email", icon = Icons.Outlined.Email)
        Spacer(modifier = Modifier.height(5.dp))
        PasswordTextFieldComponent("Password", icon = Icons.Outlined.Lock)
        Spacer(modifier = Modifier.height(5.dp))
        MyTextFieldComponent("Phone Number", icon = Icons.Outlined.Phone)
        Spacer(modifier = Modifier.height(5.dp))
        MyTextFieldComponent("First Name", icon = Icons.Outlined.Person)
        Spacer(modifier = Modifier.height(5.dp))
        MyTextFieldComponent("Last Name", icon = Icons.Outlined.Person)
        GenderDropDownMenu()
        DatePickerDocked()
    }
}


@Composable
fun MyTextFieldComponent(labelValue: String, icon: ImageVector) {
    var textValue by remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        label = {
            Text(text = labelValue)
        },
        value = textValue,
        onValueChange = {
            textValue = it
        },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "profile"
            )
        },
        keyboardOptions = KeyboardOptions.Default
    )
}

@Composable
fun PasswordTextFieldComponent(labelValue: String, icon: ImageVector) {
    var password by remember {
        mutableStateOf("")
    }

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        label = {
            Text(text = labelValue)
        },
        value = password,
        onValueChange = {
            password = it
        },

        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "profile"
            )
        },
        trailingIcon = {
            val iconImage =
                if (isPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
            val description = if (isPasswordVisible) "Show Password" else "Hide Password"
            IconButton(onClick = {
                isPasswordVisible = !isPasswordVisible
            }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropDownMenu() {
    val itemList = listOf(
        GenderDropDownItem(Icons.Outlined.Male, "Male"),
        GenderDropDownItem(Icons.Outlined.Female, "Female"),
        GenderDropDownItem(Icons.Outlined.Person, "Other")
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
                    color = Color.Gray // ou MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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

