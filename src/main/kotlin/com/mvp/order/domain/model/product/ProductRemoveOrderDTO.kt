package com.mvp.order.domain.model.product

data class ProductRemoveOrderDTO(
    var username: String,
    var orderProductId: MutableList<Long> = mutableListOf()
)
