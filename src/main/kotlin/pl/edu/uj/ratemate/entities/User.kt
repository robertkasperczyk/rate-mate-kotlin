package pl.edu.uj.ratemate.entities

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
data class User (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0,

        @Column(unique = true)
        @get: NotBlank
        val username: String

//        @get: NotBlank
//        val passwordHash: String
)