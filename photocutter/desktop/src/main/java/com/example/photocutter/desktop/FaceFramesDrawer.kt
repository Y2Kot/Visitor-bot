package com.example.photocutter.desktop

import com.example.photocutter.BoundingBox

import org.bytedeco.opencv.global.opencv_core.*
import org.bytedeco.opencv.global.opencv_imgcodecs.*
import org.bytedeco.opencv.global.opencv_imgproc.*
import org.bytedeco.opencv.opencv_core.*
import org.bytedeco.opencv.opencv_imgproc.*

//import org.opencv.imgproc.Imgproc
//import org.opencv.*
//import org.opencv.core.Mat
//import org.opencv.core.Point
//import org.opencv.core.Scalar
//import org.opencv.imgcodecs.Imgcodecs


class FaceFramesDrawer {
    fun drawFrames(imageFilename: String, outputFilename: String, boxes: List<BoundingBox>) {
        val image: Mat = imread(imageFilename)

        val green = Scalar(71.0, 217.0, 35.0, 0.0)
        val yellow = Scalar(210.0, 217.0, 36.0, 0.0)
        val white = Scalar(255.0, 255.0, 255.0, 0.0)

        val orderedBoxes = sortBboxes(boxes)

        var index = orderedBoxes.size
        for (box in orderedBoxes.reversed()) {  // рисовать сначала дальние, потом ближние, чтобы текст не перекрывался
            val pt1 = Point(box.left, box.top)
            val pt2 = Point(box.right, box.bottom)
            rectangle(image, pt1, pt2, green, 5, LINE_8, 0)
            rectangle(image, pt1, Point(box.left+(box.right-box.left)/3*2, box.top-16), green, -1, LINE_8, 0)
            putText(image, "$index", pt1, FONT_HERSHEY_PLAIN, 1.2, white, 2, LINE_4, false)

            index -= 1
        }

        imwrite(outputFilename, image)
    }

    private fun sortBboxes(boxes: List<BoundingBox>): List<BoundingBox> {
        // разбить на горизонтальные ряды и упорядочить все лица по рядам снизу вверх, внутри ряда - слева направо

        val meanY = {b: BoundingBox -> (b.top + b.bottom) / 2}
        val sortedBoxes = boxes.sortedByDescending(meanY)

        val newBoxes = buildList {
            var i = 0; val n = sortedBoxes.size
            while (i < n) {
                var j = i+1
                while (j < n) {
                    val diff = meanY(sortedBoxes[j]) - meanY(sortedBoxes[j-1])
                    val dh = sortedBoxes[j].bottom - sortedBoxes[j].top

                    if (diff < -dh / 2) // todo validate
                        break
                    j += 1
                }

                val boxesRow = sortedBoxes.subList(i, j).sortedBy { b -> b.left }
                addAll(boxesRow)
                i = j
            }
        }

        return newBoxes
    }
}