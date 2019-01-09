package pl.edu.uj.ratemate.utils

import org.springframework.stereotype.Component
import pl.edu.uj.ratemate.entities.User
import pl.edu.uj.ratemate.repositories.UserRepository
import javax.annotation.PostConstruct

@Component
class UserRegister(private val userRepository: UserRepository) {

    private val registry: MutableMap<String, User> = HashMap()

    @PostConstruct
    fun init() {
        registry.putAll(userRepository.findAll().associate { it.username to it })
    }

    fun resolveByUserName(username: String): User {
        return registry.computeIfAbsent(username) { un -> userRepository.save(User(0, un)) }
    }

}