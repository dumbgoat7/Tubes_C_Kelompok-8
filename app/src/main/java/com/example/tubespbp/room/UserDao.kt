package com.example.tubespbp.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun addUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM user")
    fun getUsers() : List<User>

    @Query("SELECT * FROM user WHERE id =:note_id")
    fun getUser(note_id: Int) : List<User>

    @Query("SELECT * FROM User WHERE username = :username AND password = :password")
    fun getLogin(username: String, password: String): User?
}