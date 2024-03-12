package com.mvp.order.application.integration.order.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mvp.order.domain.model.product.CategoryDTO
import com.mvp.order.domain.service.product.ProductService
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension


@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = [
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration",
    "spring.security.enabled=false"
])
@AutoConfigureMockMvc
class ProductControllerTest {

    private val productService = mockk<ProductService>()

    @LocalServerPort
    private var port: Int = 8080

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
    }

    @Test
    fun `test get all products`() {
        given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("/api/v1/product")
            .then()
            .statusCode(HttpStatus.OK.value())
        // Add more assertions as necessary
    }

    @Test
    fun `test get product by id`() {
        val productId = 1L

        given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("/api/v1/product/$productId")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `test get products by category name`() {
        val category = CategoryDTO(name = "Acompanhamentos")
        val jsonPayload = ObjectMapper().writeValueAsString(category)

        given()
            .contentType(ContentType.JSON)
            .body(jsonPayload)
            .`when`()
            .get("/api/v1/product/get-by-category-name")
            .then()
            .statusCode(HttpStatus.OK.value())
    }
}