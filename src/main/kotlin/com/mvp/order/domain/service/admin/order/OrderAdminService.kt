package com.mvp.order.domain.service.admin.order

import com.mvp.order.domain.model.order.OrderDTO
import com.mvp.order.domain.model.order.OrderFinishDTO
import com.mvp.order.domain.model.order.OrderStatusDTO
import org.springframework.security.core.Authentication
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface OrderAdminService {

    fun updateOrderStatus(id: Long, orderStatusDTO: OrderStatusDTO, authentication: Authentication): Mono<OrderDTO>

    fun getOrders(): Flux<OrderDTO>

    fun deleteAllOrders(): Mono<Void>

    fun updateOrderFinishedAndStatus(orderFinishDTO: OrderFinishDTO, authentication: Authentication): Mono<OrderDTO>
}