package com.mvp.order.infrastruture.entity.product

import com.mvp.order.domain.model.product.CategoryDTO
import jakarta.persistence.*

@Entity
@Table(name = "tb_category")
data class CategoryEntity(
    @Id
    var id: Long? = null,
    var name: String = "",
    var description: String = ""
) {
    fun toDTO(): CategoryDTO {
        return CategoryDTO(
            id = this.id,
            name = this.name,
            description = this.description
        )
    }
}