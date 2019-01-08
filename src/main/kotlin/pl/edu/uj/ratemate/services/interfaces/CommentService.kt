package pl.edu.uj.ratemate.services.interfaces

import pl.edu.uj.ratemate.dto.CommentDTO
import pl.edu.uj.ratemate.row.CommentRow

interface CommentService {

    fun getComments(productId: Int): List<CommentRow>

    fun addComment(productId: Int, comment: CommentDTO)

    fun deleteComment(id: Int)

    fun editComment(productId: Int, commentId: Int, newComment: CommentDTO)
}