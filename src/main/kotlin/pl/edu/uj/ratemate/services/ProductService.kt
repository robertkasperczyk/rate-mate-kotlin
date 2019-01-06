package pl.edu.uj.ratemate.services

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import pl.edu.uj.ratemate.util.ImageSaver
import pl.edu.uj.ratemate.dto.ProductDTO
import pl.edu.uj.ratemate.entities.Product
import pl.edu.uj.ratemate.repositories.ProductRepository
import pl.edu.uj.ratemate.row.ProductRow
import java.nio.file.Files
import java.nio.file.Paths


@Service
class ProductService(private val repository: ProductRepository, private val saver: ImageSaver) {

    @Transactional
    fun saveProduct(file: MultipartFile, name: String, description: String) {
        val product = repository.save(Product(0, name, description, comments = emptyList()))

        saver.saveImage(product.id, file.bytes)
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

    fun saveImage(id: Int, file: MultipartFile) {
        saver.saveImage(id, file.bytes)
    }
}