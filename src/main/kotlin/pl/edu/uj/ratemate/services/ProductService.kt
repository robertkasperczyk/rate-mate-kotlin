package pl.edu.uj.ratemate.services

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import pl.edu.uj.ratemate.dto.ProductDTO
import pl.edu.uj.ratemate.entities.Product
import pl.edu.uj.ratemate.repositories.ProductRepository
import java.nio.file.Files
import java.nio.file.Paths

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
        stream.transferTo(fullPath)
    }

    fun getImageForId(id: Int): ByteArray {
        return Files.readAllBytes(Paths.get("./upload").resolve(id.toString() + ".jpg"))
    }

    @Transactional(readOnly = true)
    fun listProducts(page: Int, onPage: Int): List<Product> {
        return repository.findAll(PageRequest.of(page - 1, onPage)).content
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
}