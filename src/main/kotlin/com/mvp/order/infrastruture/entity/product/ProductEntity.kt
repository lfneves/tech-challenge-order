package com.mvp.order.infrastruture.entity.product

import com.mvp.order.domain.model.product.CategoryDTO
import com.mvp.order.domain.model.product.ProductDTO
import jakarta.persistence.*
import java.math.BigDecimal


@Entity
@Table(name = "tb_product")
data class ProductEntity(
    @Id
    var id: Long? = null,
    var name: String = "",
    var price: BigDecimal = BigDecimal.ZERO,
    var quantity: Int = 0,

    @Column(name = "id_category")
    var idCategory: Long? = null
) {
    fun toDTO(): ProductDTO {
        return ProductDTO(
            id = this.id,
            name = this.name,
            price = this.price,
            quantity = this.quantity,
            idCategory = this.idCategory ?: 0
        )
    }

    fun toDTO(category: CategoryDTO?): ProductDTO {
        return ProductDTO(
            id = this.id,
            name = this.name,
            price = this.price,
            quantity = this.quantity,
            idCategory = this.idCategory!!,
            category = category!!
        )
    }

    fun updateUserEntity(productEntity: ProductEntity, request: ProductEntity) {
        request.id?.let { productEntity.id = it }
        request.name.let { productEntity.name = it }
        request.price.let { productEntity.price = it }
        request.quantity.let { productEntity.quantity = it }
        request.idCategory?.let { productEntity.idCategory = it }
    }

    fun toListDTO(listEntity: List<ProductEntity>): List<ProductDTO> {
        var listDTO: MutableList<ProductDTO> = mutableListOf()
        listEntity.forEach {
            listDTO.add(
                ProductDTO(
                    id = it.id,
                    name = it.name,
                    price = it.price,
                    quantity = it.quantity,
                    idCategory = it.idCategory!!
                )
            )
        }
        return listDTO.toList()
    }
}
