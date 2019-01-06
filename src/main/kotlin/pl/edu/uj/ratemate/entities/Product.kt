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

        val dustRating: Int = 0,

        val powerRating: Int = 0,

        val tasteRating: Int = 0,

        @OneToMany(mappedBy="product", orphanRemoval = true)
        val comments: List<Comment>

)