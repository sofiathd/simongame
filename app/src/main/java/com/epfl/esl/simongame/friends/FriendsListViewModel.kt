package com.epfl.esl.simongame.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epfl.esl.simongame.data.UserProfile
import com.epfl.esl.simongame.data.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class FriendsListUiState(
    val isLoading: Boolean = true,
    val friends: List<UserProfile> = emptyList()
)

class FriendsListViewModel(
    repo: UserRepository
) : ViewModel() {

    val uiState: StateFlow<FriendsListUiState> =
        combine(repo.currentUser, repo.directoryUsers) { me, directory ->
            val friendIds = me?.friends?.toSet() ?: emptySet()
            val friends = directory.filter { it.id in friendIds }
            FriendsListUiState(
                isLoading = false,
                friends = friends
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            FriendsListUiState()
        )
}
