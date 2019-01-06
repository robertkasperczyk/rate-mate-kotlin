package pl.edu.uj.ratemate.row

data class ProductRow (
        val id: Int,
        val name: String,
        val description: String,
        val dustRating: Int,
        val powerRating: Int,
        val tasteRating: Int
)