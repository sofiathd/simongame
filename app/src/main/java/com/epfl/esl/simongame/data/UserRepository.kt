package com.epfl.esl.simongame.data

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val currentUser: Flow<UserProfile?>
    val directoryUsers: Flow<List<UserProfile>>

    suspend fun register(username: String, email: String)
    suspend fun logout()
    suspend fun addFriend(friendId: String)
}
