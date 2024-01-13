package com.mvp.order.application.integration.order.service

import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.service.product.ProductService
import com.mvp.order.infrastruture.repository.product.CategoryRepository
import com.mvp.order.infrastruture.repository.product.ProductRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private lateinit var productService: ProductService
    @Autowired
    private lateinit var productRepository: ProductRepository
    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Test
    fun `getProductById returns product with category for valid ID`() {
        val validProductId = 1L

        val productDTO = productService.getProductById(validProductId)

        assertNotNull(productDTO)
        assertEquals(validProductId, productDTO.id)
        assertNotNull(productDTO.category)
    }

    @Test
    fun `getProductById throws NotFoundException for non-existent product`() {
        // Arrange: Use a non-existent product ID
        val nonExistentProductId = 999L // Use an ID that doesn't exist

        // Act & Assert: Expect an exception
        assertThrows<Exceptions.NotFoundException> {
            productService.getProductById(nonExistentProductId)
        }
    }

    @Test
    fun `getProducts returns a list of products with categories`() {
        val products = productService.getProducts()

        assertNotNull(products)
        assertFalse(products.isEmpty())
        products.forEach { product ->
            assertNotNull(product.category)

        }
    }

    @Test
    @Sql(scripts = ["/sql/product_delete_before_insert.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun `getAllById returns products for valid IDs`() {
        val validIds = listOf(1L)

        val products = productService.getAllById(validIds)

        assertNotNull(products)
        assertFalse(products.isEmpty())
        assertEquals(validIds.size, products.size)
    }

    @Test
    @Sql(scripts = ["/sql/category.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun `test getProductsByCategoryByName with valid category name`() {
        val result = productService.getProductsByCategoryByName("Bebidas")

        assertEquals(1, result.size)
    }
}