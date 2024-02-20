package com.mvp.order.application.v1

import com.mvp.order.domain.model.auth.ResponseSignupDTO
import com.mvp.order.domain.model.user.UserDTO
import com.mvp.order.domain.service.auth.AuthService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/auth/")
class AuthController @Autowired constructor(private val authService: AuthService) {
    var logger: Logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/signup")
    @Operation(
        summary = "Cadastro de Usuário",
        description = "Cadastro usuário usado quando não possui usuário e senha",
        tags = ["Usuários"]
    )
    @ResponseStatus(HttpStatus.OK)
    fun signup(@RequestBody request: @Valid UserDTO): ResponseEntity<ResponseSignupDTO> {
        logger.info("/signup")
        return ResponseEntity.ok(authService.signup(request))
    }
}