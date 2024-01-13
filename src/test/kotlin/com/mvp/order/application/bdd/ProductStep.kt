package com.mvp.order.application.bdd

import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.model.product.ProductDTO
import com.mvp.order.domain.service.product.ProductService
import com.mvp.order.infrastruture.repository.product.CategoryRepository
import com.mvp.order.infrastruture.repository.product.ProductRepository
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Profile
import org.springframework.dao.EmptyResultDataAccessException
import java.math.BigDecimal

@Profile("test")
@DataJpaTest
class ProductStep {

    @Autowired
    private lateinit var productService: ProductService
    @Autowired
    private lateinit var productRepository: ProductRepository
    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    private lateinit var actualResponse: ProductDTO
    private lateinit var actualException: Exception
    private lateinit var actualResponseList: List<ProductDTO>
    private lateinit var productIds: List<Long>
    private lateinit var totalPrice: BigDecimal

    @Given("a product with ID {long} exists")
    fun aProductWithIdExists(id: Long) {
        productRepository.existsById(id)
    }

    @Given("no product with ID {long} exists")
    fun aProductWithIdNotExists(id: Long) {
        productRepository.existsById(id)
    }

    @When("I request the product with ID {long}")
    fun iRequestTheProductWithId(id: Long) {
        try {
            actualResponse = productService.getProductById(id)
        } catch (e: Exception) {
            actualException = e
        }
    }

    @Then("the product details are returned")
    fun theProductDetailsAreReturned() {
        assertNotNull(actualResponse)
    }

    @Then("a not found error is returned")
    fun aNotFoundErrorIsReturned() {
        assertTrue(actualException is Exceptions.NotFoundException)
    }

    //Get all products
    @Given("products are present in the store")
    fun productsArePresentInTheStore() {
        // Mock your product repository to return a list of products
    }

    @Then("no products are present in the store")
    fun noProductsArePresentInTheStore() {
        actualResponseList = listOf()
    }

    @Given("I request all products")
    fun iRequestAllProducts() {
        actualResponseList = productService.getProducts()
    }

    @Then("the list of all products is returned")
    fun theListOfAllProductsIsReturned() {
        assertFalse(actualResponseList.isEmpty())
    }

    @Then("an empty list is returned")
    fun anEmptyListIsReturned() {
        assertTrue(actualResponseList.isEmpty())
    }

//    Get products by IDs
    @Given("the following product IDs exist: {string}")
    fun theFollowingProductIdsExist(ids: String) {
        productIds = ids.split(", ").map { it.toLong() }.toList()
        var existsById = false
        productIds.forEach {
            existsById = productRepository.existsById(it)
        }
        assertTrue(existsById)
    }

    @Given("no product IDs are provided")
    fun noProductIdsAreProvided() {
        productIds = listOf()
    }

    @Given("the following product IDs do not exist: {string}")
    fun theFollowingProductIdsDoNotExist(ids: String) {
        productIds = ids.split(", ").map { it.toLong() }.toList()
        var existsById = false
        productIds.forEach {
            existsById = productRepository.existsById(it)
        }
        assertFalse(existsById)
    }

    @When("I request products with these IDs")
    fun iRequestProductsWithTheseIds() {
        try {
            actualResponseList = productService.getAllById(productIds)
        } catch (e: Exception) {
            actualException = e
        }
    }

    @Then("the corresponding products are returned")
    fun theCorrespondingProductsAreReturned() {
        assertFalse(actualResponseList.isEmpty())
    }

    @Then("a product not found error is thrown")
    fun aProductNotFoundErrorIsThrown() {
        assertTrue(actualException is Exceptions.NotFoundException)
    }

    //Get total price by product IDs
    @Given("the following product IDs: {string}")
    fun theFollowingProductIds(ids: String) {
        productIds = ids.split(", ").map { it.toLong() }.toList()
    }

    @When("I request the total price for these products")
    fun iRequestTheTotalPriceForTheseProducts() {
        totalPrice = productService.getByIdTotalPrice(productIds)
    }

    @Then("the total price is returned")
    fun theTotalPriceIsReturned() {
        assertNotNull(totalPrice)
    }

    @Then("a default price of {string} is returned")
    fun aDefaultPriceIsReturned(expectedPrice: String) {
        assertEquals(BigDecimal(expectedPrice), totalPrice)
    }

    //Get products by category name
    @Given("the category with name {string} exists")
    fun theCategoryWithNameExists(categoryName: String) {
        var existsById = false
        val category = categoryRepository.findByName(categoryName)
        if(category.name == categoryName) {
            existsById = true
        }
        assertTrue(existsById)
    }

    @Given("the category with name {string} does not exist")
    fun theCategoryWithNameDoesNotExist(categoryName: String) {
        var existsById = false
        try {
            val category = categoryRepository.findByName(categoryName)
            if (category.name == categoryName) {
                existsById = true
            }
        } catch (e: EmptyResultDataAccessException) {
            existsById = false
        }
        assertFalse(existsById)
    }

    @When("I request products for the category {string}")
    fun iRequestProductsForTheCategory(categoryName: String) {
        actualResponseList = productService.getProductsByCategoryByName(categoryName)
    }

    @Then("a list of products in the {string} category is returned")
    fun aListOfProductsInTheCategoryIsReturned(categoryName: String) {
        assertFalse(actualResponseList.isEmpty())
    }
}