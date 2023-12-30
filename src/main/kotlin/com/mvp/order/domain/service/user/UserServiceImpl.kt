package com.mvp.order.domain.service.user

import com.mvp.order.domain.model.user.UserDTO
import com.mvp.order.infrastruture.repository.user.AddressRepository
import com.mvp.order.infrastruture.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl: UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var addressRepository: AddressRepository

    override fun getByUsername(username: String): UserDTO {
        val user = userRepository.findByUsernameWithAddress(username)
        val address = addressRepository.findById(user.idAddress!!)
        return user.toDTO(user, address.get())
    }

}