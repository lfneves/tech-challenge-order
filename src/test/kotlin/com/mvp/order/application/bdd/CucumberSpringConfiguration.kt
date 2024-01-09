package com.mvp.order.application.bdd

import com.mvp.order.OrderApplication
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest

@CucumberContextConfiguration
@SpringBootTest(classes = [OrderApplication::class])
class CucumberSpringConfiguration
