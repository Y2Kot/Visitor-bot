package com.example.photocutter

import java.security.InvalidParameterException
import kotlin.math.ceil
import kotlin.math.exp

class PriorBox(private val imgH: Int, private val imgW: Int)
{
    // class used for network results' postprocessing

    private val priors: Array<FloatArray>

    init {
        priors = generatePriors()
    }

    private fun generatePriors(): Array<FloatArray> {
        val anchors = mutableListOf<FloatArray>()
        for (idx in steps.indices) {
            val sizes = minSizes[idx]
            val step = steps[idx]

            val nH = ceil(imgH.toDouble() / step).toInt()
            val nW = ceil(imgW.toDouble() / step).toInt()
            for (i in 0 until nH)
                for (j in 0 until nW)
                    for (minSize in sizes) {
                        val s_kx = minSize.toFloat() / imgW
                        val s_ky = minSize.toFloat() / imgH

                        val cx = (j + 0.5f) * step / imgW
                        val cy = (i + 0.5f) * step / imgH
                        anchors.add(floatArrayOf(cx, cy, s_kx, s_ky))
                    }
        }
        return anchors.toTypedArray()
    }

    fun decodeBoxes(netOutput: Array<FloatArray>) {
        if (netOutput.size != priors.size)
            throw InvalidParameterException("can't decode boxes: sizes are not matched: " +
                    "${netOutput.size} (actual) != ${priors.size} (expected)")

        for (i in netOutput.indices) {
            // 0 - score, 1..4 - box
            for (idx in 0..1)
                netOutput[i][idx+1] = priors[i][idx] + netOutput[i][idx+1] * variance[0] * priors[i][idx+2]
            for (idx in 2..3)
                netOutput[i][idx+1] = priors[i][idx] * exp(netOutput[i][idx+1] * variance[1])

            for (idx in 0..1)
                netOutput[i][idx+1] -= netOutput[i][idx+1+2] / 2
            for (idx in 2..3)
                netOutput[i][idx+1] += netOutput[i][idx+1-2]
        }

    }


    companion object {
        // note: constants are strictly defined by pretrained model's configuration
        private val minSizes = arrayOf(intArrayOf(16, 32), intArrayOf(64, 128), intArrayOf(256, 512))
        private val steps = intArrayOf(8, 16, 32)
        private val variance = floatArrayOf(0.1f, 0.2f)
    }
}