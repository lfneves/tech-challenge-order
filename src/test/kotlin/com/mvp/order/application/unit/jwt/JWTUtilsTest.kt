package com.mvp.order.application.unit.jwt

import com.mvp.order.domain.configuration.JwtConfig
import com.mvp.order.domain.configuration.jwt.JWTUtils
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import javax.crypto.SecretKey

class JWTUtilsTest {
    
    private val jwtConfig = JwtConfig()
    @Test
    fun `generate token and validate`() {
        val email = "test@example.com"
        val token = JWTUtils.generateToken(email)

        assertNotNull(token, "Token should not be null")

        val extractedEmail = JWTUtils.extractUsername(token)
        assertEquals(email, extractedEmail, "Extracted email should match the original")

        val userDetails: UserDetails = User.withUsername(email).password("password").authorities("USER").build()
        val isValid = JWTUtils.validateToken(token, userDetails)
        assertTrue(isValid, "Token should be valid")
    }

    @Test
    fun `extract username from token`() {
        val email = "username@example.com"
        val token = JWTUtils.generateToken(email)
        val extractedEmail = JWTUtils.extractUsername(token)
        println(extractedEmail)
        assertEquals(email, extractedEmail, "Extracted username should match the original email")
    }

    @Test
    fun `validate valid token returns true`() {
        val email = "test@example.com"
        val token = JWTUtils.generateToken(email)

        assertNotNull(token, "Token should not be null")

        val extractedEmail = JWTUtils.extractUsername(token)
        assertEquals(email, extractedEmail, "Extracted email should match the original")
        val isValid = JWTUtils.validateTokenSecret(token)

        assertTrue(isValid)
    }

    @Test
    fun `validate token with wrong signature returns false`() {
        val wrongKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
        val invalidToken =  Jwts.builder()
            .setSubject("TestUser")
            .signWith(wrongKey)
            .compact()

        assertFalse(JWTUtils.validateTokenSecret(invalidToken))
    }

    @Test
    fun `validate expired token throws exception`() {
        val email = "expire@example.com"
        val token = JWTUtils.generateToken(email)

        assertThrows<AccessDeniedException> {
            JWTUtils
                .validateToken(token, User.withUsername(email).password("password").authorities("USER").build())
        }
    }
}
