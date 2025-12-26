package com.epfl.esl.simongame.wear

import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class WearListenerService : WearableListenerService() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val capture by lazy { MotionCaptureManager(this) }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        when (messageEvent.path) {
            WearPaths.START_CAPTURE -> {
                val req = WearJson.decodeStartCapture(messageEvent.data)

                scope.launch {
                    val (accel, gyro) = capture.captureWindow(req.durationMs)
                    val window = MotionWindow(turnId = req.turnId, accel = accel, gyro = gyro)
                    sendMotionWindow(window)
                }
            }
        }
    }

    private fun sendMotionWindow(w: MotionWindow) {
        val payload = WearJson.encodeMotionWindow(w)

        val nodeClient = Wearable.getNodeClient(this)
        val msgClient = Wearable.getMessageClient(this)

        // blocking inside background coroutine thread is OK here
        val nodes = Tasks.await(nodeClient.connectedNodes)
        for (n in nodes) {
            Tasks.await(msgClient.sendMessage(n.id, WearPaths.MOTION_WINDOW, payload))
        }
    }
}
