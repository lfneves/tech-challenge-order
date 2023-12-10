package com.mvp.order.domain.model.product

data class ProductRemoveOrderDTO(
    var orderProductId: MutableList<Long> = mutableListOf()
)
