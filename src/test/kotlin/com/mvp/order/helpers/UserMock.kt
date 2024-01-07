package com.mvp.order.helpers

import com.mvp.order.domain.model.user.UserDTO
import com.mvp.order.infrastruture.entity.user.UserEntity

object UserMock {
    private const val TEST_USERNAME = "12345678912"
    private const val TEST_NAME = "Lucas"
    private const val TEST_EMAIL = "testemail@email.com"
    private const val TEST_USER_ID = 9999
    private const val TEST_ADDRESS_ID = 99L

    private fun mockUserDTO() = UserDTO(
        id = TEST_USER_ID,
        name = TEST_NAME,
        cpf = TEST_USERNAME,
        email = TEST_EMAIL,
        idAddress = TEST_ADDRESS_ID
    )

    fun mockUser() = mockUserDTO()

    fun mockUserEntity() = UserEntity(
            id = TEST_USER_ID,
            name = TEST_NAME,
            cpf = TEST_USERNAME,
            email = TEST_EMAIL,
            idAddress = TEST_ADDRESS_ID
        )


    fun mockUpdateUserRequest() = UserDTO(
        email = "test@email.com",
        cpf = "12345678912",
        password = "1234",
    )
}