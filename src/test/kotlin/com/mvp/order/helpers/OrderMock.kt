package com.mvp.order.helpers

import com.mvp.order.domain.model.order.OrderDTO
import com.mvp.order.domain.model.order.OrderProductDTO
import com.mvp.order.domain.model.order.OrderRequestDTO
import java.math.BigDecimal

object OrderMock {
    private const val TEST_MP_TOKEN = "Bearer TEST-29597298295284-083000-fcac7f6198671d6a6b16cdeecc34c787-170225675"
    private const val TEST_USERNAME = "99999999999"
    private const val TEST_PRODUCT_ID = 1L
    private const val TEST_CLIENT_ID = 1
    private val TEST_TOTAL_PRICE = BigDecimal.TEN
    private const val TEST_STATUS = "Pendente"
    private const val TEST_FINISHED_TRUE = true
    private const val TEST_FINISHED_FALSE = false

    fun mockOrderRequest() = OrderRequestDTO(
        orderProduct = mutableListOf(
            OrderProductDTO(
                idProduct = TEST_PRODUCT_ID
            )
        ),
        username = TEST_USERNAME
    )

    fun mockOrder() = OrderDTO(
        idClient = TEST_CLIENT_ID,
        totalPrice = TEST_TOTAL_PRICE,
        status = TEST_STATUS,
        isFinished = TEST_FINISHED_FALSE
    )
}