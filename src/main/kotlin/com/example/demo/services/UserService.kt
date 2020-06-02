package com.example.demo.services

import com.example.demo.models.User
import com.example.demo.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService {
    @Autowired
    val userRepository: UserRepository? = null ;

    fun findByUsername(username: String): Optional<User> {
        return userRepository!!.findByUsername(username);
    }
}