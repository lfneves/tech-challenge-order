package com.mvp.order.domain.configuration

import com.mvp.order.domain.configuration.jwt.JWTUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class JWTConfig {
    @Value("\${jwt.secret}")
    private lateinit var secret: String

    @PostConstruct
    fun init() {
        JWTUtils.initializeSecret(secret)
    }
}
