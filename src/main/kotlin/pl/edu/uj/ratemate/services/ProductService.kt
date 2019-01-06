package pl.edu.uj.ratemate.services

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import pl.edu.uj.ratemate.dto.ProductDTO
import pl.edu.uj.ratemate.entities.Product
import pl.edu.uj.ratemate.repositories.ProductRepository
import pl.edu.uj.ratemate.row.ProductRow
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO


@Service
class ProductService(private val repository: ProductRepository) {

    @Transactional
    fun saveProduct(file: MultipartFile, name: String, description: String) {
        val product = repository.save(Product(0, name, description, comments = emptyList()))

        saveImage(product.id, file)
    }

    fun saveImage(id: Int, stream: MultipartFile) {
        val dirPath = Paths.get("./upload")
        if (Files.notExists(dirPath)) {
            Files.createDirectories(dirPath)
        }

        val fullPath = dirPath.resolve(String.format("%d.jpg", id))

        Files.deleteIfExists(fullPath)

        Files.createFile(fullPath)

        val bytes = stream.bytes

        val input = ByteArrayInputStream(bytes)
        val size = 500

        try {
            val result: BufferedImage
            val img = ImageIO.read(input)

            result = if (img.height == img.width) {
                scaleImage(img, size)
            } else if (img.height < size && img.width < size) {
                img
            } else if(img.height < size || img.width < size) {
                cropImage(img, Math.min(img.height, img.width))
            } else {
                cropImage(img, size)
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

    private fun scaleImage(img: BufferedImage, size: Int): BufferedImage {
        val scaledImage = img.getScaledInstance(size, size, Image.SCALE_SMOOTH)
        val result = BufferedImage(size, size, BufferedImage.TYPE_INT_RGB)
        result.graphics.drawImage(scaledImage, 0, 0, Color(0, 0, 0), null)
        return result
    }

    fun getImageForId(id: Int): ByteArray {
        return Files.readAllBytes(Paths.get("./upload").resolve(id.toString() + ".jpg"))
    }

    @Transactional(readOnly = true)
    fun listProducts(page: Int, onPage: Int): List<ProductRow> {
        return repository.findAll(PageRequest.of(page - 1, onPage))
                .content
                .map { pr -> toRow(pr) }
    }

    @Transactional
    fun deleteProduct(id: Int) {
        repository.deleteById(id)
        Files.deleteIfExists(Paths.get(String.format("./upload/%d.jpg", id)))
    }

    @Transactional
    fun updateProduct(id: Int, product: ProductDTO) {
        val oldProduct = repository.findById(id).get()
        repository.save(oldProduct.copy(name = product.name, description = product.description))
    }

    fun getRanking(): List<ProductRow> {
        val products = repository.findAll()
        return products
                .toList()
                .sortedByDescending { pr -> pr.powerRating + pr.tasteRating + pr.dustRating }
                .map { pr -> toRow(pr) }
    }

    fun search(phrase: String): List<ProductRow> {
        val products = repository.findAll()
        return products.toList()
                .filter { pr -> pr.name.contains(phrase) }
                .map { pr -> toRow(pr) }
    }

    private fun toRow(product: Product): ProductRow {
        return ProductRow(product.id, product.name, product.description, product.dustRating, product.powerRating, product.tasteRating)
    }
}