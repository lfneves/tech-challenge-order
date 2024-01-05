package com.mvp.order.domain.service.user

import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.model.user.UserDTO
import com.mvp.order.infrastruture.entity.user.UserEntity
import com.mvp.order.infrastruture.repository.user.AddressRepository
import com.mvp.order.infrastruture.repository.user.UserRepository
import com.mvp.order.utils.constants.ErrorMsgConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl: UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var addressRepository: AddressRepository

    override fun getByUsername(username: String): UserDTO? {
        val userOptional = userRepository.findByUsernameWithAddress(username)
        return if(userOptional.isPresent) {
            val user = userOptional.get()
            user.toDTO()
        } else {
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_USER_NOT_FOUND)
        }
    }
}