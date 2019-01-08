package pl.edu.uj.ratemate.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import pl.edu.uj.ratemate.dto.ProductDTO
import pl.edu.uj.ratemate.row.ProductRow
import pl.edu.uj.ratemate.services.implementation.ProductServiceImpl

@RestController
class ProductController(private val service: ProductServiceImpl) {

    @RequestMapping("/product/add")
    @ResponseStatus(value = HttpStatus.OK)
    fun addProduct(file: MultipartFile,
                   @RequestParam("name") name: String,
                   @RequestParam("description") description: String) {
        service.saveProduct(file, name, description)
    }

    @GetMapping("/products/list")
    fun listProducts(@RequestParam("page") page: Int,
                     @RequestParam("onPage") onPage: Int): ResponseEntity<List<ProductRow>> {
        return ResponseEntity.ok(service.listProducts(page, onPage))
    }

    @GetMapping(
            value = ["/product/{id}/image"],
            produces = [MediaType.IMAGE_JPEG_VALUE]
    )
    fun getImage(@PathVariable("id") id: Int): ByteArray {
        return service.getImageForId(id)
    }

    @DeleteMapping("/product/{id}/delete")
    @ResponseStatus(value = HttpStatus.OK)
    fun deleteProduct(@PathVariable("id") id: Int) {
        service.deleteProduct(id)
    }

    @PostMapping("/product/{id}/edit")
    @ResponseStatus(value = HttpStatus.OK)
    fun updateProduct(
            @PathVariable("id") id: Int,
            @RequestBody product: ProductDTO) {
        service.updateProduct(id, product)
    }

    @RequestMapping("/product/{id}/image/edit")
    @ResponseStatus(value = HttpStatus.OK)
    fun updateImage(
            @PathVariable("id") id: Int,
            file: MultipartFile) {
        service.saveImage(id, file)
    }

    @GetMapping("/products/ranking")
    fun ranking(): ResponseEntity<List<ProductRow>> {
        return ResponseEntity.ok(service.getRanking())
    }

    @GetMapping("/products/search")
    fun search(@RequestParam("phrase") phrase: String): ResponseEntity<List<ProductRow>> {
        return ResponseEntity.ok(service.search(phrase))
    }
}