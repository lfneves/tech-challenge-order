package com.mvp.order.domain.service.client.order

import com.mvp.order.domain.model.order.OrderByIdResponseDTO
import com.mvp.order.domain.model.order.store.QrDataDTO
import com.mvp.order.domain.model.order.store.webhook.MerchantOrderDTO
import com.mvp.order.domain.model.order.store.webhook.MerchantOrderResponseDTO
import org.springframework.security.core.Authentication
import reactor.core.publisher.Mono

interface MPOrderService {

    suspend fun saveCheckoutOrderExternalStoreID(merchantOrderDTO: MerchantOrderDTO): Mono<Void>

    fun generateOrderQrs(requestBody: String): Mono<QrDataDTO>

    fun orderCheckoutGenerateQrs(order: OrderByIdResponseDTO): String

    fun getMerchantOrderByID(requestUrl: String): Mono<MerchantOrderResponseDTO>

    fun checkoutOrder(username: String): Mono<QrDataDTO>
}