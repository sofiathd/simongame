package com.epfl.esl.simongame.solo_mode

import com.epfl.esl.simongame.game.Move
import com.epfl.esl.simongame.wear.MotionWindow
import kotlin.math.sqrt

interface MotionToMovePredictor {
    fun predict(window: MotionWindow): Move
}

/**
 * Mock “ML” for now:
 * - compute average accel magnitude
 * - below threshold => IDLE
 * - above threshold => random active move
 */
class SimpleEnergyMovePredictor(
    private val idleThreshold: Float = 2.0f
) : MotionToMovePredictor {

    override fun predict(window: MotionWindow): Move {
        val e = avgMagnitude(window.accel)
        if (e < idleThreshold) return Move.IDLE

        val activeMoves = listOf(
            Move.JUMP, Move.CLAP, Move.SPIN, Move.TOUCH_NOSE,
            Move.HANDS_UP, Move.SIT, Move.STAND
        )
        return activeMoves.random()
    }

    private fun avgMagnitude(samples: List<com.epfl.esl.simongame.wear.Vec3Sample>): Float {
        if (samples.isEmpty()) return 0f
        var sum = 0f
        for (s in samples) sum += sqrt(s.x * s.x + s.y * s.y + s.z * s.z)
        return sum / samples.size
    }
}
