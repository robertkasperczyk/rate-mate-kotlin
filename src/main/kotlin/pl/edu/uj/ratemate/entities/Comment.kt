package pl.edu.uj.ratemate.entities

import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
data class Comment (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0,

        @OneToOne
        val author: User,

        @ManyToOne
        val product: Product,

        @get: NotBlank
        val content: String = "",

        @get: NotBlank
        val dateTime: LocalDateTime
)