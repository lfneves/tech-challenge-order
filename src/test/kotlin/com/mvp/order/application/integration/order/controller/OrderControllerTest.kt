package com.mvp.order.application.integration.order.controller

import com.mvp.order.domain.model.order.OrderProductDTO
import com.mvp.order.domain.model.order.OrderRequestDTO
import io.restassured.RestAssured.given
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql


@Profile("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Sql(scripts = ["/sql/order.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class OrderControllerTest {


    @Test
    fun `getOrderById returns order when found1`() {
        val orderId = 1

        given()
            .`when`()
            .get("/api/v1/order/$orderId")
            .then()
            .statusCode(200)
            .body("id", Matchers.equalTo(orderId))
    }

    @Test
    fun `Get All Order Products By Order ID`() {
        given()
            .get("/api/v1/order/all-products-by-order-id/{id}", 1)
            .then()
            .statusCode(200)
    }

    @Test
    fun `Test Add New Product To Order`() {
        val orderProductDTO = OrderProductDTO(
            id = 1,
            idProduct = 1,
            idOrder = 1
        )
        val orderRequestDTO = OrderRequestDTO(listOf(orderProductDTO), "99999999999")
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderRequestDTO)
            .`when`()
            .put("/api/v1/order/add-new-product-to-order")
            .then()
            .statusCode(200)
    }
}