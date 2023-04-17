package com.example.photocutter.desktop

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import com.example.photocutter.BaseFaceDetectionService
import com.example.photocutter.BoundingBox
import java.awt.image.BufferedImage

class DesktopFaceDetectionService(modelPath: String): BaseFaceDetectionService() {
    private val ortEnvironment: OrtEnvironment
    private val ortSession: OrtSession

    init {
        ortEnvironment = OrtEnvironment.getEnvironment()
        ortSession = ortEnvironment.createSession(modelPath, OrtSession.SessionOptions())
        println(ortSession)
    }

    fun getFaceBoundingBox(image: BufferedImage): BoundingBox? {
        // get a single face with best score (if any is over the threshold)
        val result = runModel(image, FACE_MODE.SINGLE)

        if (result.isNotEmpty()) {
            return getBoundingBox(result[0])
        }
        return null
    }

    @Synchronized
    fun getAllFacesBoundingBoxes(image: BufferedImage): List<BoundingBox> {
        // get all faces found
        val result = runModel(image, FACE_MODE.MULTI)

        if (result.isNotEmpty()) {
            return result.map{x -> getBoundingBox(x)}.toList()
        }
        return emptyList()
    }

    private fun runModel(image: BufferedImage, mode: FACE_MODE = FACE_MODE.SINGLE): Array<FloatArray> {
        val inputName = ortSession.inputNames?.iterator()?.next()

        val limitedImg = limitBufferedImageSize(image, MAX_IMAGE_PIXELS)
        val input = convertBufferedImageToFloatBuffer(limitedImg)
        input.rewind()
        val inputTensor = OnnxTensor.createTensor(
            ortEnvironment, input,
            longArrayOf(1, 3, limitedImg.height.toLong(), limitedImg.width.toLong())
        )

        val results = ortSession.run(mapOf(inputName to inputTensor))

        val output = results[0].value as Array<FloatArray>

        // calculate on limited bitmap, but return actual sizes
        val result = postprocess(output, limitedImg.width, limitedImg.height,
            image.width, image.height, mode)
        return result
    }

}
