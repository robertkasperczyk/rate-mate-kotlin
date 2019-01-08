package pl.edu.uj.ratemate.utils

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

@Lazy
@Service
class ImageSaver {

    fun saveImage(id: Int, bytes: ByteArray) {
        val dirPath = Paths.get("./upload")
        if (Files.notExists(dirPath)) {
            Files.createDirectories(dirPath)
        }

        val fullPath = dirPath.resolve(String.format("%d.jpg", id))

        Files.deleteIfExists(fullPath)

        Files.createFile(fullPath)

        val input = ByteArrayInputStream(bytes)
        val size = 500

        try {
            val result: BufferedImage
            val img = ImageIO.read(input)

            result = if (img.height == img.width) {
                scaleImage(img, size, img.height / img.width * size)
            } else if (img.height < size && img.width < size) {
                img
            } else if (img.height < size || img.width < size) {
                cropImage(img, Math.min(img.height, img.width))
            } else {
                val temp: BufferedImage = if (img.width > img.height) {
                    scaleImage(img, (img.width.toDouble() / img.height.toDouble() * size).toInt(), size)
                } else {
                    scaleImage(img, size, img.height / img.width * size)
                }
                cropImage(temp, size)
            }

            val buffer = ByteArrayOutputStream()
            ImageIO.write(result, "jpg", buffer)

            Files.write(fullPath, buffer.toByteArray())
        } catch (e: IOException) {
            throw RuntimeException("IOException in scale")
        }
    }

    private fun cropImage(img: BufferedImage?, size: Int): BufferedImage {
        return img?.getSubimage((img.width - size) / 2, (img.height - size) / 2, size, size)!!
    }

    private fun scaleImage(img: BufferedImage, width: Int, height: Int): BufferedImage {
        val scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH)
        val result = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        result.graphics.drawImage(scaledImage, 0, 0, Color(0, 0, 0), null)
        return result
    }
}