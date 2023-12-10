package com.mvp.order.application.v1


import com.mvp.order.domain.model.order.store.QrDataDTO
import com.mvp.order.domain.model.order.store.webhook.MerchantOrderDTO
import com.mvp.order.domain.service.client.order.MPOrderService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/mp-order")
class MPOrderController(private val mpOrderService: MPOrderService) {

    @PostMapping("/qr-code-checkout")
    @Operation(
        summary = "Efetua o pagamento atualizando os status",
        description = "Efetua o pagamento atualizando os status",
        tags = ["Pedidos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun checkoutOrder(username: String): ResponseEntity<Mono<QrDataDTO>> {
        return ResponseEntity.ok(mpOrderService.checkoutOrder(username))
    }

    @PostMapping("/webhook")
    @Operation(
        summary = "Recebe chamadas do Mercado Pago",
        description = "Efetua atualiza o status de pagamento",
        tags = ["Pedidos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun handlerWebHook(@RequestBody merchantOrderDTO: MerchantOrderDTO): ResponseEntity<Any> {
        return ResponseEntity.ok(mpOrderService.saveCheckoutOrderExternalStoreID(merchantOrderDTO))
    }
}