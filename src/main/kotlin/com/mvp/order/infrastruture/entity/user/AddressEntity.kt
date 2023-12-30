package com.mvp.order.infrastruture.entity.user

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "tb_address")
data class AddressEntity (
    @Id
    @Schema(hidden = true)
    var id: Long? = null,
    var street: String? = null,
    var city: String? = null,
    var state: String? = null,

    @Column(name = "postal_code")
    var postalCode: String? =null
) {
    fun updateUserEntity(address: AddressEntity, request: AddressEntity) {
        request.id?.let { address.id = it }
        request.street?.let { address.street = it }
        request.city?.let { address.city = it }
        request.state?.let { address.state = it }
    }
}