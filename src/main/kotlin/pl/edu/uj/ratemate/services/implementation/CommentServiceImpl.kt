package pl.edu.uj.ratemate.services.implementation

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.uj.ratemate.dto.CommentDTO
import pl.edu.uj.ratemate.entities.Comment
import pl.edu.uj.ratemate.repositories.CommentRepository
import pl.edu.uj.ratemate.repositories.ProductRepository
import pl.edu.uj.ratemate.row.CommentRow
import pl.edu.uj.ratemate.services.interfaces.CommentService
import pl.edu.uj.ratemate.utils.UserRegister
import java.time.LocalDateTime

@Service
class CommentServiceImpl(
        private val commentRepository: CommentRepository,
        private val productRepository: ProductRepository,
        private val userRegister: UserRegister) : CommentService {

    @Transactional(readOnly = true)
    override fun getComments(productId: Int): List<CommentRow> {
        return commentRepository.findByProductId(productId).map(Comment::toRow)
    }

    @Transactional
    override fun addComment(productId: Int, comment: CommentDTO) {
        val product = productRepository.findById(productId).get()
        val user = userRegister.resolveByUserName(comment.username)
        commentRepository.save(Comment(0, user, product, comment.content, LocalDateTime.now(),
                comment.dustRating, comment.powerRating, comment.tasteRating))

        val ratings = productRepository.countComments(productId)
        val newDustRating = (product.dustRating * (ratings - 1) + comment.dustRating) / ratings
        val newPowerRating = (product.powerRating * (ratings - 1) + comment.powerRating) / ratings
        val newTasteRating = (product.tasteRating * (ratings - 1) + comment.tasteRating) / ratings

        productRepository.save(product.copy(dustRating = newDustRating, tasteRating = newTasteRating, powerRating = newPowerRating))
    }

    @Transactional
    override fun deleteComment(id: Int) {
        val comment = commentRepository.findById(id).get()
        commentRepository.deleteById(id)
        val product = comment.product
        val ratings = productRepository.countComments(product.id)

        if (ratings == 0) {
            productRepository.save(product.copy(dustRating = 0.0, tasteRating = 0.0, powerRating = 0.0))
        }

        val newDustRating = (product.dustRating * (ratings + 1) - comment.dustRating) / ratings
        val newPowerRating = (product.powerRating * (ratings + 1) - comment.powerRating) / ratings
        val newTasteRating = (product.tasteRating * (ratings + 1) - comment.tasteRating) / ratings

        productRepository.save(product.copy(dustRating = newDustRating, tasteRating = newPowerRating, powerRating = newTasteRating))
    }

    @Transactional
    override fun editComment(productId: Int, commentId: Int, newComment: CommentDTO) {
        val product = productRepository.findById(productId).get()
        val oldComment = commentRepository.findById(commentId).get()
        val ratings = productRepository.countComments(productId)
        val newDustRating = (product.dustRating * ratings - oldComment.dustRating + newComment.dustRating) / ratings
        val newPowerRating = (product.powerRating * ratings - oldComment.powerRating + newComment.powerRating) / ratings
        val newTasteRating = (product.tasteRating * ratings - oldComment.tasteRating + newComment.tasteRating) / ratings.toDouble()

        productRepository.save(product.copy(dustRating = newDustRating, tasteRating = newTasteRating, powerRating = newPowerRating))
        commentRepository.save(commentRepository
                .findById(commentId).get().copy(
                        content = newComment.content,
                        powerRating = newComment.powerRating,
                        dustRating = newComment.dustRating,
                        tasteRating = newComment.tasteRating))
    }
}