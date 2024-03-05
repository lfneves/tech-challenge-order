package com.mvp.order.domain.service.admin.user

import com.mvp.order.domain.model.user.UserDTO
import com.mvp.order.domain.service.order.OrderService
import com.mvp.order.infrastruture.entity.user.UserEntity
import com.mvp.order.infrastruture.repository.order.OrderProductRepository
import com.mvp.order.infrastruture.repository.order.OrderRepository
import com.mvp.order.infrastruture.repository.user.AddressRepository
import com.mvp.order.infrastruture.repository.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserAdminServiceImpl(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository,
): UserAdminService {

    override fun getUsers(): List<UserDTO> {
        val users: MutableList<UserEntity> = userRepository.findAll()
        return users.map { user ->
            val address = user.idAddress?.let { addressRepository.findById(it).get() }
            user.toDTO(address)
        }
    }

    @Transactional
    override fun deleteByUsername(username: String) {
        val userEntity = userRepository.findByUsernameWithAddress(username)
        if(userEntity.isPresent) {
            val user = userEntity.get()
            val order = orderRepository.findByUsername(username)
            order.id?.let { orderProductRepository.deleteByIdOrder(it) }
            order.id?.let { orderRepository.deleteById(it) }
            userRepository.deleteById(user.id!!.toLong())
            user.idAddress?.let { addressRepository.deleteById(it) }
        }
    }

    // Used in the development process should not be used in production
    override fun deleteAllUsers() {
         return userRepository
             .deleteAll()
    }
}