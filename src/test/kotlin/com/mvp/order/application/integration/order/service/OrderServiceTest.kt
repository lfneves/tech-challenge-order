package com.mvp.order.application.integration.order.service

import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.model.order.OrderDTO
import com.mvp.order.domain.model.order.OrderProductDTO
import com.mvp.order.domain.model.order.OrderRequestDTO
import com.mvp.order.domain.model.product.ProductRemoveOrderDTO
import com.mvp.order.domain.service.message.SnsService
import com.mvp.order.domain.service.order.OrderServiceImpl
import com.mvp.order.domain.service.product.ProductServiceImpl
import com.mvp.order.domain.service.user.UserServiceImpl
import com.mvp.order.infrastruture.entity.order.OrderProductEntity
import com.mvp.order.infrastruture.repository.order.OrderProductRepository
import com.mvp.order.infrastruture.repository.order.OrderProductResponseRepository
import com.mvp.order.infrastruture.repository.order.OrderRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*


@SpringBootTest
class OrderServiceTest{
    private val logger = LoggerFactory.getLogger(OrderServiceTest::class.java)

    @Autowired private lateinit var orderService: OrderServiceImpl
    @Autowired private lateinit var orderProductRepository: OrderProductRepository
    @Autowired private lateinit var orderProductResponseRepository: OrderProductResponseRepository
    @Autowired private lateinit var snsService: SnsService
    @Autowired private lateinit var orderRepository: OrderRepository
    @Autowired private lateinit var productService: ProductServiceImpl
    @Autowired private lateinit var userService: UserServiceImpl


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
    @Sql(scripts = ["/sql/order.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun `findAllByIdOrderInfo should return correct data`() {
        val testOrderId = 1L
        val orderDTO = OrderDTO(
            id = 1,
            externalId = UUID.fromString("cdfe4e61-ec36-4a7c-933e-d49d75af963e"),
            idClient = 1,
            totalPrice = BigDecimal.TEN,
            status = "PENDING",
            waitingTime = ZonedDateTime.now(ZoneId.of( "America/Sao_Paulo")).toLocalDateTime(),
            isFinished = false
        )

        val result = orderService.findAllByIdOrderInfo(orderDTO.id!!)

        assertNotNull(result)
        assertEquals(orderDTO.id, result.get(0).idOrder)
    }

    @Test
    @Sql(scripts = ["/sql/order.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun `getOrderByExternalId returns correct order data for valid UUID`() {
        val validUUID = UUID.fromString("cdfe4e61-ec36-4a7c-933e-d49d75af963e") // replace with actual UUID

        val result = orderService.getOrderByExternalId(validUUID)

        assertNotNull(result)
        assertEquals(validUUID, result?.externalId)
    }

    @Test
    fun `createOrder creates and returns a valid order`() {
        val orderProductDTO = OrderProductDTO(
            id = 1,
            idProduct = 1,
            idOrder = 1
        )
        val orderRequestDTO = OrderRequestDTO(listOf(orderProductDTO), "99999999999")

        val result = orderService.createOrder(orderRequestDTO)

        assertNotNull(result)
    }

    @Test
    fun `createOrder throws NotFoundException for invalid products`() {
        val orderProductDTO = OrderProductDTO(
            id = 1,
            idProduct = 1,
            idOrder = 1
        )
        val orderRequestDTO = OrderRequestDTO(listOf(orderProductDTO), "1")

        assertThrows<Exceptions.NotFoundException> {
            orderService.createOrder(orderRequestDTO)
        }
    }

    @Test
    fun `updateOrderProduct updates existing order`() {
        val orderProductDTO = OrderProductDTO(
            id = 1,
            idProduct = 1,
            idOrder = 1
        )
        val orderRequestDTO = OrderRequestDTO(listOf(orderProductDTO), "99999999999")

        val result = orderService.updateOrderProduct(orderRequestDTO)

        assertNotNull(result)
    }

    @Test
    @Sql(scripts = ["/sql/order_delete_before_insert.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun `saveAllOrderProduct saves and returns list of order products`() {
        val testData = listOf(OrderProductEntity(id = 1L, idProduct = 1L, idOrder = 9999L))

        val result = orderService.saveAllOrderProduct(testData)

        assertNotNull(result)
        if (result != null) {
            assertEquals(testData.size, result.size)
        }
    }

    @Test
    @Sql(scripts = ["/sql/order_delete_before_insert.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun `getAllOrderProductsByIdOrder returns products for existing order`() {
        val existingOrderId = 9999L

        val result = orderService.getAllOrderProductsByIdOrder(existingOrderId)

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `getAllOrderProductsByIdOrder throws NotFoundException for non-existent order`() {
        val nonExistentOrderId = 999L

        assertThrows<Exceptions.NotFoundException> {
            orderService.getAllOrderProductsByIdOrder(nonExistentOrderId)
        }
    }

    @Test
    @Sql(scripts = ["/sql/order.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun `deleteOrderById successfully deletes an order`() {
        val orderId = 1L

        orderService.deleteOrderById(orderId)

        assertFalse(orderRepository.existsById(orderId))
    }

    @Test
    @Sql(scripts = ["/sql/order.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun `deleteOrderProductById successfully deletes specified products`() {
        val productIds = mutableListOf(1L)

        orderService.deleteOrderProductById(ProductRemoveOrderDTO(1, productIds))

        productIds.forEach { id ->
            assertFalse(orderProductRepository.existsById(id))
        }
    }
}