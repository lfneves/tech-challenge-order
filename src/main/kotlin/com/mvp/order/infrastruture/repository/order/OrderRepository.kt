package com.mvp.order.infrastruture.repository.order

import com.mvp.order.infrastruture.entity.order.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderRepository : JpaRepository<OrderEntity, Long> {

    @Query("""
        SELECT tb_order.id, tb_order.external_id, id_client, SUM(price) AS total_price, status, is_finished, waiting_time
         FROM tb_order 
         INNER JOIN tb_client ON tb_client.id = tb_order.id_client
         INNER JOIN tb_order_product ON tb_order_product.id_order = tb_order.id
         INNER JOIN tb_product ON tb_product.id = tb_order_product.id_product
         WHERE tb_client.cpf = :username
         GROUP BY tb_order.id, id_client, status, is_finished
    """, nativeQuery = true)
    fun findByUsername(username: String?): OrderEntity

    @Query("""
        SELECT tb_order.id, tb_order.external_id, id_client, SUM(price) AS total_price, status, is_finished, waiting_time
         FROM tb_order 
         INNER JOIN tb_client ON tb_client.id = tb_order.id_client
         LEFT JOIN tb_order_product ON tb_order_product.id_order = tb_order.id
         LEFT JOIN tb_product ON tb_product.id = tb_order_product.id_product
         WHERE tb_client.cpf = :username
            OR tb_client.email = :username
         GROUP BY tb_order.id, id_client, status, is_finished
    """, nativeQuery = true)
    fun findByUsernameIfExists(username: String?): Optional<OrderEntity>

    @Query("""
        SELECT tb_order.id, tb_order.external_id, id_client, SUM(price) AS total_price, status, is_finished, waiting_time
         FROM tb_order 
         INNER JOIN tb_client ON tb_client.id = tb_order.id_client
         INNER JOIN tb_order_product ON tb_order_product.id_order = tb_order.id
         INNER JOIN tb_product ON tb_product.id = tb_order_product.id_product
         WHERE tb_order.id = :id
         GROUP BY tb_order.id, id_client, status, is_finished
    """, nativeQuery = true)
    fun findByIdOrder(id: Long): Optional<OrderEntity>

    @Query("""
        SELECT tb_order.id, tb_order.external_id, id_client, SUM(price) AS total_price, status, is_finished, waiting_time
         FROM tb_order 
         INNER JOIN tb_client ON tb_client.id = tb_order.id_client
         INNER JOIN tb_order_product ON tb_order_product.id_order = tb_order.id
         INNER JOIN tb_product ON tb_product.id = tb_order_product.id_product
         WHERE tb_order.external_id = CAST(:externalId AS UUID)
         GROUP BY tb_order.id, id_client, status, is_finished
    """, nativeQuery = true)
    fun findByExternalId(@Param("externalId") externalId: String): Optional<OrderEntity>

    @Modifying
    @Query(value = "UPDATE tb_order SET status = :status WHERE id = :id", nativeQuery = true)
    fun updateStatus(id: Long, status: String)
}