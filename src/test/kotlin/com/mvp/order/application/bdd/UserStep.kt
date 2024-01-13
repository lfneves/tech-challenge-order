package com.mvp.order.application.bdd

import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.model.user.UserDTO
import com.mvp.order.domain.service.user.UserService
import com.mvp.order.infrastruture.repository.user.UserRepository
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Profile

@Profile("test")
@DataJpaTest
class UserStep {

    @Autowired
    private lateinit var  userService: UserService
    @Autowired
    private lateinit var  userRepository: UserRepository
    private lateinit var  result: UserDTO
    private lateinit var exception: Exception

    @Given("a user with username {string}")
    fun givenUserWithUsername(username: String) {
        result = userService.getByUsername(username)!!
    }

    @Given("no user with username {string}")
    fun givenNoUserWithUsername(username: String) {
        val existsUsername = userRepository.findByUsernameWithAddress(username).isPresent
        Assertions.assertFalse(existsUsername)
    }

    @When("I request to get the user by username {string}")
    fun whenRequestUserByUsername(username: String) {
        try {
            result = userService.getByUsername(username)!!
        } catch (e: Exception) {
            exception = e
        }
    }

    @Then("the user should be returned")
    fun thenUserShouldBeReturned() {
        Assertions.assertNotNull(result)
    }

    @Then("a NotFoundException should be thrown")
    fun thenNotFoundExceptionShouldBeThrown() {
        Assertions.assertTrue(exception is Exceptions.NotFoundException)
    }
}