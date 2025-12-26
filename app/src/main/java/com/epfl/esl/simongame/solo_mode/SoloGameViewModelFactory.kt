package com.epfl.esl.simongame.solo_mode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.epfl.esl.simongame.game.SimonSaysEngine
import com.epfl.esl.simongame.wear.MotionWindowRepository
import com.epfl.esl.simongame.wear.PhoneWearClient
import com.epfl.esl.simongame.wear.WearConnectionRepository

class SoloGameViewModelFactory(
    private val wearConnectionRepository: WearConnectionRepository,
    private val motionWindowRepository: MotionWindowRepository,
    private val phoneWearClient: PhoneWearClient,
    private val predictor: MotionToMovePredictor,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SoloGameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SoloGameViewModel(
                engine = SimonSaysEngine(),
                wearConnectionRepository = wearConnectionRepository,
                motionWindowRepository = motionWindowRepository,
                phoneWearClient = phoneWearClient,
                predictor = predictor // ✅
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}

