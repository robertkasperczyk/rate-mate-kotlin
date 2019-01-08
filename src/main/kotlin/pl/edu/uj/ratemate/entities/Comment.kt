package pl.edu.uj.ratemate.entities

import pl.edu.uj.ratemate.row.CommentRow
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
data class Comment(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0,

//        @OneToOne
//        val author: User,

        @ManyToOne
        @JoinColumn(name = "product_id")
        val product: Product,

        @get: NotBlank
        val content: String = "",

        val dateTime: LocalDateTime,

        val dustRating: Int = 0,

        val powerRating: Int = 0,

        val tasteRating: Int = 0
) {
    fun toRow(): CommentRow {
        return CommentRow(id, content, powerRating, powerRating, tasteRating, dateTime)
    }
}