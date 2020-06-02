package com.example.demo.controllers

import com.example.demo.config.JwtTokenUtil
import com.example.demo.dto.JwtResponse
import com.example.demo.dto.UserRegisterDTO
import com.example.demo.exceptions.CPMErrorsMessage
import com.example.demo.exceptions.ErrorMessageResponse
import com.example.demo.models.User
import com.example.demo.services.JwtUserDetailsService
import com.example.demo.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class JwtAuthController {
    @Autowired
    private val authenticationManager: AuthenticationManager? = null

    @Autowired
    private val jwtTokenUtil: JwtTokenUtil? = null

    @Autowired
    private val userDetailsService: JwtUserDetailsService? = null

    @Autowired
    private val userService: UserService? = null

    @PostMapping(value = ["/login"])
    @Throws(Exception::class)
    fun createAuthenticationToken(@RequestBody authenticationRequest: UserRegisterDTO): ResponseEntity<*>? {
        try {
            authenticate(authenticationRequest.username, authenticationRequest.password)
        } catch (e: InternalAuthenticationServiceException) {
            return ResponseEntity<Any>(ErrorMessageResponse(CPMErrorsMessage.LOGIN_ERROR.getCodeError(),CPMErrorsMessage.LOGIN_ERROR.getMessage()), null, HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            return ResponseEntity<Any>(ErrorMessageResponse(CPMErrorsMessage.LOGIN_ERROR.getCodeError(),CPMErrorsMessage.LOGIN_ERROR.getMessage()), null, HttpStatus.BAD_REQUEST)
        }
        val userDetails = userDetailsService!!.loadUserByUsername(authenticationRequest.username)
        val token = jwtTokenUtil!!.generateToken(userDetails)
        val user: Optional<User> = userService!!.findByUsername(userDetails.username)
        return ResponseEntity.ok<Any>(JwtResponse(token, user.get().username, user.get().id))
    }



    @PostMapping(value = ["/register"])
    @Throws(Exception::class)
    fun saveUser(@RequestBody user: UserRegisterDTO): ResponseEntity<*>? {
        if (user.username.length <= 5) {
            return ResponseEntity<Any>(ErrorMessageResponse(CPMErrorsMessage.USERNAME_ERROR.getCodeError(), CPMErrorsMessage.USERNAME_ERROR.getMessage()), null, HttpStatus.BAD_REQUEST)
        }
        if (user.password.length <= 7) {
            return ResponseEntity<Any>(ErrorMessageResponse(CPMErrorsMessage.PASSWORD_ERROR.getCodeError(), CPMErrorsMessage.PASSWORD_ERROR.getMessage()), null, HttpStatus.BAD_REQUEST)
        }
        val optUser: Optional<User> = userService!!.findByUsername(user.username)
        println(optUser)

        if (optUser.isPresent) {
            return ResponseEntity<Any>(ErrorMessageResponse(CPMErrorsMessage.USER_ALREADY_EXIST.getCodeError(), CPMErrorsMessage.USER_ALREADY_EXIST.getMessage()), null, HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity.ok(userDetailsService!!.save(user))
    }

    @Throws(Exception::class)
    private fun authenticate(username: String, password: String) {
        try {
            authenticationManager!!.authenticate(UsernamePasswordAuthenticationToken(username, password))
        } catch (e: BadCredentialsException) {
            throw Exception(CPMErrorsMessage.LOGIN_ERROR.getMessage(), e)
        }
    }


}