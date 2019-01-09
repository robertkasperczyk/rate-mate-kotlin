package pl.edu.uj.ratemate.row

data class ProductRow (
        val id: Int,
        val name: String,
        val description: String,
        val dustRating: Double,
        val powerRating: Double,
        val tasteRating: Double,
        val comments: List<CommentRow>
)