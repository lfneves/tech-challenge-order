package com.mvp.order.infrastruture.repository.user


import com.mvp.order.infrastruture.entity.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {

    @Query(""" SELECT tb_client.id, password, name, email, cpf, tb_address.id AS id_address 
        FROM tb_client 
        INNER JOIN tb_address ON tb_client.id_address = tb_address.id 
        WHERE tb_client.cpf = :username """, nativeQuery = true)
    fun findByUsernameWithAddress(username: String?): Optional<UserEntity>

    fun findByEmail(email: String?): Optional<UserEntity>

    fun findByCpf(email: String?): Optional<UserEntity>
}