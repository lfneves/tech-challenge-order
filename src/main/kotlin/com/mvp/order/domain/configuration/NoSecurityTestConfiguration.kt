package com.mvp.order.domain.configuration

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@EnableAutoConfiguration(exclude = [SecurityAutoConfiguration::class])
@Profile("test")
class NoSecurityTestConfiguration {
}