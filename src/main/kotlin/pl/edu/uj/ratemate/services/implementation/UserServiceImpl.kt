package pl.edu.uj.ratemate.services.implementation

import org.springframework.stereotype.Service
import pl.edu.uj.ratemate.dto.UserDTO
import pl.edu.uj.ratemate.entities.User
import pl.edu.uj.ratemate.repositories.CommentRepository
import pl.edu.uj.ratemate.repositories.UserRepository
import pl.edu.uj.ratemate.services.interfaces.UserService
import pl.edu.uj.ratemate.utils.UserRegister

@Service
class UserServiceImpl(
        private val userRegister: UserRegister,
        private val commentRepository: CommentRepository) : UserService {
    override fun listUsers(): List<UserDTO> {
        return userRegister.findAll().map {us -> UserDTO(us.username, commentRepository.countByAuthorId(us.id))}
    }
}