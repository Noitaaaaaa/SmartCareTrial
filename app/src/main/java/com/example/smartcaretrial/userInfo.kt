package com.example.smartcaretrial

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val specialty: String = "",
    val role: String = "Patient",
    val Birthdate: String = "",
    val gender: String = "",
    val contactNumber: String = "",
    val emergencyNumber: String = "",
    val securityQuestion: String = "",
    val securityAnswer: String = ""

)

