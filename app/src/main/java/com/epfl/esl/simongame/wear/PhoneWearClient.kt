package com.epfl.esl.simongame.wear

import android.content.Context
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhoneWearClient(private val context: Context) {

    suspend fun sendStartCapture(turnId: Long, durationMs: Long) {
        val payload = WearJson.encodeStartCapture(StartCaptureRequest(turnId, durationMs))
        sendToAllNodes(WearPaths.START_CAPTURE, payload)
    }

    private suspend fun sendToAllNodes(path: String, payload: ByteArray) = withContext(Dispatchers.IO) {
        val nodes = Tasks.await(Wearable.getNodeClient(context).connectedNodes)
        val msgClient = Wearable.getMessageClient(context)
        for (n in nodes) {
            Tasks.await(msgClient.sendMessage(n.id, path, payload))
        }
    }
}
