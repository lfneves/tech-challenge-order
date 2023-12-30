package com.mvp.order.application.unit.user

import com.mvp.order.domain.model.user.UserDTO
import com.mvp.order.domain.service.user.UserServiceImpl
import com.mvp.order.infrastruture.repository.user.AddressRepository
import com.mvp.order.infrastruture.repository.user.UserRepository
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest
class UserRepositoryTest {
    private val userRepository: UserRepository = mockk()
    private val addressRepository: AddressRepository = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()

    private val userService = UserServiceImpl()

    @Test
    fun `test saveUser`() {
        // Mocked user data
        val userDTO = UserDTO(
            id = 1,
            name = "Test User",
            email = "test@example.com",
            password = "password123"
        )

        val userEntity = userDTO.toEntity()

//        every { userRepository.deleteAll() } returns Mono.empty()
//        every { userRepository.save(userEntity) } returns Mono.just(userDTO.toEntity())

//        val setup: Flux<UserEntity> = userRepository.deleteAll().thenMany(userRepository.save(userEntity))

//        StepVerifier
//            .create(setup)
//            .expectNextCount(1)
//            .verifyComplete()
    }

    @Test
    fun `find user by username`() {
        val USERNAME = "99999999999"
        // Mocked user data
        val userDTO = UserDTO(
            id = 1,
            name = "Test User",
            email = "test@example.com",
            password = "password123",
            cpf = USERNAME
        )

        val userEntity = userDTO.toEntity()

//        every { userRepository.deleteAll() } returns Mono.empty()
//        every { userRepository.save(userEntity) } returns Mono.just(userDTO.toEntity())
//        every { userRepository.findByUsernameWithAddress(USERNAME) } returns Mono.just(userDTO.toEntity())
//
//        val setup: Mono<UserEntity> = userRepository.save(userEntity)
//        val find: Mono<UserEntity> = userRepository.findByUsernameWithAddress(USERNAME)
//        val composite: Publisher<UserEntity> = Mono
//            .from(setup)
//            .then(find)
//        StepVerifier
//            .create(composite)
//            .consumeNextWith { account: UserEntity ->
//                assertNotNull(account.id)
//                assertEquals(account.cpf, USERNAME)
//                assertEquals(account.name, "Test User")
//            }
//            .verifyComplete()
    }
}
