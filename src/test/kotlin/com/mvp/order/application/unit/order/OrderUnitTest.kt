package com.mvp.order.application.unit.order

import com.fasterxml.jackson.databind.ObjectMapper
import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.model.order.*
import com.mvp.order.domain.model.product.CategoryDTO
import com.mvp.order.domain.model.product.ProductDTO
import com.mvp.order.domain.model.product.ProductRemoveOrderDTO
import com.mvp.order.domain.model.user.UserDTO
import com.mvp.order.domain.service.message.SnsService
import com.mvp.order.domain.service.order.OrderService
import com.mvp.order.domain.service.order.OrderServiceImpl
import com.mvp.order.domain.service.product.ProductServiceImpl
import com.mvp.order.domain.service.user.UserServiceImpl
import com.mvp.order.infrastruture.entity.order.OrderEntity
import com.mvp.order.infrastruture.entity.order.OrderProductEntity
import com.mvp.order.infrastruture.entity.order.OrderProductResponseEntity
import com.mvp.order.infrastruture.repository.order.OrderProductRepository
import com.mvp.order.infrastruture.repository.order.OrderProductResponseRepository
import com.mvp.order.infrastruture.repository.order.OrderRepository
import com.mvp.order.utils.constants.ErrorMsgConstants
import io.mockk.*
import io.mockk.impl.log.Logger
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class OrderUnitTest {

    private val logger: Logger = mockk(relaxed = true)

    private val orderRepository = mockk<OrderRepository>()
    private val userService = mockk<UserServiceImpl>()
    private val orderProductRepository = mockk<OrderProductRepository>()
    private val orderProductResponseRepository = mockk<OrderProductResponseRepository>()
    private val productService = mockk<ProductServiceImpl>()
    private val mapper = mockk<ObjectMapper>()

    private lateinit var orderService: OrderService
    private lateinit var snsService: SnsService

    private lateinit var orderProducts: MutableList<OrderProductResponseDTO>
    private lateinit var orderProductsEntity: MutableList<OrderProductResponseEntity>
    private lateinit var orderByIdResponseDTO: OrderByIdResponseDTO
    private lateinit var orderEntity: OrderEntity
    private lateinit var orderRequestDTO: OrderRequestDTO
    private lateinit var userDTO: UserDTO
    private lateinit var productDTOList: List<ProductDTO>
    private lateinit var orderResponseDTO: OrderResponseDTO
    private lateinit var product: ProductDTO
    private lateinit var orderProductEntity: OrderProductEntity
    private lateinit var orderProductResponseEntity: OrderProductResponseEntity

    private var orderId = 1L
    private val zonedDateTime = ZonedDateTime.now(ZoneId.of( "America/Sao_Paulo")).toLocalDateTime()

    @BeforeEach
    fun setup() {
        snsService = mockk(relaxed = true)
        orderService = OrderServiceImpl(snsService, orderRepository, orderProductRepository, orderProductResponseRepository, productService, userService)

        orderProductResponseEntity = OrderProductResponseEntity(
            id = 1,
            idProduct = 1,
            idOrder = 1,
            productName = "Suco",
            categoryName = "Bebidas",
            price = BigDecimal.ZERO
        )

        orderProductsEntity = mutableListOf(OrderProductResponseEntity(
            id = 1,
            idProduct = 1,
            idOrder = 1,
            productName = "Suco",
            categoryName = "Bebidas",
            price = BigDecimal.ZERO
        ))
        orderProducts = mutableListOf(OrderProductResponseDTO(
            id = 1,
            idProduct = 1,
            idOrder = 1,
            productName = "Suco",
            categoryName = "Bebidas",
            price = BigDecimal.ZERO
        ))
        orderByIdResponseDTO = OrderByIdResponseDTO(
            id = 1,
            externalId = UUID.fromString("4879d212-bdf1-413c-9fd1-5b65b50257bc"),
            idClient = 1,
            totalPrice = BigDecimal.TEN,
            status = "PENDING",
            waitingTime = zonedDateTime,
            isFinished = false,
            products = orderProducts
        )
        orderEntity = OrderEntity(
            id = 1,
            externalId = UUID.fromString("4879d212-bdf1-413c-9fd1-5b65b50257bc"),
            idClient = 1,
            totalPrice = BigDecimal.TEN,
            status = "PENDING",
            waitingTime = zonedDateTime,
            isFinished = false
        )
        val orderProductDTO = OrderProductDTO(
            id = 1,
            idProduct = 1,
            idOrder = 1
        )

        orderRequestDTO = OrderRequestDTO(listOf(orderProductDTO), "99999999999")
        userDTO = UserDTO(
            id = 1,
            name = "Test User",
            email = "test@example.com",
            password = "password123",
            cpf = "99999999999"
        )
        product = ProductDTO(
            id = 1L,
            name = "Suco",
            price = BigDecimal(19.99),
            quantity = 5,
            idCategory = 2L,
            category = CategoryDTO(id = 1, name = "Bebidas", description = "Água, Refrigerante, Cerveja entre outros.")
        )
        productDTOList = listOf(product)
        orderProductEntity = OrderProductEntity(id = 1L, idProduct = 1L, idOrder = 1L)
        orderResponseDTO = OrderResponseDTO(orderEntity.toDTO())
    }

    @Test
    fun `test createOrder with valid data`() {
        // Prepare input
        val orderRequestDTO = orderRequestDTO

        // Mock response
        every { userService.getByUsername(any()) } returns userDTO
        every { productService.getAllById(any()) } returns productDTOList
        every { orderRepository.findByUsername(any()) } returns orderEntity
        every { orderRepository.save(any()) } answers { firstArg() }

        val listOrderProductEntity = listOf(OrderProductEntity(id = 1L, idProduct = 1L, idOrder = 1L))

        every { orderRepository.findByUsernameIfExists(orderRequestDTO.username) } returns Optional.of(orderEntity)

        // Mock orderProductRepository response
        every { orderProductRepository.saveAll(listOrderProductEntity) } answers { firstArg() }
        every { snsService.sendMessage(mapper.writeValueAsString(OrderResponseDTO(orderEntity.toDTO()))) } just Runs

        // Perform the test
        val result = orderService.createOrder(orderRequestDTO)

        // Assertions and verification
        assertNotNull(result)
        verify(exactly = 1) { userService.getByUsername(orderRequestDTO.username) }
        verify(exactly = 1) { productService.getAllById(any()) }
        verify(exactly = 1) { orderRepository.findByUsernameIfExists(any()) }
        verify(exactly = 1) { orderRepository.save(any()) }
    }


    @Test
    fun getOrderById() {
        val orderId = 1L

        every { orderProductResponseRepository.findAllByIdOrderInfo(orderId) } returns listOf(orderProductResponseEntity)
        every { orderRepository.findByIdOrder(orderId) } returns Optional.of(orderEntity)

        // Act
        val result = orderService.getOrderById(orderId)

        // Assert
        assertNotNull(result)
        assertEquals(orderByIdResponseDTO, result)
    }

    @Test
    fun getOrderByExternalId() {
        val expectedOrder = orderByIdResponseDTO

        every { orderRepository.findByExternalId(orderEntity.externalId.toString()) } returns orderEntity

        val result = orderService.getOrderByExternalId(orderEntity.externalId!!)
        result?.products?.addAll(orderProducts)

        // Assert
        assertEquals(expectedOrder, result)
    }

    @Test
    fun updateOrderProduct() {
        val expectedResponse = orderResponseDTO

        val mockOrderEntity = orderEntity
        val mockOptional = mockk<Optional<OrderEntity>>()

        every { userService.getByUsername(any()) } returns userDTO
        every { productService.getAllById(any()) } returns productDTOList
        every { orderRepository.findByUsername(any()) } returns orderEntity
        every { orderRepository.save(any()) } answers { firstArg() }

        every { mockOptional.isPresent } returns false
        every { mockOptional.orElse(any()) } answers { firstArg() }
        every { orderRepository.findByUsernameIfExists(any()) } returns mockOptional

        val orderProductEntitiesSlot = slot<List<OrderProductEntity>>()
        every { orderProductRepository.saveAll(capture(orderProductEntitiesSlot)) } answers { orderProductEntitiesSlot.captured }

        every { mockOptional.isPresent } returns true
        every { mockOptional.get() } returns mockOrderEntity
        every { orderRepository.findByUsernameIfExists(any()) } returns mockOptional

        // Act
        val result = orderService.updateOrderProduct(orderRequestDTO)

        // Assert
        assertNotNull(result)
        assertNotEquals(expectedResponse, result)
    }

    @Test
    fun deleteOrderById() {
        val orderId = 1L

        // Mocking behavior
        every { orderRepository.findByIdOrder(orderId) } returns Optional.of(orderEntity)
        coEvery { orderProductRepository.deleteByIdOrder(orderId) } returns 1
        every { orderRepository.deleteById(orderId) } just runs

        // Act
        orderService.deleteOrderById(orderId)

        // Verify interactions with mocks
        verify { orderProductRepository.deleteByIdOrder(orderId) }
        verify { orderRepository.deleteById(orderId) }
    }

    @Test
    fun deleteOrderProductById() {
        every { orderProductRepository.deleteAllById(any()) } just runs

        orderService.deleteOrderProductById(ProductRemoveOrderDTO(orderRequestDTO.username, mutableListOf(1L)))

        verify { orderProductRepository.deleteAllById(listOf(1L)) }
    }

    @Test
    fun getAllOrderProductsByIdOrder() {
        val id = 1L
        every { orderProductResponseRepository.findAllByIdOrderInfo(id) } returns orderProductsEntity.toList()
        every { orderRepository.findByIdOrder(orderId) } returns Optional.of(orderEntity)

        val expectedResponse = orderProducts

        val result = orderService.getAllOrderProductsByIdOrder(id)
        assertEquals(expectedResponse, result)
    }
}