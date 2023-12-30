package com.mvp.order.infrastruture.repository.order

import com.mvp.order.domain.model.order.OrderProductResponseDTO
import com.mvp.order.infrastruture.entity.order.OrderProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


interface OrderProductRepository : JpaRepository<OrderProductEntity?, Long?> {

    @Query("""
        SELECT tb_order_product.id, id_product AS idProduct, id_order AS idOrder, tb_product.name AS productName, tb_product.price, 
            tb_category.name AS categoryName
        FROM tb_order_product
        INNER JOIN tb_order ON tb_order.id = tb_order_product.id_order
        INNER JOIN tb_product ON tb_product.id = tb_order_product.id_product
        INNER JOIN tb_category ON tb_product.id_category = tb_category.id
        WHERE tb_order_product.id_order = :id
    """, nativeQuery = true)
    fun findAllByIdOrderInfo(id: Long): List<OrderProductResponseDTO>

    fun deleteByIdOrder(id: Long)
}