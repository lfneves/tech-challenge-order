package com.mvp.order.domain.model.product

import java.math.BigDecimal

data class ProductTotalPriceDTO(
    var totalPrice: BigDecimal = BigDecimal.ZERO
)