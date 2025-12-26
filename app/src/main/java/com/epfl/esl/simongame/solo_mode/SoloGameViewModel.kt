package com.epfl.esl.simongame.solo_mode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epfl.esl.simongame.game.GameResult
import com.epfl.esl.simongame.game.Move
import com.epfl.esl.simongame.game.SimonSaysEngine
import com.epfl.esl.simongame.wear.MotionWindowRepository
import com.epfl.esl.simongame.wear.PhoneWearClient
import com.epfl.esl.simongame.wear.WearConnectionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

data class SoloGameUiState(
    val instructionText: String = "",
    val round: Int = 0,
    val score: Int = 0,
    val mistakes: Int = 0,
    val timeLeftSec: Int = 0,

    val isWatchConnected: Boolean = false,
    val isCapturing: Boolean = false,

    val isGameOver: Boolean = false,
    val lastAnswerCorrect: Boolean? = null,
    val gameResult: GameResult? = null,
)

class SoloGameViewModel(
    private val engine: SimonSaysEngine,
    private val wearConnectionRepository: WearConnectionRepository,
    private val motionWindowRepository: MotionWindowRepository,
    private val phoneWearClient: PhoneWearClient,
    private val predictor: MotionToMovePredictor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SoloGameUiState())
    val uiState: StateFlow<SoloGameUiState> = _uiState

    private var gameLoopJob: Job? = null
    private var startRequested = false

    private val captureDurationMs = 2500L       // how long the watch records for each round
    private val betweenRoundsDelayMs = 600L     // short pause after showing result
    private val extraTimeoutMs = 1500L          // extra wait

    init {
        // Keep UI updated with watch connection
        viewModelScope.launch {
            wearConnectionRepository.isConnected.collect { connected ->
                _uiState.update { it.copy(isWatchConnected = connected) }
                // If user pressed Start while disconnected, auto-start once connected
                if (connected && startRequested && gameLoopJob == null && !_uiState.value.isGameOver) {
                    startGameInternal()
                }
            }
        }
    }

    fun startGame() {
        startRequested = true
        if (_uiState.value.isWatchConnected) {
            startGameInternal()
        } else {
            // show waiting state; user can connect watch then it auto-starts
            _uiState.update {
                it.copy(
                    instructionText = "CONNECT YOUR WATCH",
                    timeLeftSec = 0,
                    isCapturing = false,
                    lastAnswerCorrect = null,
                    gameResult = null,
                    isGameOver = false
                )
            }
        }
    }

    fun onEndGame() {
        engine.endNow()
        gameLoopJob?.cancel()
        gameLoopJob = null

        val s = engine.state
        _uiState.update {
            it.copy(
                instructionText = s.currentInstruction?.text.orEmpty(),
                round = s.round,
                score = s.score,
                mistakes = s.mistakes,
                isCapturing = false,
                isGameOver = true,
                lastAnswerCorrect = null,
                gameResult = engine.toResult()
            )
        }
    }

    private fun startGameInternal() {
        gameLoopJob?.cancel()
        engine.startNewGame()

        _uiState.update {
            it.copy(
                instructionText = engine.state.currentInstruction?.text.orEmpty(),
                round = engine.state.round,
                score = engine.state.score,
                mistakes = engine.state.mistakes,
                timeLeftSec = (captureDurationMs / 1000L).toInt(),
                isCapturing = false,
                isGameOver = false,
                lastAnswerCorrect = null,
                gameResult = null
            )
        }

        gameLoopJob = viewModelScope.launch {
            while (true) {
                if (_uiState.value.isGameOver) break
                if (!_uiState.value.isWatchConnected) {
                    // if watch disconnects mid game stop loop but keep state
                    _uiState.update { it.copy(isCapturing = false, instructionText = "WATCH DISCONNECTED") }
                    gameLoopJob = null
                    break
                }

                val instruction = engine.state.currentInstruction
                if (instruction == null) {
                    engine.generateNextInstruction()
                }

                // Show instruction (YourTurn)
                _uiState.update {
                    it.copy(
                        instructionText = engine.state.currentInstruction?.text.orEmpty(),
                        round = engine.state.round,
                        score = engine.state.score,
                        mistakes = engine.state.mistakes,
                        lastAnswerCorrect = null,
                        isCapturing = false,
                        timeLeftSec = (captureDurationMs / 1000L).toInt()
                    )
                }

                // Start a timer that updates UI once/sec during capture
                val timerJob = launch {
                    val totalSec = (captureDurationMs / 1000L).toInt()
                    for (t in totalSec downTo 0) {
                        _uiState.update { it.copy(timeLeftSec = t) }
                        delay(1000L)
                    }
                }

                // Capture + evaluate (Checking)
                val turnId = System.currentTimeMillis()
                _uiState.update { it.copy(isCapturing = true) }

                // Tell watch to start recording
                phoneWearClient.sendStartCapture(turnId = turnId, durationMs = captureDurationMs)

                // Wait for the corresponding motion window
                val window = withTimeoutOrNull(captureDurationMs + extraTimeoutMs) {
                    motionWindowRepository.windows.first { it.turnId == turnId }
                }

                timerJob.cancel()

                val predictedMove: Move = if (window == null) Move.IDLE else predictor.predict(window)
                val correct = engine.answer(predictedMove)
                val s = engine.state

                _uiState.update {
                    it.copy(
                        instructionText = s.currentInstruction?.text.orEmpty(),
                        round = s.round,
                        score = s.score,
                        mistakes = s.mistakes,
                        isCapturing = false,
                        lastAnswerCorrect = correct,
                        isGameOver = s.isGameOver,
                        gameResult = if (s.isGameOver) engine.toResult() else null
                    )
                }

                if (s.isGameOver) {
                    gameLoopJob = null
                    break
                }

                // next instruction
                delay(betweenRoundsDelayMs)
                engine.generateNextInstruction()
            }
        }
    }
}
