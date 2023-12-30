package com.mvp.order.domain.service.order

import com.mvp.order.domain.model.order.OrderByIdResponseDTO
import com.mvp.order.domain.model.order.store.QrDataDTO
import com.mvp.order.domain.model.order.store.webhook.MerchantOrderDTO
import com.mvp.order.domain.model.order.store.webhook.MerchantOrderResponseDTO

interface MPOrderService {

    fun saveCheckoutOrderExternalStoreID(merchantOrderDTO: MerchantOrderDTO)

    fun generateOrderQrs(requestBody: String): QrDataDTO

    fun orderCheckoutGenerateQrs(order: OrderByIdResponseDTO): String

    fun getMerchantOrderByID(requestUrl: String): MerchantOrderResponseDTO

    fun checkoutOrder(username: String): QrDataDTO
}