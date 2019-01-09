package pl.edu.uj.ratemate.row

import java.time.LocalDateTime

data class CommentRow (
        val id: Int,
        val content: String,
        val dustRating: Int,
        val powerRating: Int,
        val tasteRating: Int,
        val dateTime: LocalDateTime,
        val username: String
)