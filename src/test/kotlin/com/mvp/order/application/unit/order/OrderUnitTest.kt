package com.mvp.order.application.unit.order

import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.model.order.*
import com.mvp.order.domain.model.product.CategoryDTO
import com.mvp.order.domain.model.product.ProductDTO
import com.mvp.order.domain.model.product.ProductRemoveOrderDTO
import com.mvp.order.domain.model.user.UserDTO
import com.mvp.order.domain.service.client.message.SnsService
import com.mvp.order.domain.service.client.order.OrderServiceImpl
import com.mvp.order.domain.service.client.product.ProductServiceImpl
import com.mvp.order.domain.service.client.user.UserServiceImpl
import com.mvp.order.infrastruture.entity.order.OrderEntity
import com.mvp.order.infrastruture.entity.order.OrderProductEntity
import com.mvp.order.infrastruture.entity.order.OrderProductResponseEntity
import com.mvp.order.infrastruture.repository.order.OrderProductRepository
import com.mvp.order.infrastruture.repository.order.OrderRepository
import com.mvp.order.utils.constants.ErrorMsgConstants
import io.mockk.*
import io.mockk.impl.log.Logger
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class OrderUnitTest {

    private val logger: Logger = mockk(relaxed = true)

    private val orderRepository = mockk<OrderRepository>()
    private val userService = mockk<UserServiceImpl>()
    private val orderProductRepository = mockk<OrderProductRepository>()
    private val productService = mockk<ProductServiceImpl>()
    private val snsService = mockk<SnsService>()

    private val orderServiceImpl = OrderServiceImpl(orderRepository, userService, orderProductRepository, productService, snsService)

    private lateinit var orderProducts: MutableList<OrderProductResponseEntity>
    private lateinit var orderByIdResponseDTO: OrderByIdResponseDTO
    private lateinit var orderEntity: OrderEntity
    private lateinit var orderRequestDTO: OrderRequestDTO
    private lateinit var userDTO: UserDTO
    private lateinit var productDTOList: List<ProductDTO>
    private lateinit var orderResponseDTO: OrderResponseDTO
    private lateinit var product: ProductDTO
    private lateinit var orderProductEntity: OrderProductEntity

    private var orderId = 1L
    private val zonedDateTime = ZonedDateTime.now(ZoneId.of( "America/Sao_Paulo")).toLocalDateTime()

    @BeforeEach
    fun setup() {
        orderProducts = mutableListOf(OrderProductResponseEntity(
            id = 1,
            idProduct = 1,
            idOrder = 1,
            productName = "Suco",
            categoryName = "Bebidas",
            price = BigDecimal.ZERO
        ))
        orderByIdResponseDTO = OrderByIdResponseDTO(
            id = 1,
            externalId = UUID.randomUUID(),
            idClient = 1,
            totalPrice = BigDecimal.TEN,
            status = "PENDING",
            waitingTime = zonedDateTime,
            isFinished = false,
            products = orderProducts
        )
        orderEntity = OrderEntity(
            id = 1,
            externalId = UUID.randomUUID(),
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
    fun getOrderById() {

        every { orderRepository.findByIdOrder(orderId) } returns Mono.just(orderEntity)
        every { orderProductRepository.findAllByIdOrderInfo(orderId) } returns Flux.fromIterable(orderProducts)

        val result = orderServiceImpl.getOrderById(orderId)

        StepVerifier.create(result)
            .expectNextMatches { response ->
                response.id == orderByIdResponseDTO.id &&
                        response.idClient == orderByIdResponseDTO.idClient &&
                        response.totalPrice == orderByIdResponseDTO.totalPrice &&
                        response.status == orderByIdResponseDTO.status &&
                        response.isFinished == orderByIdResponseDTO.isFinished &&
                        // Dynamic values check only the structure or format, not the exact value
                        response.externalId.toString().matches(Regex("[a-f0-9-]{36}")) &&
                        response.waitingTime.toString().matches(Regex("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+"))
                // lists check the size or contents as needed
                response.products.size == orderByIdResponseDTO.products.size
            }
            .verifyComplete()
    }

    @Test
    fun getOrderByExternalId() {
        val externalId = UUID.randomUUID()
        val expectedOrder = orderEntity
        val expectedDto = expectedOrder.toResponseDTO()

        coEvery { orderRepository.findByExternalId(externalId) } returns Mono.just(expectedOrder)

        runBlocking {
            val result = orderServiceImpl.getOrderByExternalId(externalId)
            assertEquals(expectedDto, result)
        }
    }

    @Test
    fun createOrder() {
        val orderServiceImplMockk = mockk<OrderServiceImpl>()
        every { userService.getByUsername(orderRequestDTO.username) } returns Mono.just(userDTO)
        every { productService.getAllById(listOf(1L)) } returns Flux.just(product)
        every { orderRepository.findByUsername(orderRequestDTO.username) } returns Mono.just(orderEntity)
        every { orderProductRepository.save(any()) } returns Mono.just(orderProductEntity)
        every { orderRepository.save(orderEntity) } returns Mono.just(orderEntity)
        every { orderServiceImplMockk.saveAllOrderProduct(listOf(orderProductEntity)) } returns Flux.just(orderProductEntity)

        val result = orderServiceImpl.createOrder(orderRequestDTO)

        result.subscribe { response ->
            assertEquals(orderResponseDTO, response)
        }
    }

    @Test
    fun updateOrderProduct() {
        every { userService.getByUsername(orderRequestDTO.username) } returns Mono.just(userDTO)
        every { orderRepository.findByUsername(orderRequestDTO.username) } returns Mono.just(orderEntity)
        every { orderServiceImpl.createOrder(orderRequestDTO) } returns Mono.just(orderResponseDTO)

        StepVerifier.create(orderServiceImpl.updateOrderProduct(orderRequestDTO))
            .expectNextMatches{ response ->
                response.orderDTO?.id == orderResponseDTO.orderDTO?.id
                        && response.orderDTO?.status == orderResponseDTO.orderDTO?.status
                        && response.orderDTO?.externalId == orderResponseDTO.orderDTO?.externalId
                        && response.orderDTO?.isFinished == orderResponseDTO.orderDTO?.isFinished
            }.expectComplete()

        verify(exactly = 1) { orderRepository.findByUsername(orderRequestDTO.username) }
    }

    @Test
    fun deleteOrderById() {
        val orderId = 1L
        val monoVoid: Mono<Void> = Mono.empty()

        // Mocking behavior
        every { orderRepository.findByIdOrder(orderId) } returns Mono.just(orderEntity)
        every { orderProductRepository.deleteByIdOrder(orderId) } returns monoVoid
        every { orderRepository.deleteById(orderId) } returns monoVoid

        // Call the method
        val result = orderServiceImpl.deleteOrderById(orderId)

        // Verify the result
        StepVerifier.create(result)
            .verifyComplete()

        // Verify interactions with mocks
        verify { orderRepository.findByIdOrder(orderId) }
        verify { orderProductRepository.deleteByIdOrder(orderId) }
        verify { orderRepository.deleteById(orderId) }
    }

    @Test
    fun deleteOrderProductById() {
        val productRemoveOrderDTO = ProductRemoveOrderDTO(orderRequestDTO.username, mutableListOf(1L))
        val listProductId = productRemoveOrderDTO.orderProductId.map { it }.toList()

        every { orderProductRepository.deleteById(listProductId) } returns Mono.error(
            Exceptions.NotFoundException(
            ErrorMsgConstants.ERROR_ORDER_NOT_FOUND))

        val result = orderServiceImpl.deleteOrderProductById(productRemoveOrderDTO)

        StepVerifier.create(result)
            .expectErrorMatches { it is Exceptions.NotFoundException && it.message == ErrorMsgConstants.ERROR_ORDER_NOT_FOUND }
            .verify()

        // Verify interactions with the mock
        verify(exactly = 1) { orderProductRepository.deleteById(listProductId) }
    }

    @Test
    fun getAllOrderProductsByIdOrder() {
        val id = 123L
        every { orderProductRepository.findAllByIdOrderInfo(id) } returns Flux.fromIterable(orderProducts)

        val result = orderServiceImpl.getAllOrderProductsByIdOrder(id)

        StepVerifier.create(result)
            .expectNextMatches { it.id == orderProducts.get(0).id }
            .verifyComplete()

        // Verify interactions with the mock
        verify(exactly = 1) { orderProductRepository.findAllByIdOrderInfo(id) }
    }

    @Test
    fun fakeCheckoutOrder() {
        val orderCheckoutDTO = OrderCheckoutDTO(idOrder = 123L) // Replace with actual data

        every { orderRepository.findByIdOrder(orderCheckoutDTO.idOrder) } returns Mono.just(orderEntity)
        every { orderRepository.save(any()) } answers { Mono.just(firstArg()) }

        val result = orderServiceImpl.fakeCheckoutOrder(orderCheckoutDTO)

        // Verify results
        StepVerifier.create(result)
            .expectNext(true)
            .verifyComplete()

        // Verify interactions with the mock
        verify(exactly = 1) { orderRepository.findByIdOrder(orderCheckoutDTO.idOrder) }
        verify(exactly = 1) { orderRepository.save(any()) }
    }

}