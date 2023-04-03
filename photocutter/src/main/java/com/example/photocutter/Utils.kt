package com.example.photocutter

import kotlin.math.max
import kotlin.math.min

fun nms(data: Array<FloatArray>, nmsThreshold: Float = 0.4f): Array<FloatArray> {
    // NMS - non-maximum suppression
    val x1 = 1;
    val y1 = 2;
    val x2 = 3;
    val y2 = 4
    var order = data.indices.toList()

    // intersection over union
    fun getIOU(i: Int, j: Int): Float {
        val xx1 = max(data[i][x1], data[j][x1])
        val yy1 = max(data[i][y1], data[j][y1])
        val xx2 = min(data[i][x2], data[j][x2])
        val yy2 = min(data[i][y2], data[j][y2])

        val w = max(0.0f, xx2 - xx1 + 1)
        val h = max(0.0f, yy2 - yy1 + 1)
        val inter = w * h

        val area1 = (data[i][x2] - data[i][x1]) * (data[i][y2] - data[i][y1])
        val area2 = (data[j][x2] - data[j][x1]) * (data[j][y2] - data[j][y1])
        val iou = inter / (area1 + area2 - inter)
        return iou
    }

    val result = emptyList<FloatArray>().toMutableList()
    while (order.isNotEmpty()) {
        val newOrder = emptyList<Int>().toMutableList()

        val i = order[0]
        result.add(data[i])
        for (idx in 1 until order.size) {
            val j = order[idx]
            val iou = getIOU(i, j)
            if (iou <= nmsThreshold)
                newOrder.add(j)
        }
        order = newOrder
    }

    return result.toTypedArray()
}
