package com.mvp.order.infrastruture.repository.product

import com.mvp.order.infrastruture.entity.product.CategoryEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CategoryRepository : CrudRepository<CategoryEntity, Long> {

    @Query("SELECT id, name, description FROM tb_category WHERE name = :name", nativeQuery = true)
    fun findByName(name: String?): CategoryEntity
}