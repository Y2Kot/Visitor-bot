package ru.kudryavtsev.domain

import com.example.photocutter.desktop.DesktopFaceDetectionService
import com.example.photocutter.desktop.FaceFramesDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.kudryavtsev.domain.model.UserInfo
import java.io.File
import java.util.Collections
import javax.imageio.ImageIO

class FaceCountService(
    private val faceDetectionService: DesktopFaceDetectionService,
    private val drawer: FaceFramesDrawer,
    private val scope: CoroutineScope
) {
    private val requests = Collections.synchronizedList(mutableListOf<UserInfo>())

    fun addRequest(userInfo: UserInfo, image: File, result: (Int, File) -> Unit) {
        scope.launch {
            requests += userInfo
            val bufferedImage = ImageIO.read(image)
            val boxes = faceDetectionService.getAllFacesBoundingBoxes(bufferedImage)
            val resultImage = kotlin.io.path.createTempFile(suffix = ".${image.extension}").toFile()
            drawer.drawFrames(image.absolutePath, resultImage.absolutePath, boxes)
            result(boxes.size, resultImage)
            requests -= userInfo
        }
    }

    fun isRequestExist(userInfo: UserInfo): Boolean = userInfo in requests
}
