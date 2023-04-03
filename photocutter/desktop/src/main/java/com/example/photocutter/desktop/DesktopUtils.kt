package com.example.photocutter.desktop

import com.example.photocutter.BoundingBox
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.nio.FloatBuffer
import kotlin.math.floor
import kotlin.math.sqrt


fun convertBufferedImageToFloatBuffer(img: BufferedImage): FloatBuffer {
    val w = img.width
    val h = img.height
    val imgData = FloatBuffer.allocate(1 * 3 * w * h)

    for (color in 0 until 3) {
        for (i in 0 until h)
            for (j in 0 until w) {
                var value = img.getRGB(j, i)
                if (color == 0)
                    imgData.put((value shr 16 and 0xFF).toFloat() - 123)        // вычитания - препроцессинг оригинальной модели
                else if (color == 1)
                    imgData.put((value shr 8 and 0xFF).toFloat() - 117)
                else
                    imgData.put((value and 0xFF).toFloat() - 104)
            }
    }

    return imgData
}

fun limitBufferedImageSize(image: BufferedImage, maxPixels: Long): BufferedImage {
    // proportionally decrease image size if total count of pixels is bigger than <maxPixels>

    val w = image.width; val h = image.height
    if (w * h < maxPixels)
        return image

    // proportionally decrease image size
    val coef = sqrt(maxPixels.toDouble() / (w * h))
    val newWidth = floor(w*coef).toInt()
    val newHeight = floor(h*coef).toInt()

    // resize itself
    val toolkitImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)
    val width: Int = toolkitImage.getWidth(null)
    val height: Int = toolkitImage.getHeight(null)

    val newImage = BufferedImage(
        width, height,
        BufferedImage.TYPE_INT_RGB
    )
    val g = newImage.graphics
    g.drawImage(toolkitImage, 0, 0, null)
    g.dispose()

    return newImage
}