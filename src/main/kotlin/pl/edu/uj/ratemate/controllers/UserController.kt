package pl.edu.uj.ratemate.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.uj.ratemate.services.interfaces.UserService

@RestController
class UserController(private val userService: UserService) {

    @GetMapping("/users/list")
    fun list(): List<String> {
        return userService.listUsers()
    }
}