package com.mvp.order.domain.model.auth

import com.mvp.order.infrastruture.entity.auth.LoginAttemptEntity
import java.time.LocalDateTime

data class LoginAttempt(
    val email: String,
    val success: Boolean,
    val createdAt: LocalDateTime
) {
    fun toEntity(): LoginAttemptEntity {
        return LoginAttemptEntity(
            email = this.email,
            success = this.success,
            createdAt = this.createdAt
        )
    }
}