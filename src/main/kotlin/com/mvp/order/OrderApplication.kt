package com.mvp.order

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.mvp.order.infrastruture.repository"])
class OrderApplication

fun main(args: Array<String>) {
    runApplication<OrderApplication>(*args)
}