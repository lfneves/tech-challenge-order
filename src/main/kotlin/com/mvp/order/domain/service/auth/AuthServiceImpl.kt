package com.mvp.order.domain.service.auth

import com.mvp.order.domain.model.auth.ResponseSignupDTO
import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.model.user.UserDTO
import com.mvp.order.infrastruture.entity.user.UserEntity
import com.mvp.order.infrastruture.repository.user.AddressRepository
import com.mvp.order.infrastruture.repository.user.UserRepository
import com.mvp.order.utils.constants.ErrorMsgConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthServiceImpl @Autowired constructor(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository
): AuthService {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    override fun signup(user: UserDTO): ResponseSignupDTO {
        val cpf: String = user.cpf.toString()
        val email: String = user.email.toString()
        val existingUser: Optional<UserEntity> = userRepository.findByCpf(cpf)
        val existingEmail: Optional<UserEntity> = userRepository.findByEmail(email)
        return if (existingUser.isPresent) {
            ResponseSignupDTO(success = false, message = ErrorMsgConstants.ERROR_USER_ALREADY_EXIST)
        } else if (existingEmail.isPresent) {
            ResponseSignupDTO(success = false, message = ErrorMsgConstants.ERROR_USER_ALREADY_EXIST)
        } else {
            user.password = passwordEncoder.encode(user.password)
            saveUserWithAddress(user)
            ResponseSignupDTO(success = true, message = "Usuário criado com sucesso.")
        }
    }

    private fun saveUserWithAddress(user: UserDTO): UserDTO {
        val savedAddress = addressRepository.save(user.address!!)
        user.address = savedAddress
        user.idAddress = savedAddress.id

        val userEntity = userRepository.save(user.toEntity())
        val address = addressRepository.findById(userEntity.idAddress!!)
        return userEntity.toDTO(address.get())
    }

}