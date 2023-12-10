package com.mvp.order.domain.model.user

import com.mvp.order.infrastruture.entity.user.AddressEntity
import com.mvp.order.infrastruture.entity.user.UserEntity

data class UserDTO (
    var id: Int? = null,
    var name: String? = null,
    var email: String? = null,
    var cpf: String? = null,
    var password: String? = null,
    var idAddress: Long? = null,

    var address: AddressEntity? = AddressEntity()
) {
    fun toEntity(): UserEntity {
        return UserEntity(
            id = this.id,
            name = this.name,
            email = this.email,
            cpf = this.cpf,
            idAddress = this.idAddress,
            password = this.password
        )
    }
}