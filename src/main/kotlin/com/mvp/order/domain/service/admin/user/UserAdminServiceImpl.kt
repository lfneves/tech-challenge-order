package com.mvp.order.domain.service.admin.user

import com.mvp.order.domain.model.user.UserDTO
import com.mvp.order.infrastruture.entity.user.UserEntity
import com.mvp.order.infrastruture.repository.user.AddressRepository
import com.mvp.order.infrastruture.repository.user.UserRepository
import org.springframework.stereotype.Service

@Service
class UserAdminServiceImpl(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository
): UserAdminService {

    override fun getUsers(): List<UserDTO> {
        val users: MutableList<UserEntity> = userRepository.findAll()
        return users.map { user ->
            val address = user.idAddress?.let { addressRepository.findById(it).get() }
            user.toDTO(address)
        }
    }

    override fun deleteById(id: Long) {
        val userEntity = userRepository.findById(id)
        if(userEntity.isPresent) {
            val user = userEntity.get()
            user.idAddress?.let { addressRepository.deleteById(it) }
            userRepository.deleteById(id)
        }
    }

    // Used in the development process should not be used in production
    override fun deleteAllUsers() {
         return userRepository
             .deleteAll()
    }
}