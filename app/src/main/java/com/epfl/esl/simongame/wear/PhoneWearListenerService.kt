package com.epfl.esl.simongame.wear

import com.epfl.esl.simongame.SimonGameApplication
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

class PhoneWearListenerService : WearableListenerService() {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        val container = (application as SimonGameApplication).container
        when (messageEvent.path) {
            WearPaths.HELLO -> {
                container.wearConnectionRepository.setConnected(true)
            }
            WearPaths.MOTION_WINDOW -> {
                val w = WearJson.decodeMotionWindow(messageEvent.data)
                container.motionWindowRepository.emit(w)
            }
        }
    }
}
