package com.mvp.order.application.integration.order.controller

import io.restassured.RestAssured
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

class OrderControllerTest {

    @Test
    fun `getOrderById returns order when found1`() {
        val orderId = 1L

        RestAssured.given()
            .`when`()
            .get("/orders/$orderId")
            .then()
            .statusCode(200)
            .body("id", Matchers.equalTo(orderId.toInt()))
    }
}