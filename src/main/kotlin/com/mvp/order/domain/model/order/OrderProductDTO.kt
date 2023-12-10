package com.mvp.order.domain.model.order

import com.mvp.order.infrastruture.entity.order.OrderProductEntity
import java.math.BigDecimal

data class OrderProductDTO(
    var id: Long? = null,
    var idProduct: Long? = null,
    var idOrder: Long? = null,
) {
    fun toEntity() : OrderProductEntity {
        return OrderProductEntity(
            id = this.id,
            idProduct = this.idProduct,
            idOrder = this.idOrder
        )
    }
}

data class OrderProductResponseDTO(
    var id: Long? = null,
    var idProduct: Long? = null,
    var idOrder: Long? = null,
    var productName: String? = null,
    var categoryName: String? = null,
    var price: BigDecimal = BigDecimal.ZERO
) {
    fun toEntity() : OrderProductEntity {
        return OrderProductEntity(
            id = this.id,
            idProduct = this.idProduct,
            idOrder = this.idOrder,
        )
    }
}