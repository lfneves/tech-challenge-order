package com.mvp.order.infrastruture.repository.order

import com.mvp.order.infrastruture.entity.order.OrderProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional


interface OrderProductRepository : JpaRepository<OrderProductEntity, Long> {

    @Modifying
    @Transactional
    @Query("""
        DELETE FROM tb_order_product WHERE id_order = :id
    """, nativeQuery = true)
    fun deleteByIdOrder(id: Long): Int

    @Modifying
    @Transactional
    @Query("""
        DELETE FROM tb_order_product WHERE id_order = :idOrder AND id_product IN(:productList)
    """, nativeQuery = true)
    fun deleteAllProductsByIdOrder(idOrder: Long, productList: List<Long>): Int
}