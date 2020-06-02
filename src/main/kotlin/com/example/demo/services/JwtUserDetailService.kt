package com.example.demo.services

import com.example.demo.dto.UserRegisterDTO
import com.example.demo.models.User
import com.example.demo.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtUserDetailsService : UserDetailsService {
    @Autowired
    private val userDao: UserRepository? = null

    @Autowired
    private val bcryptEncoder: PasswordEncoder? = null

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val optUser: Optional<User> = userDao!!.findByUsername(username)
                ?: throw UsernameNotFoundException("User not found with username: $username")
        return org.springframework.security.core.userdetails.User(optUser.get().username, optUser.get().password,
                ArrayList<GrantedAuthority>())
    }

    fun save(user: UserRegisterDTO): User {
        val newUser = User(user.username, bcryptEncoder!!.encode(user.password))
        return userDao!!.save(newUser)
    }


}