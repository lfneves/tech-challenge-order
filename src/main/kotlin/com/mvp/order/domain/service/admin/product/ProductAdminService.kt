package com.mvp.order.domain.service.admin.product

import com.mvp.order.domain.model.product.CategoryDTO
import com.mvp.order.domain.model.product.ProductDTO
import com.mvp.order.domain.model.product.ProductRequestDTO

interface ProductAdminService {
    fun saveProduct(productDTO: ProductDTO): ProductDTO

    fun deleteProductById(id: Long)

    fun getProducts(): List<ProductDTO>

    fun getAllCategory(): List<CategoryDTO>

    fun deleteAllProducts()

    fun updateProduct(id: Long, productRequestDTO: ProductRequestDTO): ProductDTO
}