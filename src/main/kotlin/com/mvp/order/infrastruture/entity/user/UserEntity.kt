package com.mvp.order.infrastruture.entity.user

import com.mvp.order.domain.model.user.UserDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*


@Entity
@Table(name = "tb_client")
data class UserEntity (
    @Id
    @Schema(hidden = true)
    var id: Int? = null,
    var name: String? = null,
    var email: String? = null,
    var cpf: String? = null,
    var password: String? = null,

    @Column(name = "id_address")
    @Schema(hidden = true)
    var idAddress: Long? = null,
) {
    fun toDTO(): UserDTO {
        return UserDTO(
            id = this.id,
            name = this.name,
            email = this.email,
            cpf = this.cpf,
            idAddress = this.idAddress,
            password = this.password,
            address = null
        )
    }

    fun toDTO(address: AddressEntity? = null): UserDTO {
        return UserDTO(
            id = this.id,
            name = this.name,
            email = this.email,
            cpf = this.cpf,
            idAddress = this.idAddress,
            password = this.password,
            address = address
        )
    }
}