package pl.edu.uj.ratemate.repositories

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import pl.edu.uj.ratemate.entities.Product

@Repository
interface ProductRepository : PagingAndSortingRepository<Product, Int>