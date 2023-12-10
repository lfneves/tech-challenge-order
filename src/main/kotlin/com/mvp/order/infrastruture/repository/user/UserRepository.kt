package com.mvp.order.infrastruture.repository.user


import com.mvp.order.infrastruture.entity.user.UserEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserRepository : ReactiveCrudRepository<UserEntity?, Long?> {
    @Query("SELECT id, password, name, email, cpf FROM tb_client WHERE name = $1")
    fun findByName(name: String?): Flux<UserEntity>

    @Query(""" SELECT tb_client.id, password, name, email, cpf, tb_address.id AS id_address 
        FROM tb_client 
        INNER JOIN tb_address ON tb_client.id_address = tb_address.id 
        WHERE tb_client.cpf = $1 """, )
    fun findByUsernameWithAddress(username: String?): Mono<UserEntity>

    @Query("SELECT tb_client.id, tb_client.name, tb_client.email, tb_client.cpf, tb_client.password, tb_client.id_address " +
            "FROM tb_client " +
            "WHERE tb_client.id = $1")
    fun findById(id: Int): Mono<UserEntity>

    fun deleteById(id: Int): Mono<Void>

    @Query("SELECT * FROM tb_client u JOIN address a ON u.address_id = a.id WHERE u.id = $1")
    fun findByIdWithAddress(id: Int): Mono<UserEntity>
}