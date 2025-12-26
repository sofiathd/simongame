package com.epfl.esl.simongame.home


import androidx.lifecycle.ViewModel
import com.epfl.esl.simongame.game.GameResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class HomeUiState(
    val bestSoloScore: Int = 0,
    val lastSoloRounds: Int? = null,
    val lastSoloMistakes: Int? = null
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    fun onSoloGameFinished(result: GameResult) {
        _uiState.update { current ->
            val newBest = maxOf(current.bestSoloScore, result.finalScore)
            current.copy(
                bestSoloScore = newBest,
                lastSoloRounds = result.totalRounds,
                lastSoloMistakes = result.mistakes
            )
        }
    }
}
