package com.mvp.order.application.v1.admin

import com.mvp.order.domain.model.auth.RemoveUserDTO
import com.mvp.order.domain.model.user.admin.UserAdminResponseDTO
import com.mvp.order.domain.service.admin.user.UserAdminService
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/users")
class UserAdminController(private val userAdminService: UserAdminService) {
    var logger: Logger = LoggerFactory.getLogger(UserAdminController::class.java)

    @GetMapping
    @Operation(
        summary = "Busca todos usuários",
        description = "Busca todos usuários cadastrados não valida usuárioa admin neste momento",
        tags = ["Administrador de Usuários"]
    )
    fun all(): ResponseEntity<List<UserAdminResponseDTO>> {
        return ResponseEntity.ok(userAdminService.getUsers())
    }

    @DeleteMapping("/delete-by-username")
    @Operation(
        summary = "Deleta usuários por username",
        description = "Deleta usuários by id não valida usuárioa admin neste momento",
        tags = ["Administrador de Usuários"]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@RequestBody request: RemoveUserDTO): ResponseEntity<Unit> {
        logger.info("Admin - delete-by-username")
        return ResponseEntity.ok(userAdminService.deleteByUsername(request))
    }

    @DeleteMapping("/delete-all")
    @Operation(
        summary = "Deleta todos usuários",
        description = "Deleta todos usuários não valida usuárioa admin neste momento - only for development uses",
        tags = ["Administrador de Usuários"]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAllUsers(authentication: Authentication): ResponseEntity<Unit> {
        logger.info("Admin - delete all users")
        return ResponseEntity.ok(userAdminService.deleteAllUsers())
    }
}