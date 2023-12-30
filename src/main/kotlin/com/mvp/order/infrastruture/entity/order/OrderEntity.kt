package com.mvp.order.infrastruture.entity.order

import com.mvp.order.domain.model.order.OrderByIdResponseDTO
import com.mvp.order.domain.model.order.OrderDTO
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "tb_order")
data class OrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "external_id") var externalId: UUID? = null,
    @Column(name = "id_client") var idClient: Int? = null,
    @Column(name = "total_price") var totalPrice: BigDecimal = BigDecimal.ZERO,
    @Column(name = "status") var status: String = "",
    @Column(name = "waiting_time")
    var waitingTime: LocalDateTime = ZonedDateTime.now(ZoneId.of( "America/Sao_Paulo")).toLocalDateTime(),
    @Column(name = "is_finished") var isFinished: Boolean = false
) {
    fun toDTO(): OrderDTO {
        return OrderDTO(
            id = this.id,
            externalId = this.externalId,
            idClient = this.idClient,
            totalPrice = this.totalPrice,
            status = this.status,
            waitingTime = this.waitingTime,
            isFinished = this.isFinished
        )
    }

    fun toResponseDTO(): OrderByIdResponseDTO {
        return OrderByIdResponseDTO(
            id = this.id,
            externalId = this.externalId,
            idClient = this.idClient,
            totalPrice = this.totalPrice,
            status = this.status,
            waitingTime = this.waitingTime,
            isFinished = this.isFinished
        )
    }
}