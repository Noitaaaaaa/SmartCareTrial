package com.example.smartcaretrial

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


data class doctorDirectory (
    val id: Int,
    val firstName: String,
    val lastName: String,
    val specialty: String,
    val email: String,
)

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: UserInfo)

    @Update
    suspend fun updateUser(user: UserInfo)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserInfo?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserInfo?

    @Query("""
        SELECT id, firstName, lastName, specialty, email 
        FROM users 
        WHERE role = 'Doctor'
    """)
    fun getDoctorDirectory(): Flow<List<doctorDirectory>>
}







