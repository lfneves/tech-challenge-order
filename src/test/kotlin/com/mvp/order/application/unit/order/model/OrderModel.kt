package com.mvp.order.application.unit.order.model

import com.mvp.order.domain.model.order.OrderProductDTO
import com.mvp.order.domain.model.order.OrderProductResponseDTO
import com.mvp.order.domain.service.order.OrderService
import com.mvp.order.domain.service.product.ProductService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import java.math.BigDecimal

class OrderModel {

    @Test
    fun testOrderProductMapping() {
        val orderService = mockk<OrderService>()

        val productName = "Sample Product"
        val categoryName = "Sample Category"
        val price = BigDecimal(50)

        every { orderService.getAllOrderProductsByIdOrder(any()) } returns listOf(OrderProductResponseDTO(
            id = 101,
            productName = productName,
            categoryName = categoryName,
            price = price
        ))

        val orderProductResponseDTO =  orderService.getAllOrderProductsByIdOrder(1L)

        // Assert
        assertEquals(101, orderProductResponseDTO.first().id)
        assertEquals(productName, orderProductResponseDTO.first().productName)
        assertEquals(categoryName, orderProductResponseDTO.first().categoryName)
        assertEquals(price, orderProductResponseDTO.first().price)
    }
}