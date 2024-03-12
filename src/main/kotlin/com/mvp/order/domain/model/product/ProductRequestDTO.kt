package com.mvp.order.domain.model.product

import com.mvp.order.infrastruture.entity.product.ProductEntity
import java.math.BigDecimal

data class ProductRequestDTO(
    var id: Long? = null,
    var name: String = "",
    var price: BigDecimal = BigDecimal.ZERO,
    var quantity: Int = 0,
    var idCategory: Long = 0
) {
    fun toEntity(): ProductEntity {
        return ProductEntity(
            id = this.id,
            name = this.name,
            price = this.price,
            quantity = this.quantity,
            idCategory = this.idCategory
        )
    }
}