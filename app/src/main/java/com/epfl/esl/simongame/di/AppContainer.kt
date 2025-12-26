package com.epfl.esl.simongame.di

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf
import com.epfl.esl.simongame.data.LocalUserRepository
import com.epfl.esl.simongame.data.UserRepository
import com.epfl.esl.simongame.game.Move
import com.epfl.esl.simongame.solo_mode.ChannelMapping
import com.epfl.esl.simongame.solo_mode.ModelSpec
import com.epfl.esl.simongame.wear.MotionWindowRepository
import com.epfl.esl.simongame.wear.PhoneWearClient
import com.epfl.esl.simongame.wear.WearConnectionRepository
import com.epfl.esl.simongame.solo_mode.MotionToMovePredictor
import com.epfl.esl.simongame.solo_mode.TfLiteMotionToMovePredictor
import android.util.Log
import com.epfl.esl.simongame.solo_mode.SimpleEnergyMovePredictor

class AppContainer(context: Context) {
    val userRepository: UserRepository = LocalUserRepository(context)

    val wearConnectionRepository = WearConnectionRepository()
    val motionWindowRepository = MotionWindowRepository()
    val phoneWearClient = PhoneWearClient(context.applicationContext)

    val motionToMovePredictor: MotionToMovePredictor by lazy {
        try {
            TfLiteMotionToMovePredictor(
                context.applicationContext,
                ModelSpec(
                    assetName = "move_classifier.tflite",
                    channelMapping = ChannelMapping.ACCEL_GYRO_6,
                    labels = listOf(
                        Move.JUMP,
                        Move.CLAP,
                        Move.SPIN,
                        Move.TOUCH_NOSE,
                        Move.HANDS_UP,
                        Move.SIT,
                        Move.STAND,
                        Move.IDLE
                    )
                )
            )
        } catch (e: Throwable) {
            Log.e("ML", "Failed to init TFLite predictor, falling back to mock.", e)
            SimpleEnergyMovePredictor()
        }
    }


}

val LocalAppContainer = staticCompositionLocalOf<AppContainer> {
    error("AppContainer missing. Provide it in SimonSaysApp().")
}
