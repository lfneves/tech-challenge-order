package com.mvp.order.application.bdd

import com.mvp.order.domain.model.order.*
import com.mvp.order.domain.model.product.ProductRemoveOrderDTO
import com.mvp.order.domain.service.order.OrderService
import com.mvp.order.helpers.OrderMock
import com.mvp.order.infrastruture.entity.order.OrderProductEntity
import com.mvp.order.infrastruture.repository.order.OrderRepository
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class OrderStep {

    @Autowired
    private lateinit var orderService: OrderService
    @Autowired
    private lateinit var orderRepository: OrderRepository

    private lateinit var result: OrderResponseDTO
    private var retrievedOrder: OrderByIdResponseDTO? = null
    private var retrievalError: Exception? = null
    private var retrievedProducts: List<OrderProductResponseDTO>? = null
    private var updatedOrder: OrderResponseDTO? = null
    private var updateError: Exception? = null
    private lateinit var orderProductsToSave: List<OrderProductEntity>
    private var savedOrderProducts: List<OrderProductEntity>? = null


    private val orderRequest = OrderMock.mockOrderRequest()

    // Create order
    @Given("a user named {string}")
    fun aUserNamed(username: String) {
        orderRequest.username = username
    }

    @And("products with IDs {string}")
    fun productsWithIds(ids: String) {
        val productIds = ids.split(", ").map { it.toLong() }
        val products = productIds.map { OrderProductDTO(idProduct = it) }
        orderRequest.orderProduct = products
    }

    @When("I create an order for {string} with products {string}")
    fun iCreateAnOrderForUserWithProducts(username: String, productIds: String) {
        orderRequest.username = username
        result = orderService.createOrder(orderRequest)
    }

    @Then("the order should be successfully created")
    fun theOrderShouldBeSuccessfullyCreated() {
        assertNotNull(result)
    }

    @And("the total price should be calculated correctly")
    fun theTotalPriceShouldBeCalculatedCorrectly() {
        assertNotNull(result.orderDTO?.totalPrice)
    }

    // get order by ID
    // Scenario 1
    @When("I retrieve the order id with username {string}")
    fun iRetrieveTheOrderWithId(username: String) {
        try {
            val order = orderRepository.findByUsernameIfExists(username)
            val idOrder = order.get().id!!
            retrievedOrder = orderService.getOrderById(idOrder)
        } catch (e: Exception) {
            retrievalError = e
        }
    }

    @Then("the order should be successfully returned")
    fun theOrderShouldBeSuccessfullyReturned() {
        assertNotNull(retrievedOrder)
    }

    // Scenario 2
    @When("I retrieve the order not exists with ID {long}")
    fun iRetrieveTheOrderNotExistsWithId(id: Long) {
        try {
            retrievedOrder = orderService.getOrderById(id)
        } catch (e: Exception) {
            retrievalError = e
        }
    }

    // Not found error
    @Then("a not found error should be returned")
    fun aNotFoundErrorShouldBeReturned() {
        assertNotNull(retrievalError)
        assertTrue(retrievalError is Exception)
    }

    //
    @Given("an order with ID {long} has products")
    fun anOrderWithIdHasProducts(id: Long) {
        result = orderService.createOrder(orderRequest)
    }

    @When("I retrieve products for order with ID {long}")
    fun iRetrieveProductsForOrderWithId(id: Long) {
        try {
            retrievedProducts = orderService.findAllByIdOrderInfo(result.orderDTO?.id!!)
        } catch (e: Exception) {
            retrievalError = e
        }
    }

    @Then("the products should be successfully returned")
    fun theProductsShouldBeSuccessfullyReturned() {
        assertNotNull(retrievedProducts)
        retrievedProducts?.let { assertTrue(it.isNotEmpty()) }
    }

    // Retrieve order by external ID
    @When("I retrieve the order with external ID")
    fun anOrderExistsWithExternalId() {
        retrievedOrder = orderService.getOrderByExternalId(result.orderDTO?.externalId!!)
    }

    @When("I retrieve not exist the order with external ID {string}")
    fun iRetrieveNotExistTheOrderWithExternalId(externalId: String) {
        retrievedOrder = orderService.getOrderByExternalId(UUID.fromString(externalId))
    }

    @Then("no order should be found")
    fun noOrderShouldBeFound() {
        assertNull(retrievedOrder)
    }

    // Update order product
    @When("I update the order for user {string}")
    fun iUpdateTheOrderForUser(username: String) {
        try {
            val orderProductDTO = OrderProductDTO(
                id = 1,
                idProduct = 4,
                idOrder = result.orderDTO?.id!!
            )
            val orderRequestDTO = OrderRequestDTO(listOf(orderProductDTO), "99999999999")
            updatedOrder = orderService.updateOrderProduct(orderRequestDTO)
        } catch (e: Exception) {
            updateError = e
        }
    }

    @Then("the order should be successfully updated")
    fun theOrderShouldBeSuccessfullyUpdated() {
        assertNotNull(updatedOrder)
    }

    // Save order products
    @Given("a list of order products")
    fun aListOfOrderProducts() {
        orderProductsToSave = listOf(
            OrderProductEntity(id = 1L, idProduct = 1L, idOrder = result.orderDTO?.id!!),
            OrderProductEntity(id = 1L, idProduct = 1L, idOrder = result.orderDTO?.id!!)
        )
    }

    @When("I save the order products")
    fun iSaveTheOrderProducts() {
        savedOrderProducts = orderService.saveAllOrderProduct(orderProductsToSave)
    }

    @Then("the order products should be successfully saved")
    fun theOrderProductsShouldBeSuccessfullySaved() {
        assertNotNull(savedOrderProducts)
        assertEquals(orderProductsToSave.size, savedOrderProducts?.size)
    }

    // Retrieve all order products by order ID
    @When("I retrieve all products for order")
    fun iRetrieveAllProductsForOrderWithId() {
        try {
            retrievedProducts = orderService.getAllOrderProductsByIdOrder(result.orderDTO?.id!!)
        } catch (e: Exception) {
            retrievalError = e
        }
    }

    @When("I retrieve all products not exists for order with ID {long}")
    fun iRetrieveNotExistAllProductsForOrderWithId(id: Long) {
        try {
            retrievedProducts = orderService.getAllOrderProductsByIdOrder(id)
        } catch (e: Exception) {
            retrievalError = e
        }
    }

//     Delete order products by IDs
    @When("I delete the order by username {string} products with IDs {string}")
    fun iDeleteTheOrderProductsWithIds(username: String, ids: String) {
        val productIds = ids.split(", ").map { it.toLong() }.toMutableList()
        val order = orderRepository.findByUsernameIfExists(username)
        val idOrder = order.get().id!!
        val productRemoveOrderDTO = ProductRemoveOrderDTO(idOrder, orderProductId = productIds)
        try {
            orderService.deleteOrderProductById(productRemoveOrderDTO)
        } catch (e: Exception) {
            retrievalError = e
        }
    }

    @Then("the order products should be successfully deleted by username {string} and with remaining one product")
    fun theOrderProductsShouldBeSuccessfullyDeleted(username: String) {
        val order = orderRepository.findByUsernameIfExists(username)
        val idOrder = order.get().id!!
        retrievedProducts = orderService.getAllOrderProductsByIdOrder(idOrder)
        assertEquals(1, retrievedProducts!!.size)
    }

    // Delete order by ID
    @When("Delete the order")
    fun deleteTheOrder() {
        try {
            orderService.deleteOrderById(result.orderDTO?.id!!)
        } catch (e: Exception) {
            retrievalError = e
        }
    }

    @When("Delete the order by username {string}")
    fun deleteTheOrderByUsername(username: String) {
        try {
            val order = orderRepository.findByUsernameIfExists(username)
            val idOrder = order.get().id!!
            orderService.deleteOrderById(idOrder)
        } catch (e: Exception) {
            retrievalError = e
        }
    }

    @Then("the order should be successfully deleted for username {string}")
    fun theOrderShouldBeSuccessfullyDeleted(username: String) {
        assertNull(retrievalError)
        val order = orderRepository.findByUsernameIfExists(username)
        assertFalse(order.isPresent)
    }
}