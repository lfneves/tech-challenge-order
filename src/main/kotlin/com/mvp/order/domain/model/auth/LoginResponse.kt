package com.mvp.order.domain.model.auth

import io.swagger.v3.oas.annotations.media.Schema

@JvmRecord
data class LoginResponse(
    @field:Schema(description = "email") @param:Schema(description = "email")
    val email: String,
    @field:Schema(description = "JWT token") @param:Schema(description = "JWT token")
    val token: String
)
