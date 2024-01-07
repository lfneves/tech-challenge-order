package com.mvp.order.application.integration.order

import com.mvp.order.domain.service.message.SnsService
import com.mvp.order.domain.service.order.OrderServiceImpl
import com.mvp.order.domain.service.product.ProductService
import com.mvp.order.domain.service.product.ProductServiceImpl
import com.mvp.order.domain.service.user.UserService
import com.mvp.order.domain.service.user.UserServiceImpl
import com.mvp.order.helpers.OrderMock
import com.mvp.order.helpers.UserMock
import com.mvp.order.infrastruture.repository.order.OrderProductRepository
import com.mvp.order.infrastruture.repository.order.OrderProductResponseRepository
import com.mvp.order.infrastruture.repository.order.OrderRepository
import io.restassured.RestAssured.given
import org.assertj.core.api.Assertions
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.transaction.annotation.Transactional


@SpringBootTest
class OrderTest(
//    @Autowired val client: WebTestClient,
) {
    private val logger = LoggerFactory.getLogger(OrderTest::class.java)


    @Autowired private lateinit var orderService: OrderServiceImpl
    @Autowired private lateinit var orderProductRepository: OrderProductRepository
    @Autowired private lateinit var orderProductResponseRepository: OrderProductResponseRepository
    @Autowired private lateinit var snsService: SnsService
    @Autowired private lateinit var orderRepository: OrderRepository
    @Autowired private lateinit var productService: ProductServiceImpl
    @Autowired private lateinit var userService: UserServiceImpl

//    val orderApiRequestTest = OrderRequestApiTest(client)

    @BeforeEach
    fun init() {
        orderService = OrderServiceImpl(snsService, orderRepository, orderProductRepository, orderProductResponseRepository, productService, userService)
    }

    @Test
    @Transactional
    fun `getOrderById returns order when found`() {
        val testOrderId = 1L

        val result = orderService.getOrderById(testOrderId)

        assertNotNull(result)
        assertEquals(testOrderId, result.id)
    }

    @Test
    fun `getOrderById returns order when found1`() {
        val orderId = 1L // Replace with a valid order ID

        given()
            .`when`()
            .get("/orders/$orderId") // Replace with your actual endpoint
            .then()
            .statusCode(200)
            .body("id", equalTo(orderId.toInt()))
        // Add more checks as needed
    }

//
//    @Test
//    fun `Create Order test and validate registration expected = true`() {
//        val request = OrderMock.mockOrderRequest()
//        val response = orderApiRequestTest.createOrderTest(request)
//        logger.info(response.toString())
//
//        Assertions.assertThat(response.orderDTO!!.id).isNotNull
//        Assertions.assertThat(response.orderDTO!!.idClient).isEqualTo(OrderMock.mockOrder().idClient)
//        Assertions.assertThat(response.orderDTO!!.status).isEqualTo(OrderMock.mockOrder().status)
//        Assertions.assertThat(response.orderDTO!!.isFinished).isEqualTo(OrderMock.mockOrder().isFinished)
//    }
//
//    @Test
//    fun `Find Order By ID and validate expected = true`() {
//        val request = UserMock.mockUser().id!!
//        val response = orderApiRequestTest.getById(request)
//        logger.info(response.toString())
//
//        Assertions.assertThat(response.id).isNotNull
//        Assertions.assertThat(response.idClient).isEqualTo(OrderMock.mockOrder().idClient)
//        Assertions.assertThat(response.status).isEqualTo(OrderMock.mockOrder().status)
//        Assertions.assertThat(response.isFinished).isEqualTo(OrderMock.mockOrder().isFinished)
//    }
//
//    @Test
//    fun `Delete User By ID expected = true`() {
//        val request = UserMock.mockUser().id!!
//        val response = orderApiRequestTest.deleteOrderById(request)
//        logger.info(response.toString())
//
//        Assertions.assertThat(response.id).isNotNull
//        Assertions.assertThat(response.idClient).isEqualTo(OrderMock.mockOrder().idClient)
//        Assertions.assertThat(response.status).isEqualTo(OrderMock.mockOrder().status)
//        Assertions.assertThat(response.isFinished).isEqualTo(OrderMock.mockOrder().isFinished)
//    }
}