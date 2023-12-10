package com.mvp.order.domain.model.order

import com.mvp.order.infrastruture.entity.order.OrderProductEntity

data class OrderRequestDTO(
    var orderProduct : List<OrderProductDTO>,
    var username: String
) {
    fun toEntityList(): List<OrderProductEntity> {
        return this.orderProduct.map {
            return@map OrderProductEntity(
                id = it.id,
                idProduct = it.idProduct,
                idOrder = it.idOrder
            )
        }
    }
}