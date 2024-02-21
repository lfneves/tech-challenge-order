package com.mvp.order.domain.model.auth

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


@JvmRecord
data class LoginRequest(
    @field:Schema(description = "email", example = "mina@gmail.com") @param:Schema(
        description = "email",
        example = "email@gmail.com"
    )
    val email: @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String,

    @field:Size(
        min = 6,
        max = 20,
        message = "Password must be between 6 and 20 characters"
    ) @field:Schema(description = "password", example = "123456")
        @param:Schema(
        description = "password",
        example = "123456"
    ) @param:Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
        val password: @NotBlank(
        message = "Password cannot be blank"
    ) String
)
