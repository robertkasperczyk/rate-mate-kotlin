package pl.edu.uj.ratemate.dto

data class CommentDTO(
        val content: String = "",
        val dustRating: Int = 0,
        val powerRating: Int = 0,
        val tasteRating: Int = 0,
        val username: String = ""
)