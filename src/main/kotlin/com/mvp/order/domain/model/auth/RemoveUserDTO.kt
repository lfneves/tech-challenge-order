package com.mvp.order.domain.model.auth

import com.mvp.order.infrastruture.entity.user.AddressEntity

data class RemoveUserDTO(
    val username: String,
    val name: String,
    val addressEntity: AddressEntity
)
