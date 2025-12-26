package com.epfl.esl.simongame.wear

object WearPaths {
    const val HELLO = "/simonsays/hello"
    const val START_CAPTURE = "/simonsays/start_capture"
    const val MOTION_WINDOW = "/simonsays/motion_window"
}

data class StartCaptureRequest(val turnId: Long, val durationMs: Long)

data class Vec3Sample(val tMs: Long, val x: Float, val y: Float, val z: Float)

data class MotionWindow(
    val turnId: Long,
    val accel: List<Vec3Sample>,
    val gyro: List<Vec3Sample>
)
