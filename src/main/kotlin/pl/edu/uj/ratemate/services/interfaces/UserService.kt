package pl.edu.uj.ratemate.services.interfaces

import pl.edu.uj.ratemate.dto.UserDTO

interface UserService {
    fun listUsers(): List<UserDTO>
}