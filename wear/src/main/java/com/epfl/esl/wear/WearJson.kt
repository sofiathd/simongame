package com.epfl.esl.simongame.wear

import org.json.JSONArray
import org.json.JSONObject

object WearJson {
    fun decodeStartCapture(bytes: ByteArray): StartCaptureRequest {
        val o = JSONObject(bytes.toString(Charsets.UTF_8))
        return StartCaptureRequest(
            turnId = o.getLong("turnId"),
            durationMs = o.getLong("durationMs")
        )
    }

    fun encodeMotionWindow(w: MotionWindow): ByteArray {
        val o = JSONObject()
        o.put("turnId", w.turnId)
        o.put("accel", encodeSamples(w.accel))
        o.put("gyro", encodeSamples(w.gyro))
        return o.toString().toByteArray(Charsets.UTF_8)
    }

    private fun encodeSamples(samples: List<Vec3Sample>): JSONArray {
        val arr = JSONArray()
        for (s in samples) {
            val o = JSONObject()
            o.put("tMs", s.tMs)
            o.put("x", s.x)
            o.put("y", s.y)
            o.put("z", s.z)
            arr.put(o)
        }
        return arr
    }
}
