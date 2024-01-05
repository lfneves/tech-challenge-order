package com.mvp.order.domain.model.order

import com.mvp.order.infrastruture.entity.order.OrderEntity
import com.mvp.order.infrastruture.entity.order.OrderProductEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

data class OrderDTO(
    var id: Long? = null,
    var externalId: UUID? = null,
    var idClient: Int? = null,
    var totalPrice: BigDecimal = BigDecimal.ZERO,
    var status: String = "",
    var waitingTime: LocalDateTime = ZonedDateTime.now(ZoneId.of( "America/Sao_Paulo")).toLocalDateTime(),
    var isFinished: Boolean = false,
    var productList: MutableList<OrderProductEntity> = mutableListOf()
) {
    fun toEntity(): OrderEntity{
        return OrderEntity(
            id = this.id,
            idClient = this.idClient,
            totalPrice = this.totalPrice,
            status = this.status,
            waitingTime = this.waitingTime,
            isFinished = this.isFinished
        )
    }
}