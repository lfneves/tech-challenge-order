package com.mvp.order.domain.service.admin.order

import com.mvp.order.domain.model.order.OrderDTO
import com.mvp.order.domain.model.order.OrderFinishDTO
import com.mvp.order.domain.model.order.OrderStatusDTO
import org.springframework.security.core.Authentication

interface OrderAdminService {

    fun updateOrderStatus(id: Long, orderStatusDTO: OrderStatusDTO, authentication: Authentication): OrderDTO

    fun getOrders(): List<OrderDTO?>

    fun deleteAllOrders()

    fun updateOrderFinishedAndStatus(orderFinishDTO: OrderFinishDTO, authentication: Authentication): OrderDTO
}