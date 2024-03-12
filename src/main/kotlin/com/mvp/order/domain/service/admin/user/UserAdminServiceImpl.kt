package com.mvp.order.domain.service.admin.user

import com.mvp.order.domain.model.auth.RemoveUserDTO
import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.model.user.admin.UserAdminResponseDTO
import com.mvp.order.infrastruture.entity.user.UserEntity
import com.mvp.order.infrastruture.repository.order.OrderProductRepository
import com.mvp.order.infrastruture.repository.order.OrderRepository
import com.mvp.order.infrastruture.repository.user.AddressRepository
import com.mvp.order.infrastruture.repository.user.UserRepository
import com.mvp.order.utils.constants.ErrorMsgConstants
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserAdminServiceImpl(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository,
): UserAdminService {

    override fun getUsers(): List<UserAdminResponseDTO> {
        val users: MutableList<UserEntity> = userRepository.findAll()
        return users.map { user ->
            val address = user.idAddress?.let { addressRepository.findById(it).get() }
            user.toAdminDTO(address)
        }
    }

    @Transactional
    override fun deleteByUsername(removeUserDTO: RemoveUserDTO) {
        val userEntity = userRepository.findByUsernameWithAddress(removeUserDTO.username)
        if(userEntity.isPresent) {
            val user = userEntity.get()
            if(removeUserDTO.name != user.name) {
                throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_USER_NOT_FOUND)
            }
            val address = user.idAddress?.let { addressRepository.findById(it).get() }
            if(address != null && removeUserDTO.addressEntity.street != address.street) {
                throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_USER_NOT_FOUND)
            }
            val order = orderRepository.findByUsername(removeUserDTO.username)
            order.id?.let { orderProductRepository.deleteByIdOrder(it) }
            order.id?.let { orderRepository.deleteById(it) }
            userRepository.deleteById(user.id!!.toLong())
            user.idAddress?.let { addressRepository.deleteById(it) }
        } else {
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_USER_NOT_FOUND)
        }
    }

    // Used in the development process should not be used in production
    override fun deleteAllUsers() {
         return userRepository
             .deleteAll()
    }
}