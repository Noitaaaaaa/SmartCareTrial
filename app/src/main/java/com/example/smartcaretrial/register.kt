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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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

// Text field colors reused across every step so they all match
@Composable
private fun fieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(navController: NavController) {
    var userInfo by remember { mutableStateOf(UserInfo()) }
    var registerError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Doctors get a 4th step (Specialty), Patients only need 3
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = if (userInfo.role == "Doctor") 4 else 3

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Register", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Progress indicator so the user knows how far along they are
        LinearProgressIndicator(
            progress = { currentStep / totalSteps.toFloat() },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Step $currentStep of $totalSteps",
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Each step is its own Composable — only one is shown at a time.
        // Everything still writes into the same `userInfo` object, so
        // nothing is lost moving between steps.
        when (currentStep) {
            1 -> StepRoleAndBasicInfo(
                userInfo = userInfo,
                onUserInfoChange = { userInfo = it }
            )
            2 -> StepContactInfo(
                userInfo = userInfo,
                onUserInfoChange = { userInfo = it }
            )
            3 -> StepSecurityAndAccount(
                userInfo = userInfo,
                onUserInfoChange = { userInfo = it }
            )
            4 -> StepSpecialty(
                userInfo = userInfo,
                onUserInfoChange = { userInfo = it }
            )
        }

        if (registerError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = registerError ?: "",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back button — hidden on the very first step
            if (currentStep > 1) {
                OutlinedButton(onClick = { currentStep-- }) {
                    Text("Back")
                }
            } else {
                Spacer(modifier = Modifier.width(1.dp)) // keeps Next aligned right
            }

            if (currentStep < totalSteps) {
                Button(onClick = {
                    registerError = validateStep(currentStep, userInfo)
                    if (registerError == null) currentStep++
                }) {
                    Text("Next")
                }
            } else {
                Button(onClick = {
                    registerError = validateStep(currentStep, userInfo)
                    if (registerError == null) {
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
                    }
                }) {
                    Text("Register")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ---------- STEP 1: Role, Name, Gender, Age ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StepRoleAndBasicInfo(
    userInfo: UserInfo,
    onUserInfoChange: (UserInfo) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var expandedGender by remember { mutableStateOf(false) }
    val genderOptions = listOf("Male", "Female")

    Column {
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            listOf("Patient", "Doctor").forEachIndexed { index, label ->
                SegmentedButton(
                    selected = userInfo.role == label,
                    onClick = { onUserInfoChange(userInfo.copy(role = label)) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = 2)
                ) {
                    Text(label)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            OutlinedTextField(
                value = userInfo.firstName,
                onValueChange = { input ->
                    if (input.all { it.isLetter() || it.isWhitespace() })
                        onUserInfoChange(userInfo.copy(firstName = input))
                },
                label = { Text("First Name") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = fieldColors(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = userInfo.lastName,
                onValueChange = { input ->
                    if (input.all { it.isLetter() || it.isWhitespace() })
                        onUserInfoChange(userInfo.copy(lastName = input))
                },
                label = { Text("Last Name") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = fieldColors(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            ExposedDropdownMenuBox(
                expanded = expandedGender,
                onExpandedChange = { expandedGender = !expandedGender },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = userInfo.gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gender") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = expandedGender,
                    onDismissRequest = { expandedGender = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onUserInfoChange(userInfo.copy(gender = option))
                                expandedGender = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = userInfo.Birthdate,
                onValueChange = { onUserInfoChange(userInfo.copy(Birthdate = it)) },
                label = { Text("Birthdate") },
                readOnly = true,
                modifier = Modifier
                    .clickable { showDatePicker = true }
                    .weight(1f),
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select date")
                    }
                },
                colors = fieldColors()
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                onUserInfoChange(userInfo.copy(Birthdate = convertMillisToDate(millis)))
                            }
                            showDatePicker = false
                        }) {
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
    }
}

// ---------- STEP 2: Contact info ----------
@Composable
private fun StepContactInfo(
    userInfo: UserInfo,
    onUserInfoChange: (UserInfo) -> Unit
) {
    Column {
        OutlinedTextField(
            value = userInfo.contactNumber,
            onValueChange = { input ->
                if (input.length <= 11 && input.all { it.isDigit() })
                    onUserInfoChange(userInfo.copy(contactNumber = input))
            },
            label = { Text("Contact Number") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            colors = fieldColors()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = userInfo.emergencyNumber,
            onValueChange = { input ->
                if (input.length <= 11 && input.all { it.isDigit() })
                    onUserInfoChange(userInfo.copy(emergencyNumber = input))
            },
            label = { Text("Emergency Number") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            colors = fieldColors()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = userInfo.email,
            onValueChange = { input -> onUserInfoChange(userInfo.copy(email = input)) },
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
            colors = fieldColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
    }
}

// ---------- STEP 3: Security question + Password ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StepSecurityAndAccount(
    userInfo: UserInfo,
    onUserInfoChange: (UserInfo) -> Unit
) {
    var expandedSecurity by remember { mutableStateOf(false) }
    val securityOptions = listOf("Mother's maiden name", "First dog name", "Where were you born")

    Column {
        ExposedDropdownMenuBox(
            expanded = expandedSecurity,
            onExpandedChange = { expandedSecurity = !expandedSecurity }
        ) {
            OutlinedTextField(
                value = userInfo.securityQuestion,
                onValueChange = {},
                readOnly = true,
                label = { Text("Security Question") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSecurity)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                shape = RoundedCornerShape(8.dp)
            )

            ExposedDropdownMenu(
                expanded = expandedSecurity,
                onDismissRequest = { expandedSecurity = false }
            ) {
                securityOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onUserInfoChange(userInfo.copy(securityQuestion = option))
                            expandedSecurity = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = userInfo.securityAnswer,
            onValueChange = { input ->
                if (input.all { it.isLetter() || it.isWhitespace() })
                    onUserInfoChange(userInfo.copy(securityAnswer = input))
            },
            label = { Text("Security Answer") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = fieldColors()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = userInfo.password,
            onValueChange = { onUserInfoChange(userInfo.copy(password = it)) },
            label = { Text("Password") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
}

// ---------- STEP 4: Specialty (Doctor only) ----------
@Composable
private fun StepSpecialty(
    userInfo: UserInfo,
    onUserInfoChange: (UserInfo) -> Unit
) {
    Column {
        OutlinedTextField(
            value = userInfo.specialty,
            onValueChange = { input ->
                if (input.all { it.isLetter() })
                    onUserInfoChange(userInfo.copy(specialty = input))
            },
            label = { Text("Specialty") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors()
        )
    }
}

// ---------- Per-step validation ----------
// Returns an error message if the step is incomplete, or null if it's OK to move on.
private fun validateStep(step: Int, userInfo: UserInfo): String? {
    return when (step) {
        1 -> when {
            userInfo.firstName.isBlank() -> "Please enter your first name"
            userInfo.lastName.isBlank() -> "Please enter your last name"
            userInfo.gender.isBlank() -> "Please select a gender"
            userInfo.Birthdate.isBlank() -> "Please select your birth date"
            else -> null
        }
        2 -> when {
            userInfo.contactNumber.isBlank() -> "Please enter a contact number"
            userInfo.email.isBlank() -> "Please enter an email"
            !Patterns.EMAIL_ADDRESS.matcher(userInfo.email).matches() -> "Please enter a valid email address"
            else -> null
        }
        3 -> when {
            userInfo.securityQuestion.isBlank() -> "Please select a security question"
            userInfo.securityAnswer.isBlank() -> "Please answer the security question"
            userInfo.password.isBlank() -> "Please enter a password"
            else -> null
        }
        4 -> when {
            userInfo.specialty.isBlank() -> "Please enter your specialty"
            else -> null
        }
        else -> null
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC") // important! selected date is stored at UTC midnight
    return formatter.format(Date(millis))
}