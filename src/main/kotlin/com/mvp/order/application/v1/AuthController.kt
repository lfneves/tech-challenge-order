package com.mvp.order.application.v1

import com.mvp.order.domain.configuration.jwt.JWTUtils
import com.mvp.order.domain.model.auth.ApiErrorResponse
import com.mvp.order.domain.model.auth.LoginRequest
import com.mvp.order.domain.model.auth.LoginResponse
import com.mvp.order.domain.model.auth.ResponseSignupDTO
import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.model.user.UserDTO
import com.mvp.order.domain.service.auth.AuthService
import com.mvp.order.domain.service.auth.LoginService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/auth/")
class AuthController @Autowired constructor(
    private val authService: AuthService,
    private val authenticationManager: AuthenticationManager,
    private val loginService: LoginService
) {
    var logger: Logger = LoggerFactory.getLogger(AuthController::class.java)

    @Operation(
        summary = "Cadastro de Usuário",
        description = "Cadastro usuário usado quando não possui usuário e senha",
        tags = ["Usuários"]
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    fun signup(@RequestBody request: @Valid UserDTO): ResponseEntity<ResponseSignupDTO> {
        logger.info("/signup")
        return ResponseEntity.ok(authService.signup(request))
    }

    @Operation(summary = "Authenticate user and return token")
    @ApiResponse(responseCode = "200", content = [Content(schema = Schema(implementation = LoginResponse::class))])
    @ApiResponse(responseCode = "401", content = [Content(schema = Schema(implementation = ApiErrorResponse::class))])
    @ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ApiErrorResponse::class))])
    @ApiResponse(responseCode = "500", content = [Content(schema = Schema(implementation = ApiErrorResponse::class))])
    @PostMapping(value = ["/login"])
    fun login(@RequestBody request: @Valid LoginRequest): ResponseEntity<LoginResponse> {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.username, request.password))
        } catch (e: Exceptions.BadCredentialsException) {
            loginService.addLoginAttempt(request.username, false)
            throw e
        }

        val token: String = JWTUtils.generateToken(request.username)
        loginService.addLoginAttempt(request.username, true)
        return ResponseEntity.ok(LoginResponse(token))
    }
}