package pl.edu.uj.ratemate.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.uj.ratemate.dto.CommentDTO
import pl.edu.uj.ratemate.entities.Comment
import pl.edu.uj.ratemate.entities.Product
import pl.edu.uj.ratemate.repositories.CommentRepository
import pl.edu.uj.ratemate.repositories.ProductRepository
import pl.edu.uj.ratemate.row.CommentRow
import java.time.LocalDateTime

@Service
class CommentService(
        private val commentRepository: CommentRepository,
        private val productRepository: ProductRepository) {

    @Transactional(readOnly = true)
    fun getComments(productId: Int): List<CommentRow> {
        return commentRepository.findByProductId(productId).map { c -> toRow(c) }
    }

    @Transactional
    fun addComment(productId: Int, comment: CommentDTO) {
        val product = productRepository.findById(productId).get()
        val ratings = productRepository.countComments(productId)
        val newDustRating = (product.dustRating * ratings + comment.dustRating) / (ratings + 1)
        val newPowerRating = (product.powerRating * ratings + comment.powerRating) / (ratings + 1)
        val newTasteRating = (product.tasteRating * ratings + comment.tasteRating) / (ratings + 1)



        productRepository.save(product.copy(dustRating = newDustRating, tasteRating = newTasteRating, powerRating = newPowerRating))
        commentRepository.save(Comment(0, product, comment.content, LocalDateTime.now(), comment.dustRating, comment.powerRating, comment.tasteRating))
    }

    @Transactional
    fun deleteComment(id: Int) {
        commentRepository.deleteById(id)
    }

    @Transactional
    fun editComment(productId: Int, commentId: Int, newComment: CommentDTO) {
        val product = productRepository.findById(productId).get()
        val oldComment = commentRepository.findById(commentId).get()
        val ratings = productRepository.countComments(productId)
        val newDustRating = (product.dustRating * ratings - oldComment.dustRating + newComment.dustRating) / ratings
        val newPowerRating = (product.powerRating * ratings - oldComment.powerRating + newComment.powerRating) / ratings
        val newTasteRating = (product.tasteRating * ratings - oldComment.tasteRating + newComment.tasteRating) / ratings

        productRepository.save(product.copy(dustRating = newDustRating, tasteRating = newTasteRating, powerRating = newPowerRating))
        commentRepository.save(Comment(commentId, product, newComment.content, oldComment.dateTime, newComment.dustRating, newComment.powerRating, newComment.tasteRating))
    }

    private fun toRow(comment: Comment): CommentRow {
        return CommentRow(comment.id, comment.content, comment.powerRating, comment.powerRating, comment.tasteRating, comment.dateTime)
    }
}