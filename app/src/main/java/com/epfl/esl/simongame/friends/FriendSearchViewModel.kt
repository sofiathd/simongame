package com.epfl.esl.simongame.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epfl.esl.simongame.data.UserProfile
import com.epfl.esl.simongame.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FriendSearchUiState(
    val query: String = "",
    val isLoading: Boolean = true,
    val currentUserId: String? = null,
    val currentFriendIds: Set<String> = emptySet(),
    val results: List<UserProfile> = emptyList(),
    val error: String? = null
)

class FriendSearchViewModel(
    private val repo: UserRepository
) : ViewModel() {

    private val queryFlow = MutableStateFlow("")

    val uiState: StateFlow<FriendSearchUiState> =
        combine(repo.currentUser, repo.directoryUsers, queryFlow) { me, directory, query ->
            val meId = me?.id
            val myFriendIds = me?.friends?.toSet() ?: emptySet()

            val q = query.trim().lowercase()

            val filtered = directory
                .asSequence()
                .filter { u -> meId == null || u.id != meId }        // hide yourself
                .filter { u -> u.id !in myFriendIds }               // hide already-friends
                .filter { u ->
                    if (q.isBlank()) true else {
                        u.username.lowercase().contains(q) ||
                                u.email.lowercase().contains(q) ||
                                u.id.lowercase().contains(q)
                    }
                }
                .toList()

            FriendSearchUiState(
                query = query,
                isLoading = false,
                currentUserId = meId,
                currentFriendIds = myFriendIds,
                results = filtered,
                error = null
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            FriendSearchUiState()
        )

    fun onQueryChange(newValue: String) {
        queryFlow.value = newValue
    }

    fun addFriend(friendId: String) {
        viewModelScope.launch {
            repo.addFriend(friendId)
        }
    }
}
