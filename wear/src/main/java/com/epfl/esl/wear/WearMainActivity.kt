package com.epfl.esl.simongame.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WearMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            sendHello()
        }

    }

    private suspend fun sendHello() = withContext(Dispatchers.IO) {
        val nodes = Tasks.await(Wearable.getNodeClient(this@WearMainActivity).connectedNodes)
        val msgClient = Wearable.getMessageClient(this@WearMainActivity)
        for (n in nodes) {
            Tasks.await(msgClient.sendMessage(n.id, WearPaths.HELLO, byteArrayOf()))
        }
    }
}
