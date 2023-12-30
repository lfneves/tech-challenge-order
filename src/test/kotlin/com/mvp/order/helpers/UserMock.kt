//package com.mvp.order.helpers
//
//import com.mvp.order.domain.model.user.UserDTO
//import com.mvp.order.infrastruture.entity.user.UserEntity
//
//object UserMock {
//    private const val TEST_USERNAME = "12345678912"
//    private const val TEST_NAME = "Lucas"
//    private const val TEST_EMAIL = "testemail@email.com"
//    private const val TEST_PASSWORD = "123"
//    private const val TEST_USER_ID = 9999
//    private const val TEST_ADDRESS_ID = 99L
//    private const val TEST_ADDRESS_STREET = "São Paulo"
//
//    fun mockUserRegistrationRequest() = UserDTO(
//        name = TEST_NAME,
//        cpf = TEST_USERNAME,
//        email = TEST_EMAIL,
//        password = passwordEncoder.encode(TEST_PASSWORD)
//    )
//
//    fun mockUserAuthenticationRequest() = UserDTO(
//        email = TEST_EMAIL,
//        password = TEST_PASSWORD,
//    )
//
//    private fun mockUser(passwordService: Sha512PasswordEncoder) = UserDTO(
//        id = TEST_USER_ID,
//        name = TEST_NAME,
//        cpf = TEST_USERNAME,
//        email = TEST_EMAIL,
//        password = passwordService.encode(TEST_PASSWORD),
//        idAddress = TEST_ADDRESS_ID
//    )
//
//    private fun mockUserEntity(passwordService: Sha512PasswordEncoder) = UserEntity(
//        id = TEST_USER_ID,
//        name = TEST_NAME,
//        cpf = TEST_USERNAME,
//        email = TEST_EMAIL,
//        password = passwordService.encode(TEST_PASSWORD),
//        idAddress = TEST_ADDRESS_ID
//    )
//
//    fun mockUser() = mockUser(passwordEncoder)
//    fun mockUserEntity() = mockUserEntity(passwordEncoder)
//
//    fun mockUpdateUserRequest() = UserDTO(
//        email = "test@email.com",
//        cpf = "12345678912",
//        password = "1234",
//    )
//}