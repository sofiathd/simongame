package com.epfl.esl.simongame.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID

private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class LocalUserRepository(private val appContext: Context) : UserRepository {

    private object Keys {
        val USER_ID = stringPreferencesKey("user_id")
        val USERNAME = stringPreferencesKey("username")
        val EMAIL = stringPreferencesKey("email")
        val FRIENDS = stringSetPreferencesKey("friends")
    }

    // we'll replace with firebase later
    private val builtInDirectory: List<UserProfile> = listOf(
        UserProfile("u_alex", "Alex", "alex@example.com", emptyList()),
        UserProfile("u_sam", "Sam", "sam@example.com", emptyList()),
        UserProfile("u_nora", "Nora", "nora@example.com", emptyList()),
        UserProfile("u_yuki", "Yuki", "yuki@example.com", emptyList())
    )

    override val directoryUsers: Flow<List<UserProfile>> = flowOf(builtInDirectory)

    override val currentUser: Flow<UserProfile?> =
        appContext.userDataStore.data.map { prefs ->
            val id = prefs[Keys.USER_ID] ?: return@map null
            UserProfile(
                id = id,
                username = prefs[Keys.USERNAME].orEmpty(),
                email = prefs[Keys.EMAIL].orEmpty(),
                friends = prefs[Keys.FRIENDS]?.toList().orEmpty()
            )
        }.distinctUntilChanged()

    override suspend fun register(username: String, email: String) {
        appContext.userDataStore.edit { prefs ->
            val id = prefs[Keys.USER_ID] ?: "local_${UUID.randomUUID()}"
            prefs[Keys.USER_ID] = id
            prefs[Keys.USERNAME] = username.trim()
            prefs[Keys.EMAIL] = email.trim()
            if (prefs[Keys.FRIENDS] == null) prefs[Keys.FRIENDS] = emptySet()
        }
    }

    override suspend fun logout() {
        appContext.userDataStore.edit { it.clear() }
    }

    override suspend fun addFriend(friendId: String) {
        appContext.userDataStore.edit { prefs ->
            val cur = prefs[Keys.FRIENDS] ?: emptySet()
            prefs[Keys.FRIENDS] = (cur + friendId)
        }
    }
}
