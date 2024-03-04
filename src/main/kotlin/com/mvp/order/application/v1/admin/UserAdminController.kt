package com.mvp.order.application.v1.admin

import com.mvp.order.domain.model.user.UserDTO
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
    fun all(): ResponseEntity<List<UserDTO>> {
        return ResponseEntity.ok(userAdminService.getUsers())
    }

    @DeleteMapping("/delete-by-id/{id}")
    @Operation(
        summary = "Deleta usuários por id",
        description = "Deleta usuários by id não valida usuárioa admin neste momento",
        tags = ["Administrador de Usuários"]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: Long, authentication: Authentication): ResponseEntity<Unit> {
        logger.info("Admin - delete-by-id")
        return ResponseEntity.ok(userAdminService.deleteById(id))
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