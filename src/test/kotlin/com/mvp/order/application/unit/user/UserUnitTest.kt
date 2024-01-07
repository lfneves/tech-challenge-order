package com.mvp.order.application.unit.user

import com.mvp.order.domain.model.user.UserDTO
import com.mvp.order.domain.service.user.UserServiceImpl
import com.mvp.order.infrastruture.repository.user.AddressRepository
import com.mvp.order.infrastruture.repository.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

class UserUnitTest {

    private val userRepository = mockk<UserRepository>()
    private lateinit var userService: UserServiceImpl


    @BeforeEach
    fun setup() {
        userService = UserServiceImpl(userRepository)
    }

    @Test
    fun `find user by username`() {
        val username = "99999999999"
        // Mocked user data
        val userDTO = UserDTO(
            id = 1,
            name = "Test User",
            email = "test@example.com",
            password = "password123",
            cpf = username
        )

        val userEntity = userDTO.toEntity()

        every { userRepository.findByUsernameWithAddress(username) } returns Optional.of(userEntity)

        // Act
        val result = userService.getByUsername(username)

        // Assert
        assertEquals(userEntity.toDTO(), result)
    }
}
