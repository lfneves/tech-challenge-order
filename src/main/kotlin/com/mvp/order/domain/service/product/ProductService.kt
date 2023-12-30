package com.mvp.order.domain.service.product

import com.mvp.order.domain.model.product.ProductDTO
import java.math.BigDecimal

interface ProductService {

    fun getProductById(id: Int): ProductDTO

    fun getProducts(): List<ProductDTO>

    fun getProductsByCategoryByName(name: String): List<ProductDTO>

    fun getAllById(id: List<Long>): List<ProductDTO>

    fun getByIdTotalPrice(ids: List<Long?>): BigDecimal
}