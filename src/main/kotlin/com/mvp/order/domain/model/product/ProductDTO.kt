package com.mvp.order.domain.model.product

import java.math.BigDecimal

data class ProductDTO(
    var id: Long? = null,
    var name: String = "",
    var price: BigDecimal = BigDecimal.ZERO,
    var quantity: Int = 0,
    var idCategory: Long = 0,

    var category: CategoryDTO = CategoryDTO()
)