package com.mvp.order.application.unit.product

import com.mvp.order.domain.model.product.CategoryDTO
import com.mvp.order.domain.service.client.product.ProductService
import com.mvp.order.domain.service.client.product.ProductServiceImpl
import com.mvp.order.infrastruture.entity.product.ProductEntity
import com.mvp.order.infrastruture.repository.product.CategoryRepository
import com.mvp.order.infrastruture.repository.product.ProductRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.relational.core.mapping.Column
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import scala.Product
import java.math.BigDecimal

class ProductUnitTest {

    private val productRepository = mockk<ProductRepository>()
    private val categoryRepository = mockk<CategoryRepository>()
    private val productService = ProductServiceImpl(productRepository, categoryRepository)

    @Test
    fun `getProductById returns ProductDTO when product is found`() {
        val productId = 1
        val product = ProductEntity(
            id = 1L,
            name = "Suco",
            price = BigDecimal(19.99),
            quantity = 5,
            idCategory = 1
        )
        val category = CategoryDTO(id = 1, name = "Bebidas", description = "Água, Refrigerante, Cerveja entre outros.")
        val expectedProductDTO = product.toDTO(category)

        every { productRepository.findById(productId) } returns Mono.just(product)
        every { categoryRepository.findById(product.idCategory!!) } returns Mono.just(category.toEntity())

        // When
        val result = productService.getProductById(productId)


        StepVerifier.create(result)
            .expectNext(expectedProductDTO)
            .verifyComplete()

        verify(exactly = 1) { productRepository.findById(productId) }
        verify(exactly = 1) { categoryRepository.findById(product.idCategory!!) }
    }

    @Test
    fun getProducts() {

    }

    @Test
    fun getProductsByCategoryByName(){

    }

    @Test
    fun getAllById() {

    }

    @Test
    fun getByIdTotalPrice() {

    }
}