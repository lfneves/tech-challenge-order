package com.mvp.order.infrastruture.entity.auth

import com.mvp.order.domain.model.auth.LoginAttempt
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tb_login_attempt")
data class LoginAttemptEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val email: String,
    val success: Boolean,
    val createdAt: LocalDateTime
) {
    fun toDTO(): LoginAttempt {
        return LoginAttempt(
            email = this.email,
            success = this.success,
            createdAt = this.createdAt
        )
    }
}