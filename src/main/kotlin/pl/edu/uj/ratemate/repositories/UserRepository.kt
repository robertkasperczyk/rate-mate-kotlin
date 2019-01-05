package pl.edu.uj.ratemate.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pl.edu.uj.ratemate.entities.User

@Repository
interface UserRepository : CrudRepository<User, Int>