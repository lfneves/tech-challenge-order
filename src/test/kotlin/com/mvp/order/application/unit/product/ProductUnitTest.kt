package com.mvp.order.application.unit.product

import com.mvp.order.domain.model.product.CategoryDTO
import com.mvp.order.domain.model.product.ProductDTO
import com.mvp.order.domain.service.product.ProductServiceImpl
import com.mvp.order.infrastruture.entity.product.CategoryEntity
import com.mvp.order.infrastruture.entity.product.ProductEntity
import com.mvp.order.infrastruture.repository.product.CategoryRepository
import com.mvp.order.infrastruture.repository.product.ProductRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

class ProductUnitTest {

    private val productRepository = mockk<ProductRepository>()
    private val categoryRepository = mockk<CategoryRepository>()
    private val productService = ProductServiceImpl(productRepository, categoryRepository)

    private var productDTO = mockk<ProductDTO>()
    private var productEntity = mockk<ProductEntity>()
    private var categoryDTO = mockk<CategoryDTO>()
    private var categoryEntity = mockk<CategoryEntity>()
    private var products = mockk<List<ProductDTO>>()
    private var productsEntity = mockk<MutableList<ProductEntity>>()

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
        products = listOf(productEntity.toDTO(categoryDTO))
        productsEntity = mutableListOf(productEntity)
    }

    @Test
    fun `getProductById returns ProductDTO when product is found`() {
        val productId = 1L
        val expectedProductDTO = productEntity.toDTO(categoryDTO)

        every { productRepository.findById(productId) } returns Optional.of(productEntity)
        every { categoryRepository.findById(productEntity.idCategory!!) } returns Optional.of(categoryDTO.toEntity())

        // Act
        val result = productService.getProductById(productId)

        assertEquals(expectedProductDTO, result)
    }

    @Test
    fun getProducts() {
        every { productRepository.findAll() } returns productsEntity

        products.forEach { product ->
            val category = categoryEntity
            every { categoryRepository.findById(product.idCategory) } returns Optional.of(category)
        }

        val result = productService.getProducts()

        val expectedResponse = products.map { it }
        assertEquals(expectedResponse, result)
    }

    @Test
    fun getProductsByCategoryByName(){
        every { categoryRepository.findByName(categoryDTO.name) } returns categoryEntity
        every { productRepository.findByIdCategory(categoryDTO.id) } returns productsEntity

        val expectedResponse = products.map { it }
        val result = productService.getProductsByCategoryByName(categoryDTO.name)

        assertEquals(expectedResponse, result)
    }

    @Test
    fun getAllById() {
        val ids = listOf(1L)
        every { productRepository.findAllProductById(ids) } returns productsEntity

        products.forEach { product ->
            every { categoryRepository.findById(product.idCategory) } returns Optional.of(categoryEntity)
        }

        val expectedResponse = products.map { it }
        val result = productService.getAllById(ids)

        assertEquals(expectedResponse, result)
    }

    @Test
    fun getByIdTotalPrice() {
        val ids = listOf(1L)
        val expectedPrice = productEntity.price

        every { productRepository.findByIdTotalPrice(ids) } returns productEntity.price

        val result = productService.getByIdTotalPrice(ids)

        assertEquals(expectedPrice, result)
    }
}