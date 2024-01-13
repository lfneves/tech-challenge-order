package com.mvp.order.domain.model.order

import java.math.BigDecimal

data class OrderProductDTO(
    var id: Long? = null,
    var idProduct: Long? = null,
    var idOrder: Long? = null,
)

data class OrderProductResponseDTO(
    var id: Long? = null,
    var idProduct: Long? = null,
    var idOrder: Long? = null,
    var productName: String? = null,
    var categoryName: String? = null,
    var price: BigDecimal = BigDecimal.ZERO
)