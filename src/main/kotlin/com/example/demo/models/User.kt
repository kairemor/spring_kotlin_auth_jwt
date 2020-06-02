package com.example.demo.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name = "users")
data class User(val username: String, val password: String, @Id @GeneratedValue val id: Long? = null)
