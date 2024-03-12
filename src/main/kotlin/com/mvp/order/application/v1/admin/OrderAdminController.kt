package com.mvp.order.application.v1.admin


import com.mvp.order.domain.model.order.OrderDTO
import com.mvp.order.domain.model.order.OrderFinishDTO
import com.mvp.order.domain.model.order.OrderStatusDTO
import com.mvp.order.domain.service.admin.order.OrderAdminService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/order")
class OrderAdminController(private val orderAdminService: OrderAdminService) {

    @GetMapping
    @Operation(
        summary = "Busca todos pedidos",
        description = "Busca todos pedidos",
        tags = ["Admin Pedidos"]
    )
    fun all(): ResponseEntity<List<OrderDTO?>> {
        return ResponseEntity.ok(orderAdminService.getOrders())
    }

    @PutMapping("/update-order-status/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
        summary = "Atualiza o status do pedido",
        description = "Altera o status do pedido exemplo: PENDING, PREPARING, PAID, FINISHED",
        tags = ["Admin Pedidos"]
    )
    fun updateOrderStatus(@PathVariable id: Long, @RequestBody orderStatusDTO: OrderStatusDTO, authentication: Authentication): ResponseEntity<OrderDTO> {
        return ResponseEntity.ok(orderAdminService.updateOrderStatus(id, orderStatusDTO, authentication))
    }

    @PutMapping("/finish-order")
    @Operation(
        summary = "Finaliza o pedido atualizando os status",
        description = "Executa update dos status e fecha o pedido",
        tags = ["Admin Pedidos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateOrderFinishedAndStatus(@RequestBody orderFinishDTO: OrderFinishDTO, authentication: Authentication): ResponseEntity<OrderDTO> {
        return ResponseEntity.ok(orderAdminService.updateOrderFinishedAndStatus(orderFinishDTO, authentication))
    }
}