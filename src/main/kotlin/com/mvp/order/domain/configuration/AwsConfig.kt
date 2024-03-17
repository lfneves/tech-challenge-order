package com.mvp.order.domain.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sqs.SqsClient

@Configuration
@ConfigurationProperties(prefix = "order.sns")
class AwsConfig {

    lateinit var topicArn: String
    lateinit var statusQueue: String
    lateinit var region: String

    @Bean
    fun snsClient(): SnsClient = SnsClient.builder().build()

    @Bean
    fun sqsClient(): SqsClient {
        return SqsClient.builder()
            .region(Region.of(region))
            .build()
    }
}