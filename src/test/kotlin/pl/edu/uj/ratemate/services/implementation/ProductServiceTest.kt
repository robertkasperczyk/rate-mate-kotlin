package pl.edu.uj.ratemate.services.implementation

import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.multipart.MultipartFile
import pl.edu.uj.ratemate.entities.Product
import pl.edu.uj.ratemate.repositories.ProductRepository
import pl.edu.uj.ratemate.row.ProductRow
import pl.edu.uj.ratemate.utils.ImageSaver
import java.util.*

class ProductServiceTest {

    @get:Rule
    public var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var productRepository: ProductRepository

    @Mock
    lateinit var imageSaver: ImageSaver

    @InjectMocks
    lateinit var productService: ProductServiceImpl

    @Test
    fun savesProduct() {
        val file = Mockito.mock(MultipartFile::class.java)

        Mockito.`when`(file.bytes).thenReturn(ByteArray(1))
        Mockito.`when`(productRepository.save(Product(0, "name", "desc", comments = emptyList())))
                .thenReturn(Product(1, "name", "desc", comments = emptyList()))

        productService.saveProduct(file, "name", "desc")

        Mockito.verify(imageSaver).saveImage(1, ByteArray(1))
        Mockito.verify(productRepository).save(Product(0, "name", "desc", comments = emptyList()))
    }

    @Test
    fun listsProducts() {
        val page = Mockito.mock(Page::class.java)
        Mockito.`when`(page.content).thenReturn(Arrays.asList(Product(1, "name", "desc", comments = emptyList())))
        Mockito.`when`(productRepository.findAll(PageRequest.of(2, 3)))
                .thenReturn(page as Page<Product>?)

        val result = productService.listProducts(3, 3)

        Mockito.verify(productRepository).findAll(PageRequest.of(2, 3))
        Assertions.assertThat(result).containsExactly(
                ProductRow(1, "name", "desc", 0, 0, 0, emptyList()))
    }
}