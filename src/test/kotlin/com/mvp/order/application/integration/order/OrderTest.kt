package com.mvp.order.application.integration.order

import com.mvp.order.application.integration.user.UserApplicationTest
import com.mvp.order.domain.service.client.order.OrderServiceImpl
import com.mvp.order.helpers.OrderMock
import com.mvp.order.helpers.UserMock
import com.mvp.order.infrastruture.repository.order.OrderProductRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.test.web.reactive.server.WebTestClient


@SpringBootTest
class OrderTest(
    @Autowired val client: WebTestClient,
) {
    private val logger = LoggerFactory.getLogger(UserApplicationTest::class.java)


    @Autowired private lateinit var orderService: OrderServiceImpl
    @Autowired private lateinit var orderProductRepository: OrderProductRepository
    private lateinit var authentication: Authentication

    val orderApiRequestTest = OrderRequestApiTest(client)

    @BeforeEach
    fun init() {

    }

    @Test
    fun `Create Order test and validate registration expected = true`() {
        val request = OrderMock.mockOrderRequest()
        val response = orderApiRequestTest.createOrderTest(request)
        logger.info(response.toString())

        Assertions.assertThat(response.orderDTO!!.id).isNotNull
        Assertions.assertThat(response.orderDTO!!.idClient).isEqualTo(OrderMock.mockOrder().idClient)
        Assertions.assertThat(response.orderDTO!!.status).isEqualTo(OrderMock.mockOrder().status)
        Assertions.assertThat(response.orderDTO!!.isFinished).isEqualTo(OrderMock.mockOrder().isFinished)
    }

    @Test
    fun `Find Order By ID and validate expected = true`() {
        val request = UserMock.mockUser().id!!
        val response = orderApiRequestTest.getById(request)
        logger.info(response.toString())

        Assertions.assertThat(response.id).isNotNull
        Assertions.assertThat(response.idClient).isEqualTo(OrderMock.mockOrder().idClient)
        Assertions.assertThat(response.status).isEqualTo(OrderMock.mockOrder().status)
        Assertions.assertThat(response.isFinished).isEqualTo(OrderMock.mockOrder().isFinished)
    }

    @Test
    fun `Delete User By ID expected = true`() {
        val request = UserMock.mockUser().id!!
        val response = orderApiRequestTest.deleteOrderById(request)
        logger.info(response.toString())

        Assertions.assertThat(response.id).isNotNull
        Assertions.assertThat(response.idClient).isEqualTo(OrderMock.mockOrder().idClient)
        Assertions.assertThat(response.status).isEqualTo(OrderMock.mockOrder().status)
        Assertions.assertThat(response.isFinished).isEqualTo(OrderMock.mockOrder().isFinished)
    }
}