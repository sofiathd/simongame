package com.epfl.esl.simongame.game

// what should return the ML model
// (on doit changer, mettre que les move qu'on compte utiliser)
enum class Move {
    JUMP, CLAP, SPIN, TOUCH_NOSE, HANDS_UP, SIT, STAND, IDLE
}

data class Instruction(
    val text: String,
    val isSimonSays: Boolean,
    val expectedMove: Move
)

data class GameConfig(
    val maxMistakes: Int = 3,
    val moveTexts: Map<Move, String> = mapOf(
        Move.JUMP to "jump",
        Move.CLAP to "clap your hands",
        Move.SPIN to "spin around",
        Move.TOUCH_NOSE to "touch your nose",
        Move.HANDS_UP to "raise your hands",
        Move.SIT to "sit down",
        Move.STAND to "stand up",
    )
)

data class GameState(
    val score: Int = 0,
    val mistakes: Int = 0,
    val currentInstruction: Instruction? = null,
    val isGameOver: Boolean = false,
    val round: Int = 0
)

data class GameResult(
    val finalScore: Int,
    val totalRounds: Int,
    val mistakes: Int
)