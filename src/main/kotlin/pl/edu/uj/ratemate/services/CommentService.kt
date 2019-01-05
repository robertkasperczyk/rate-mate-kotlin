package pl.edu.uj.ratemate.services

import org.springframework.stereotype.Service
import pl.edu.uj.ratemate.entities.Comment
import pl.edu.uj.ratemate.repositories.CommentRepository

@Service
class CommentService(private val reposiory: CommentRepository) {
    fun getComments(productId: Int): List<Comment> {
        return reposiory.findByProductId(productId)
    }

    fun addComment(comment: Comment) {
        reposiory.save(comment)
    }
}