package com.mvp.order.infrastruture.repository.order

import com.mvp.order.infrastruture.entity.order.OrderProductResponseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface OrderProductResponseRepository : JpaRepository<OrderProductResponseEntity, Long> {

    @Query("""
        SELECT tb_order_product.id, 
               tb_order_product.id_product, 
               tb_order_product.id_order, 
               tb_product.name AS productName, 
               tb_category.name AS categoryName, 
               tb_product.price
        FROM tb_order_product
        INNER JOIN tb_order ON tb_order.id = tb_order_product.id_order
        INNER JOIN tb_product ON tb_product.id = tb_order_product.id_product
        INNER JOIN tb_category ON tb_category.id = tb_product.id_category
        WHERE tb_order_product.id_order = :id
    """, nativeQuery = true)
    fun findAllByIdOrderInfo(@Param("id") id: Long): List<OrderProductResponseEntity>
}