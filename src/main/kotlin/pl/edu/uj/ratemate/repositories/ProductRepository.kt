package pl.edu.uj.ratemate.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import pl.edu.uj.ratemate.entities.Product

@Repository
interface ProductRepository : PagingAndSortingRepository<Product, Int> {

    @Query(value = "SELECT COUNT(*) FROM product P JOIN comment C ON P.id = C.product_id WHERE P.id = ?1", nativeQuery = true)
    fun countComments(id: Int): Int

}