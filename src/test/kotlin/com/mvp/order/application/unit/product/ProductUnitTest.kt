package com.mvp.order.application.unit.product

import com.mvp.order.domain.model.product.CategoryDTO
import com.mvp.order.domain.model.product.ProductDTO
import com.mvp.order.domain.service.client.product.ProductServiceImpl
import com.mvp.order.infrastruture.entity.product.CategoryEntity
import com.mvp.order.infrastruture.entity.product.ProductEntity
import com.mvp.order.infrastruture.repository.product.CategoryRepository
import com.mvp.order.infrastruture.repository.product.ProductRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal

class ProductUnitTest {

    private val productRepository = mockk<ProductRepository>()
    private val categoryRepository = mockk<CategoryRepository>()
    private val productService = ProductServiceImpl(productRepository, categoryRepository)

    private var productDTO = mockk<ProductDTO>()
    private var productEntity = mockk<ProductEntity>()
    private var categoryDTO = mockk<CategoryDTO>()
    private var categoryEntity = mockk<CategoryEntity>()

    @BeforeEach
    fun setup() {
        productEntity = ProductEntity(
            id = 1L,
            name = "Suco",
            price = BigDecimal(19.99),
            quantity = 5,
            idCategory = 1
        )
        categoryDTO = CategoryDTO(id = 1L, name = "Bebidas", description = "Água, Refrigerante, Cerveja entre outros.")
        categoryEntity = CategoryEntity(id = 1L, name = "Bebidas", description = "Água, Refrigerante, Cerveja entre outros.")
        productDTO = productEntity.toDTO(categoryDTO)
    }

    @Test
    fun `getProductById returns ProductDTO when product is found`() {
        val productId = 1
        val expectedProductDTO = productEntity.toDTO(categoryDTO)

        every { productRepository.findById(productId) } returns Mono.just(productEntity)
        every { categoryRepository.findById(productEntity.idCategory!!) } returns Mono.just(categoryDTO.toEntity())

        // When
        val result = productService.getProductById(productId)


        StepVerifier.create(result)
            .expectNext(expectedProductDTO)
            .verifyComplete()

        verify(exactly = 1) { productRepository.findById(productId) }
        verify(exactly = 1) { categoryRepository.findById(productEntity.idCategory!!) }
    }

    @Test
    fun getProducts() {
        every { productRepository.findAll() } returns Flux.just(productEntity)
        every { categoryRepository.findById(productEntity.idCategory!!) } returns Mono.just(categoryEntity)

        StepVerifier.create(productService.getProducts())
            .expectNext(productDTO)
            .verifyComplete()
    }

    @Test
    fun getProductsByCategoryByName(){
        val ids = listOf(1L, 2L)
        val product1 = mockk<ProductEntity>()
        val product2 = mockk<ProductEntity>()
        val category1 = CategoryEntity(1L)
        val category2 = CategoryEntity(2L)
        val productDTO1 = ProductDTO(1L)
        val productDTO2 = ProductDTO(1L)
        every { productRepository.findAllProductById(ids) } returns Flux.just(product1, product2)
        every { product1.idCategory } returns 1L
        every { product2.idCategory } returns 2L
        every { categoryRepository.findById(1L) } returns Mono.just(category1)
        every { categoryRepository.findById(2L) } returns Mono.just(category2)
        every { product1.toDTO(any()) } returns productDTO1
        every { product2.toDTO(any()) } returns productDTO2

        StepVerifier.create(productService.getAllById(ids))
            .expectNext(productDTO1)
            .expectNext(productDTO2)
            .verifyComplete()
    }

    @Test
    fun getAllById() {
        val ids = listOf(1L, 2L)
        val product1 = mockk<ProductEntity>()
        val product2 = mockk<ProductEntity>()
        val category1 = mockk<CategoryEntity>()
        val category2 = mockk<CategoryEntity>()
        val productDTO1 = ProductDTO(1L)
        val productDTO2 = ProductDTO(2L)

        every { productRepository.findAllProductById(ids) } returns Flux.just(product1, product2)
        every { product1.idCategory } returns 1L
        every { product2.idCategory } returns 2L
        every { categoryRepository.findById(1L) } returns Mono.just(category1)
        every { categoryRepository.findById(2L) } returns Mono.just(category2)
        every { product1.toDTO(any()) } returns productDTO1
        every { product2.toDTO(any()) } returns productDTO2
        every { category1.toDTO() } returns CategoryDTO(1L)
        every { category2.toDTO() } returns CategoryDTO(2L)

        StepVerifier.create(productService.getAllById(ids))
            .expectNext(productDTO1)
            .expectNext(productDTO2)
            .verifyComplete()
    }

    @Test
    fun getByIdTotalPrice() {
        val ids = listOf(1L, 2L)
        val totalPrice = BigDecimal("100.00")
        val productAggregate = ProductEntity(price = totalPrice)

        every { productRepository.findByIdTotalPrice(ids) } returns Mono.just(productAggregate)

        StepVerifier.create(productService.getByIdTotalPrice(ids))
            .expectNext(totalPrice)
            .verifyComplete()
    }
}