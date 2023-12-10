package com.mvp.order.domain.service.client.order

import com.mvp.order.domain.model.order.*
import com.mvp.order.domain.model.product.ProductRemoveOrderDTO
import org.springframework.security.core.Authentication
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

interface OrderService {

    fun getOrderById(id: Long): Mono<OrderByIdResponseDTO>

    suspend fun getOrderByExternalId(externalId: UUID): OrderByIdResponseDTO?

    fun createOrder(orderRequestDTO: OrderRequestDTO): Mono<OrderResponseDTO>

    fun updateOrderProduct(orderRequestDTO: OrderRequestDTO): Mono<OrderResponseDTO>

    fun deleteOrderById(id: Long): Mono<Void>

    fun deleteOrderProductById(products: ProductRemoveOrderDTO): Mono<Void>

    fun getAllOrderProductsByIdOrder(id: Long): Flux<OrderProductResponseDTO>

    fun fakeCheckoutOrder(orderCheckoutDTO: OrderCheckoutDTO): Mono<Boolean>

}