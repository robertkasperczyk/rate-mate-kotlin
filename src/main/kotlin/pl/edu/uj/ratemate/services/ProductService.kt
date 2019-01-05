package pl.edu.uj.ratemate.services

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import pl.edu.uj.ratemate.entities.Product
import pl.edu.uj.ratemate.repositories.ProductRepository
import java.nio.file.Files
import java.nio.file.Paths

@Service
class ProductService(private val repository: ProductRepository) {

    fun saveProduct(file: MultipartFile, name: String, description: String) {
        val product = repository.save(Product(0, name, description))

        saveImage(product.id, file)
    }

    private fun saveImage(id: Int, stream: MultipartFile) {
        val dirPath = Paths.get("./upload")
        if (Files.notExists(dirPath)) {
            Files.createDirectories(dirPath)
        }

        val fullPath = dirPath.resolve(String.format("%d.jpg", id))
        if (Files.exists(fullPath)) {
            Files.delete(fullPath)
        } else {
            Files.createFile(fullPath)
        }

        stream.transferTo(fullPath)
    }

    fun getImageForId(id: Int): ByteArray {
        return Files.readAllBytes(Paths.get("./upload").resolve(id.toString() + ".jpg"))
    }

    fun listProducts(page: Int, onPage: Int): List<Product> {
        return repository.findAll(PageRequest.of(page - 1, onPage)).content
    }
}