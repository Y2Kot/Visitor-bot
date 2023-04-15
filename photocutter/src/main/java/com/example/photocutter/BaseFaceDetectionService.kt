package com.example.photocutter

import java.security.InvalidKeyException



open class BaseFaceDetectionService {
    // caching prior boxes, which may take long to calculate
    protected val priorBoxCache: HashMap<Pair<Int, Int>, PriorBox>  // map: (height, width) -> priorbox

    protected enum class FACE_MODE {
        SINGLE, MULTI
    }

    init {
        priorBoxCache = HashMap()
    }

    protected fun clearPriorBoxCache() {
        priorBoxCache.clear()
    }

    protected fun postprocess(
        data: Array<FloatArray>,
        netWidth: Int,
        netHeight: Int,
        originWidth: Int,
        originHeight: Int,
        mode: FACE_MODE
    ): Array<FloatArray> {
        // each unit: 0 - score, 1-4 - bounding box (1,2 - w,h of point, 3,4 - w,h of another point
        // units are sorted by score, 0 has max score

        // step 1 - preprocess bounding boxes - they come in as some raw data, convert into [0..1] width-height values
        val key = Pair(netHeight, netWidth)
        if (!priorBoxCache.contains(key))
            priorBoxCache[key] = PriorBox(netHeight, netWidth)

        val priorBox = priorBoxCache[key] ?: throw InvalidKeyException("failed to get prior box for ($netHeight, $netWidth)")
        priorBox.decodeBoxes(data)


        // step 2 - sort by score (first is the biggest conf) & ignore low confidences
        var data = data.toList()
            .filter { it[0] >= SCORE_THRESHOLD }
            .sortedByDescending { it[0] }
            .toTypedArray()

        // get original boxes
        for (i in 0 until data.size) {
            data[i][1] *= 1.0f * originWidth
            data[i][2] *= 1.0f * originHeight
            data[i][3] *= 1.0f * originWidth
            data[i][4] *= 1.0f * originHeight
        }

        if (mode == FACE_MODE.SINGLE)   // useless to do NMS if taking just one best frame
            return data

        // nms
        data = nms(data, NMS_THRESHOLD)
        return data
    }

    protected fun getBoundingBox(
        record: FloatArray
    ): BoundingBox {
        return BoundingBox(
            left = record[1].toInt(),
            top = record[2].toInt(),
            right = record[3].toInt(),
            bottom = record[4].toInt(),
        )
    }

    companion object {
        protected const val NMS_THRESHOLD = 0.2f
        protected const val SCORE_THRESHOLD = 0.5 //0.65    // network confidence level [0..1]
        @JvmStatic
        protected val MAX_IMAGE_PIXELS = 10500*10000L //2500*2000L // images will be proportionally resized to have <= MAX_IMAGE_PIXELS pixels
    }
}
