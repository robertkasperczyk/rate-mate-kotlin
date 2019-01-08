package pl.edu.uj.ratemate.services.interfaces

import org.springframework.web.multipart.MultipartFile
import pl.edu.uj.ratemate.dto.ProductDTO
import pl.edu.uj.ratemate.row.ProductRow

interface ProductService {

    fun saveProduct(file: MultipartFile, name: String, description: String)

    fun getImageForId(id: Int): ByteArray

    fun listProducts(page: Int, onPage: Int): List<ProductRow>

    fun deleteProduct(id: Int)

    fun updateProduct(id: Int, product: ProductDTO)

    fun getRanking(): List<ProductRow>

    fun saveImage(id: Int, file: MultipartFile)

    fun search(phrase: String?): List<ProductRow>
}