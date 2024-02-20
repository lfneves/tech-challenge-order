package com.mvp.order.utils.jwt


import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.userdetails.UserDetails
import java.security.Key
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

object JWTUtils {

    private val SECRET_KEY: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private const val MINUTES = 60

    fun generateToken(email: String?): String {
        val now = Instant.now()
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(MINUTES.toLong(), ChronoUnit.MINUTES)))
            .signWith(SECRET_KEY)
            .compact()
    }

    fun extractUsername(token: String): String {
        return getTokenBody(token).subject
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun getTokenBody(token: String): Claims {
        try {
            return Jwts
                .parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: SignatureException) { // Invalid signature or expired token
            throw AccessDeniedException("Access denied: " + e.message)
        } catch (e: ExpiredJwtException) {
            throw AccessDeniedException("Access denied: " + e.message)
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        val claims = getTokenBody(token)
        return claims.expiration.before(Date())
    }
}