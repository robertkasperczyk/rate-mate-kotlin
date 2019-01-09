package pl.edu.uj.ratemate.services.implementation

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.uj.ratemate.dto.CommentDTO
import pl.edu.uj.ratemate.entities.Comment
import pl.edu.uj.ratemate.entities.User
import pl.edu.uj.ratemate.repositories.CommentRepository
import pl.edu.uj.ratemate.repositories.ProductRepository
import pl.edu.uj.ratemate.repositories.UserRepository
import pl.edu.uj.ratemate.row.CommentRow
import pl.edu.uj.ratemate.services.interfaces.CommentService
import java.time.LocalDateTime

@Service
class CommentServiceImpl(
        private val commentRepository: CommentRepository,
        private val productRepository: ProductRepository,
        private val userRepository: UserRepository) : CommentService {

    @Transactional(readOnly = true)
    override fun getComments(productId: Int): List<CommentRow> {
        return commentRepository.findByProductId(productId).map(Comment::toRow)
    }

    @Transactional
    override fun addComment(productId: Int, comment: CommentDTO) {
        val product = productRepository.findById(productId).get()
        val ratings = productRepository.countComments(productId)
        val newDustRating = (product.dustRating * ratings + comment.dustRating) / (ratings + 1)
        val newPowerRating = (product.powerRating * ratings + comment.powerRating) / (ratings + 1)
        val newTasteRating = (product.tasteRating * ratings + comment.tasteRating) / (ratings + 1)

        val user = userRepository.findByLogin(comment.username).orElse(userRepository.save(User(0, comment.username)))

        productRepository.save(product.copy(dustRating = newDustRating, tasteRating = newTasteRating, powerRating = newPowerRating))
        commentRepository.save(Comment(0, user, product, comment.content, LocalDateTime.now(),
                comment.dustRating, comment.powerRating, comment.tasteRating))
    }

    @Transactional
    override fun deleteComment(id: Int) {
        commentRepository.deleteById(id)
    }

    @Transactional
    override fun editComment(productId: Int, commentId: Int, newComment: CommentDTO) {
        val product = productRepository.findById(productId).get()
        val oldComment = commentRepository.findById(commentId).get()
        val ratings = productRepository.countComments(productId)
        val newDustRating = (product.dustRating * ratings - oldComment.dustRating + newComment.dustRating) / ratings
        val newPowerRating = (product.powerRating * ratings - oldComment.powerRating + newComment.powerRating) / ratings
        val newTasteRating = (product.tasteRating * ratings - oldComment.tasteRating + newComment.tasteRating) / ratings

        productRepository.save(product.copy(dustRating = newDustRating, tasteRating = newTasteRating, powerRating = newPowerRating))
        commentRepository.save(commentRepository
                .findById(commentId).get().copy(
                        content = newComment.content,
                        powerRating = newPowerRating,
                        dustRating = newDustRating,
                        tasteRating = newTasteRating))
    }
}