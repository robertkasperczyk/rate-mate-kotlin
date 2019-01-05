package pl.edu.uj.ratemate

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RateMateApplication

fun main(args: Array<String>) {
    runApplication<RateMateApplication>(*args)
}

