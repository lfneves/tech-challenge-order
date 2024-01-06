package com.mvp.order.domain.service.order

import com.mvp.order.domain.model.order.*
import com.mvp.order.domain.model.product.ProductRemoveOrderDTO
import com.mvp.order.infrastruture.entity.order.OrderProductEntity
import java.util.*

interface OrderService {

    fun getOrderById(id: Long): OrderByIdResponseDTO

    fun findAllByIdOrderInfo(id: Long): List<OrderProductResponseDTO>

    fun getOrderByExternalId(externalId: UUID): OrderByIdResponseDTO?

    fun createOrder(orderRequestDTO: OrderRequestDTO): OrderResponseDTO

    fun updateOrderProduct(orderRequestDTO: OrderRequestDTO): OrderResponseDTO

    fun deleteOrderById(id: Long)

    fun deleteOrderProductById(products: ProductRemoveOrderDTO)

    fun getAllOrderProductsByIdOrder(id: Long): List<OrderProductResponseDTO>

    fun saveAllOrderProduct(data: List<OrderProductEntity>): List<OrderProductEntity>?
}