package com.mvp.order.domain.model.product

data class ProductRemoveOrderDTO(
    var idOrder: Long,
    var orderProductId: MutableList<Long> = mutableListOf()
)
