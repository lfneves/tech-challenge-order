package com.mvp.order.infrastruture.entity.order

import com.mvp.order.domain.model.order.OrderProductDTO
import com.mvp.order.domain.model.order.OrderProductResponseDTO
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "tb_order_product")
data class OrderProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var idProduct: Long? = null,
    var idOrder: Long? = null
) {
    fun toDTO() : OrderProductDTO {
        return OrderProductDTO(
            id = this.id,
            idProduct = this.idProduct,
            idOrder = this.idOrder
        )
    }
}

data class OrderProductResponseEntity(
    var id: Long? = null,
    var idProduct: Long? = null,
    var idOrder: Long? = null,
    var productName: String? = null,
    var categoryName: String? = null,
    var price: BigDecimal = BigDecimal.ZERO
) {
    fun toDTO() : OrderProductResponseDTO {
        return OrderProductResponseDTO(
            id = this.id,
            idProduct = this.idProduct,
            idOrder = this.idOrder,
            productName = this.productName,
            categoryName = this.categoryName,
            price = this.price
        )
    }
}