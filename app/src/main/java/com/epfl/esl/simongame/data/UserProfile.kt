package com.epfl.esl.simongame.data

data class UserProfile(
    val id: String,
    val username: String,
    val email: String,
    val friends: List<String>
)
