package com.mvp.order.application.v1


import com.mvp.order.domain.model.order.*
import com.mvp.order.domain.model.product.ProductRemoveOrderDTO
import com.mvp.order.domain.service.client.order.OrderService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/order")
class OrderController(private val orderService: OrderService) {

    @PostMapping("/create-order")
    @Operation(
        summary = "Inicia um pedido",
        description = "Inicia um pedido informando e adiciona produdos pelo id",
        tags = ["Pedidos"]
    )
    fun createOrder(@RequestBody orderRequestDTO: OrderRequestDTO): ResponseEntity<Mono<OrderResponseDTO>> {
        return ResponseEntity.ok(orderService.createOrder(orderRequestDTO))
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Busca pedido id",
        description = "Busca pedido por id",
        tags = ["Pedidos"]
    )
    fun getOrderById(@PathVariable id: Long): ResponseEntity<Mono<OrderByIdResponseDTO>> {
        return ResponseEntity.ok(orderService.getOrderById(id)
            .defaultIfEmpty(OrderByIdResponseDTO())
        )
    }

    @GetMapping("all-products-by-order-id/{id}")
    @Operation(
        summary = "Busca todos os produtos id do pedido",
        description = "Busca os produtos que estão associados por um pedido pelo id ",
        tags = ["Pedidos"]
    )
    fun getAllOrderProductsByIdOrder(@PathVariable id: Long):  ResponseEntity<Flux<OrderProductResponseDTO>> {
        return  ResponseEntity.ok(
            orderService.getAllOrderProductsByIdOrder(id)
            .defaultIfEmpty(OrderProductResponseDTO())
        )
    }

    @PutMapping("/add-new-product-to-order")
    @Operation(
        summary = "Adiciona iten(s) ao pedido",
        description = "Adiciona iten(s) ao pedido recebe por uma lista de id(s) do(s) produto(s)",
        tags = ["Pedidos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateOrderProduct(@RequestBody orderRequestDTO: OrderRequestDTO): ResponseEntity<Mono<OrderResponseDTO>> {
        return ResponseEntity.ok(orderService.updateOrderProduct(orderRequestDTO))
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Remove pedido pelo id",
        description = "Remove um pedido e todos produtos pelo id",
        tags = ["Pedidos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun deleteOrder(@PathVariable id: Long): ResponseEntity<Mono<Void>> {
        return ResponseEntity.ok(orderService.deleteOrderById(id))
    }

    @DeleteMapping("/remove-product-order")
    @Operation(
        summary = "Remove produto de um pedido",
        description = "Remove um produto do pedido informado pelo id",
        tags = ["Pedidos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun deleteOrderProductById(@RequestBody productRemoveOrderDTO: ProductRemoveOrderDTO): ResponseEntity<Mono<Void>> {
        return ResponseEntity.ok(orderService.deleteOrderProductById(productRemoveOrderDTO))
    }

    @PutMapping("/fake-checkout")
    @Operation(
        summary = "Efetua o pagamento atualizando os status",
        description = "Efetua o pagamento atualizando os status",
        tags = ["Pedidos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun checkoutOrder(@RequestBody orderCheckoutDTO: OrderCheckoutDTO): ResponseEntity<Mono<Boolean>> {
        return ResponseEntity.ok(orderService.fakeCheckoutOrder(orderCheckoutDTO))
    }
}