package com.example.photocutter

class BoundingBox(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
) {
    override fun toString(): String {
        return "BoundingBox(left=$left, top=$top, right=$right, bottom=$bottom)"
    }

    fun enlarge(enlargeBoundingBoxValue: Double): BoundingBox {
        val enlargeForMin = 1 - enlargeBoundingBoxValue
        val enlargeForMax = 1 + enlargeBoundingBoxValue

        return BoundingBox(
            left = (left * enlargeForMin).toInt(),
            top = (top * enlargeForMin).toInt(),
            right = (right * enlargeForMax).toInt(),
            bottom = (bottom * enlargeForMax).toInt()
        )
    }

    fun enlarge(enlargeCoefSet: DoubleArray): BoundingBox {
        return BoundingBox(
            left = (left * (1-enlargeCoefSet[0])).toInt(),
            top = (top * (1-enlargeCoefSet[1])).toInt(),
            right = (right * (1+enlargeCoefSet[2])).toInt(),
            bottom = (bottom * (1+enlargeCoefSet[3])).toInt()
        )
    }

    fun validate(xMin: Int, yMin: Int, xMax: Int, yMax: Int): BoundingBox =
        BoundingBox(
            left = if (left < xMin) xMin else left,
            top = if (top < yMin) yMin else top,
            right = if (right > xMax) xMax else right,
            bottom = if (bottom > yMax) yMax else bottom
        )

}
