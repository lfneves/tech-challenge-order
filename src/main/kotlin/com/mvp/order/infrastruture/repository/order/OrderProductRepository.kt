package com.mvp.order.infrastruture.repository.order

import com.mvp.order.infrastruture.entity.order.OrderProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional


interface OrderProductRepository : JpaRepository<OrderProductEntity, Long> {

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
    fun findAllByIdOrderInfo(@Param("id") id: Long): List<Any>

    @Modifying
    @Transactional
    @Query("""
        DELETE FROM tb_order_product WHERE id_order = :id
    """, nativeQuery = true)
    fun deleteByIdOrder(id: Long): Int
}