package com.epfl.esl.simongame.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epfl.esl.simongame.data.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface AuthState {
    data object Loading : AuthState
    data object Unregistered : AuthState
    data object Registered : AuthState
}

class AuthViewModel(private val repo: UserRepository) : ViewModel() {

    val state: StateFlow<AuthState> = repo.currentUser
        .map { user -> if (user == null) AuthState.Unregistered else AuthState.Registered }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AuthState.Loading)

    fun register(username: String, email: String) {
        viewModelScope.launch { repo.register(username, email) }
    }

    fun logout() {
        viewModelScope.launch { repo.logout() }
    }
}
