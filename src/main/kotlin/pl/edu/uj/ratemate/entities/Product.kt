package pl.edu.uj.ratemate.entities

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
data class Product(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0,

        @get: NotBlank
        val name: String = "",

        @get: NotBlank
        val description: String = "",

        val dustRating: Double = 0.0,

        val powerRating: Double = 0.0,

        val tasteRating: Double = 0.0,

        @OneToMany(mappedBy="product", orphanRemoval = true)
        val comments: List<Comment>

)