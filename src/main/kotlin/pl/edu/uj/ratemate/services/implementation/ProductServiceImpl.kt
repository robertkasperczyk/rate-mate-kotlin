package pl.edu.uj.ratemate.services.implementation

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import pl.edu.uj.ratemate.utils.ImageSaver
import pl.edu.uj.ratemate.dto.ProductDTO
import pl.edu.uj.ratemate.entities.Comment
import pl.edu.uj.ratemate.entities.Product
import pl.edu.uj.ratemate.repositories.ProductRepository
import pl.edu.uj.ratemate.row.ProductRow
import pl.edu.uj.ratemate.services.interfaces.ProductService
import java.nio.file.Files
import java.nio.file.Paths


@Service
class ProductServiceImpl(private val repository: ProductRepository, private val saver: ImageSaver) : ProductService {

    @Transactional
    override fun saveProduct(file: MultipartFile, name: String, description: String) {
        val product = repository.save(Product(0, name, description, comments = emptyList()))

        saver.saveImage(product.id, file.bytes)
    }

    override fun getImageForId(id: Int): ByteArray {
        return Files.readAllBytes(Paths.get("./upload").resolve(id.toString() + ".jpg"))
    }

    @Transactional(readOnly = true)
    override fun listProducts(page: Int, onPage: Int): List<ProductRow> {
        return repository.findAll(PageRequest.of(page - 1, onPage))
                .content
                .map { pr -> toRow(pr) }
    }

    @Transactional
    override fun deleteProduct(id: Int) {
        repository.deleteById(id)
        Files.deleteIfExists(Paths.get(String.format("./upload/%d.jpg", id)))
    }

    @Transactional
    override fun updateProduct(id: Int, product: ProductDTO) {
        val oldProduct = repository.findById(id).get()
        repository.save(oldProduct.copy(name = product.name, description = product.description))
    }

    @Transactional(readOnly = true)
    override fun getRanking(): List<ProductRow> {
        val products = repository.findAll()
        return products
                .toList()
                .sortedByDescending { pr -> pr.powerRating + pr.tasteRating + pr.dustRating }
                .map { pr -> toRow(pr) }
    }

    override fun search(phrase: String?): List<ProductRow> {
        if (phrase == null) {
            return emptyList()
        }

        val products = repository.findAll()
        return products.toList()
                .filter { pr -> pr.name.contains(phrase, ignoreCase = true) }
                .map { pr -> toRow(pr) }
    }

    private fun toRow(product: Product): ProductRow {
        return ProductRow(product.id, product.name, product.description,
                product.dustRating, product.powerRating, product.tasteRating,
                product.comments.map(Comment::toRow))
    }

    override fun saveImage(id: Int, file: MultipartFile) {
        saver.saveImage(id, file.bytes)
    }
}