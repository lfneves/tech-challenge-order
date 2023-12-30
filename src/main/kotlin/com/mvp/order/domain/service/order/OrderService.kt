package com.mvp.order.domain.service.order

import com.mvp.order.domain.model.order.*
import com.mvp.order.domain.model.product.ProductRemoveOrderDTO
import java.util.*

interface OrderService {

    fun getOrderById(id: Long): OrderByIdResponseDTO

    fun getOrderByExternalId(externalId: UUID): OrderByIdResponseDTO?

    fun createOrder(orderRequestDTO: OrderRequestDTO): OrderResponseDTO

    fun updateOrderProduct(orderRequestDTO: OrderRequestDTO): OrderResponseDTO

    fun deleteOrderById(id: Long)

    fun deleteOrderProductById(products: ProductRemoveOrderDTO)

    fun getAllOrderProductsByIdOrder(id: Long): List<OrderProductResponseDTO>
}