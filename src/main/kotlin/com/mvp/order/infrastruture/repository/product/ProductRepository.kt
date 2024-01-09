package com.mvp.order.infrastruture.repository.product

import com.mvp.order.infrastruture.entity.product.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<ProductEntity, Long> {
    @Query("SELECT id, name, price, quantity FROM tb_product WHERE name = :name", nativeQuery = true)
    fun findByName(name: String?): List<ProductEntity>

    @Query("SELECT id, name, price, quantity, id_category FROM tb_product WHERE id_category = :id", nativeQuery = true)
    fun findByIdCategory(id: Long?): List<ProductEntity>

    @Query("SELECT id, name, price, quantity, id_category FROM tb_product WHERE id IN(:ids)", nativeQuery = true)
    fun findAllProductById(ids: Iterable<Long>): List<ProductEntity>

    @Query("SELECT id, SUM(price) AS price FROM tb_product WHERE id IN(:ids) GROUP BY id", nativeQuery = true)
    fun findByIdTotalPrice(ids: List<Long?>): ProductEntity
}