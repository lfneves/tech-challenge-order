package com.mvp.order.application.integration.order.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mvp.order.domain.configuration.AwsConfig
import com.mvp.order.domain.model.order.OrderByIdResponseDTO
import com.mvp.order.domain.model.order.OrderProductDTO
import com.mvp.order.domain.model.order.OrderRequestDTO
import com.mvp.order.domain.model.product.ProductRemoveOrderDTO
import com.mvp.order.domain.service.message.SnsAndSqsService
import com.mvp.order.domain.service.order.OrderService
import io.mockk.*
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit.jupiter.SpringExtension
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.PublishRequest
import software.amazon.awssdk.services.sns.model.PublishResponse
import software.amazon.awssdk.services.sqs.SqsClient
import java.math.BigDecimal
import java.util.*


@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = [
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration",
    "spring.security.enabled=false"
])
@AutoConfigureMockMvc
class OrderControllerTest {

    private val TOPIC_ORDER_SNS = System.getenv("TOPIC_ORDER_SNS") ?: "arn:aws:sns:us-east-1:111111111111:ORDER_TOPIC"

    private val awsConfig = mockk<AwsConfig>(relaxed = true)
    private val sqsClient = mockk<SqsClient>(relaxed = true)
    private val snsClient= mockk<SnsClient>(relaxed = true)
    private val orderService = mockk<OrderService>()

    private val snsAndSqsService = SnsAndSqsService(snsClient, sqsClient, awsConfig)

        @LocalServerPort
    private var port: Int = 8080

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
    }

    @Test
    @Sql(scripts = ["/sql/order.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun `getOrderById returns order when found1`() {

        val id = 1L
        val  orderByIdResponseDTO = OrderByIdResponseDTO(
            id = 1,
            externalId = UUID.fromString("4879d212-bdf1-413c-9fd1-5b65b50257bc"),
            idClient = 1,
            totalPrice = BigDecimal.TEN,
            status = "PENDING"
        )

        every { orderService.getOrderById(id) } returns orderByIdResponseDTO

        given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("/api/v1/order/$id")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    @WithMockUser(username="admin", roles=["USER", "ADMIN"])
    fun `Get All Order Products By Order ID`() {
        given()
            .get("/api/v1/order/all-products-by-order-id/{id}", 1)
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    @Sql(scripts = ["/sql/order_delete_before_insert.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun `Test Add New Product To Order with error sns`() {
        val orderProductDTO = OrderProductDTO(
            id = 1,
            idProduct = 1,
            idOrder = 1
        )
        val orderRequestDTO = OrderRequestDTO(listOf(orderProductDTO), "99999999999")

        every { awsConfig.topicArn } returns TOPIC_ORDER_SNS

        val publishRequest = PublishRequest.builder()
            .topicArn(TOPIC_ORDER_SNS)
            .message(jacksonObjectMapper().writeValueAsString(orderRequestDTO))
            .build()

        val fakeResponse = PublishResponse.builder()
            .messageId("fakeMessageId")
            .build()

        every { snsClient.publish(publishRequest) } returns fakeResponse

        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderRequestDTO)
            .`when`()
            .put("/api/v1/order/add-new-product-to-order")
    }

    @Test
    @Sql(scripts = ["/sql/order_delete_before_insert.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun `test delete order by id`() {
        val orderId = 1L

        val orderProductDTO = OrderProductDTO(
            id = 1,
            idProduct = 1,
            idOrder = 1
        )
        val orderRequestDTO = OrderRequestDTO(listOf(orderProductDTO), "99999999999")

        every { awsConfig.topicArn } returns TOPIC_ORDER_SNS

        val publishRequest = PublishRequest.builder()
            .topicArn(TOPIC_ORDER_SNS)
            .message(jacksonObjectMapper().writeValueAsString(orderRequestDTO))
            .build()

        val fakeResponse = PublishResponse.builder()
            .messageId("fakeMessageId")
            .build()

        every { snsClient.publish(publishRequest) } returns fakeResponse

        given()
            .contentType(ContentType.JSON)
            .`when`()
            .delete("/api/v1/order/$orderId")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(equalTo("{}"))
    }

    @Test
    fun `test remove product from order by id`() {
        val productRemoveOrderDTO = ProductRemoveOrderDTO(
            idOrder = 1L,
            orderProductId = mutableListOf(1L)
        )
        val jsonPayload = ObjectMapper().writeValueAsString(productRemoveOrderDTO)

        every { awsConfig.topicArn } returns TOPIC_ORDER_SNS

        given()
            .contentType(ContentType.JSON)
            .body(jsonPayload)
            .`when`()
            .delete("/api/v1/order/remove-product-order")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `test create order with error sns`() {
        try {
            val orderProductDTO = OrderProductDTO(
                id = 1,
                idProduct = 1,
                idOrder = 1
            )
            val orderRequestDTO = OrderRequestDTO(listOf(orderProductDTO), "99999999999")
            val jsonPayload = ObjectMapper().writeValueAsString(orderRequestDTO)

            every { awsConfig.topicArn } returns TOPIC_ORDER_SNS

            val publishRequest = PublishRequest.builder()
                .topicArn(TOPIC_ORDER_SNS)
                .message(jacksonObjectMapper().writeValueAsString(orderRequestDTO))
                .build()

            val fakeResponse = PublishResponse.builder()
                .messageId("fakeMessageId")
                .build()

            every { snsClient.publish(publishRequest) } returns fakeResponse

            given()
                .contentType(ContentType.JSON)
                .body(jsonPayload)
                .`when`()
                .post("/api/v1/order/create-order")
        } catch (_: Exception) {

        }
    }
}