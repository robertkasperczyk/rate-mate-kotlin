package pl.edu.uj.ratemate.controllers

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import pl.edu.uj.ratemate.entities.Product
import pl.edu.uj.ratemate.services.ProductService

@RestController
class ProductController(private val service: ProductService) {

    @RequestMapping("/product/add")
    fun addProduct(file: MultipartFile,
                   @RequestParam("name") name: String,
                   @RequestParam("description") description: String) {
        service.saveProduct(file, name, description)
    }

    @GetMapping("/products/list")
    fun listProducts(@RequestParam("page") page: Int,
                     @RequestParam("onPage") onPage: Int): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(service.listProducts(page, onPage))
    }

    @GetMapping(
            value = ["/product/{id}/image"],
            produces = [MediaType.IMAGE_JPEG_VALUE]
    )
    fun getImage(@PathVariable("id") id: Int): ByteArray {
        return service.getImageForId(id)
    }

}