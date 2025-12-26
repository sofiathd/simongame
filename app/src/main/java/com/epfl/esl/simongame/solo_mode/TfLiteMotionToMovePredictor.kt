package com.epfl.esl.simongame.solo_mode

import android.content.Context
import com.epfl.esl.simongame.game.Move
import com.epfl.esl.simongame.wear.MotionWindow
import com.epfl.esl.simongame.wear.Vec3Sample
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class TfLiteMotionToMovePredictor(
    context: Context,
    private val spec: ModelSpec
) : MotionToMovePredictor {

    private val interpreter: Interpreter
    private val T: Int
    private val C: Int

    init {
        interpreter = Interpreter(loadModel(context, spec.assetName), Interpreter.Options().setNumThreads(2))

        val shape = interpreter.getInputTensor(0).shape()
        require(shape.size == 3 && shape[0] == 1) {
            "Model input must be rank-3 [1, T, C]. Got ${shape.contentToString()}"
        }
        T = shape[1]
        C = shape[2]

        // Validate mapping vs model channels
        val expectedC = when (spec.channelMapping) {
            ChannelMapping.ACCEL_3 -> 3
            ChannelMapping.ACCEL_GYRO_6 -> 6
        }
        require(C == expectedC) {
            "Model expects C=$C channels, but channelMapping expects $expectedC. Fix mapping or retrain/export."
        }
    }

    override fun predict(window: MotionWindow): Move {
        val accel = resample(window.accel, T) // T x 3
        val gyro  = resample(window.gyro, T)  // T x 3

        val input = ByteBuffer
            .allocateDirect(4 * 1 * T * C)
            .order(ByteOrder.nativeOrder())

        for (t in 0 until T) {
            when (spec.channelMapping) {
                ChannelMapping.ACCEL_3 -> {
                    input.putFloat(accel[t][0])
                    input.putFloat(accel[t][1])
                    input.putFloat(accel[t][2])
                }
                ChannelMapping.ACCEL_GYRO_6 -> {
                    input.putFloat(accel[t][0])
                    input.putFloat(accel[t][1])
                    input.putFloat(accel[t][2])
                    input.putFloat(gyro[t][0])
                    input.putFloat(gyro[t][1])
                    input.putFloat(gyro[t][2])
                }
            }
        }
        input.rewind()

        val out = Array(1) { FloatArray(spec.labels.size) }
        interpreter.run(input, out)

        val scores = out[0]
        val bestIdx = scores.indices.maxByOrNull { scores[it] } ?: 0
        return spec.labels.getOrElse(bestIdx) { Move.IDLE }
    }

    private fun loadModel(context: Context, assetName: String): MappedByteBuffer {
        context.assets.openFd(assetName).use { fd ->
            FileInputStream(fd.fileDescriptor).channel.use { ch ->
                return ch.map(FileChannel.MapMode.READ_ONLY, fd.startOffset, fd.declaredLength)
            }
        }
    }

    private fun resample(samples: List<Vec3Sample>, T: Int): Array<FloatArray> {
        if (samples.isEmpty()) return Array(T) { floatArrayOf(0f, 0f, 0f) }

        val sorted = samples.sortedBy { it.tMs }
        val t0 = sorted.first().tMs
        val t1 = sorted.last().tMs

        if (t1 == t0) {
            val s = sorted.first()
            return Array(T) { floatArrayOf(s.x, s.y, s.z) }
        }

        val out = Array(T) { floatArrayOf(0f, 0f, 0f) }
        var j = 0

        for (i in 0 until T) {
            val target = t0 + (i.toDouble() / (T - 1) * (t1 - t0)).toLong()
            while (j + 1 < sorted.size && sorted[j + 1].tMs <= target) j++
            val s = sorted[j]
            out[i][0] = s.x
            out[i][1] = s.y
            out[i][2] = s.z
        }
        return out
    }
}
