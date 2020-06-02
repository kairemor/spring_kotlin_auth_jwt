package com.example.demo.config


import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function
import kotlin.reflect.KFunction1

@Component
class JwtTokenUtil {
    val JWT_TOKEN_VALIDITY = 5 * 60 * 60.toLong()

    @Value("\${jwt.secret}")
    private val secret: String? = null

    private fun getAllClaimsFromToken(token: String?): Claims {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
    }

    fun <T> getClaimFromToken(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims: Claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }
//    fun getUsernameFromToken(token: String?): String {
//        return getClaimFromToken(token, Claims::getSubject)
//    }
    fun getUsernameFromToken(token: String?) :  String {
        val claims: Claims = getAllClaimsFromToken(token);
        return (claims::getSubject)();
    }
    fun getExpirationDateFromToken(token: String?): Date {
        val claims: Claims = getAllClaimsFromToken(token);
        return (claims::getExpiration)()
    }

//    fun getExpirationDateFromToken(token: String?): Date {
//        return getClaimFromToken(token, Claims::getExpiration)
//    }

    private fun isTokenExpired(token: String?): Boolean? {
        val expiration: Date = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    fun generateToken(userDetails: UserDetails): String? {
        val claims: Map<String, Any> = HashMap()
        return doGenerateToken(claims, userDetails.username)
    }

    private fun doGenerateToken(claims: Map<String, Any>, subject: String): String? {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact()
    }
    
    fun validateToken(token: String?, userDetails: UserDetails): Boolean? {
        val username = getUsernameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)!!
    }

}