package com.example.smartcaretrial

import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(navController: NavController) {
    var userInfo by remember { mutableStateOf(UserInfo()) }
    var registerError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Male", "Female")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Register", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.width(16.dp))

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                listOf("Patient", "Doctor").forEachIndexed { index, label ->
                    SegmentedButton(
                        selected = userInfo.role == label,
                        onClick = { userInfo = userInfo.copy(role = label) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = 2
                        )
                    ) {
                        Text(label)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            OutlinedTextField(
                value = userInfo.firstName,
                onValueChange = { input ->
                    if (input.all { it.isLetter() || it.isWhitespace() })
                        userInfo = userInfo.copy(firstName = input)
                },
                label = { Text("Name") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = userInfo.lastName,
                onValueChange = { input ->
                    if (input.all { it.isLetter() || it.isWhitespace() })
                        userInfo = userInfo.copy(lastName = input)
                },
                label = { Text("Last Name") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = userInfo.gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gender") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                userInfo = userInfo.copy(gender = option)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer (modifier = Modifier.width(8.dp))


            OutlinedTextField(
                value = userInfo.age,
                onValueChange = { userInfo = userInfo.copy(age = it) },
                label = { Text(text = "Age") },
                readOnly = true,
                modifier = Modifier.clickable { showDatePicker = true }.weight(1f),
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select date")
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val formatted = convertMillisToDate(millis)
                                userInfo = userInfo.copy(age = formatted)
                            }
                            showDatePicker = false }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = userInfo.contactNumber,
            onValueChange = { input ->
                if (input.length <= 11 && input.all { it.isDigit() }) {
                    userInfo = userInfo.copy(contactNumber = input)
                }
            },
            label = { Text("Contact Number") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        OutlinedTextField(
            value = userInfo.emergencyNumber,
            onValueChange = { input ->
                if (input.length <= 11 && input.all {it.isDigit() }) {
                    userInfo = userInfo.copy(emergencyNumber = input)
                }
            },
            label = { Text(text = "Emergency Number") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = userInfo.email,
            onValueChange = { input ->
                userInfo = userInfo.copy(email = input)
            },
            label = { Text("Email") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            isError = userInfo.email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(userInfo.email).matches(),
            supportingText = {
                if (userInfo.email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(userInfo.email).matches()) {
                    Text("Please enter a valid email address")
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = userInfo.password,
            onValueChange = { userInfo = userInfo.copy(password = it) },
            label = { Text("Password") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        // Only doctors need a specialty field
        if (userInfo.role == "Doctor") {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = userInfo.specialty,
                onValueChange = { input ->
                    if (input.all { it.isLetter() })
                        userInfo = userInfo.copy(specialty = input)
                },
                label = { Text("Specialty") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
        }

        // Show a registration error if there is one
        if (registerError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = registerError ?: "",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    val db = DatabaseProvider.getDatabase(context)
                    val existingUser = db.userDao().getUserByEmail(userInfo.email)

                    if (existingUser != null) {
                        registerError = "An account with that email already exists"
                    } else {
                        registerError = null
                        db.userDao().insertUser(userInfo)
                        navController.navigate("login")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC") // important! selected date is stored at UTC midnight
    return formatter.format(Date(millis))
}