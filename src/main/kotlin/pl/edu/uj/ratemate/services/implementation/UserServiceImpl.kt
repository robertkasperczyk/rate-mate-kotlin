package pl.edu.uj.ratemate.services.implementation

import org.springframework.stereotype.Service
import pl.edu.uj.ratemate.entities.User
import pl.edu.uj.ratemate.repositories.UserRepository
import pl.edu.uj.ratemate.services.interfaces.UserService

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override fun listUsers(): List<String> {
        return userRepository.findAll().map(User::login)
    }
}