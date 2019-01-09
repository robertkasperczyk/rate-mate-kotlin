package pl.edu.uj.ratemate.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pl.edu.uj.ratemate.entities.Comment

@Repository
interface CommentRepository : CrudRepository<Comment, Int> {
    fun findByProductId(id: Int) : List<Comment>

    fun countByAuthorId(id: Int): Int
}