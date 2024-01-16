package com.mvp.order.application.message

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.PublishRequest
import software.amazon.awssdk.services.sns.model.PublishResponse
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import software.amazon.awssdk.services.sqs.model.SendMessageRequest

class SendMessageSqsTest {

    val ORDER_TOPIC: String = System.getenv().getOrDefault("ORDER_TOPIC", "")

    @Test
    fun `Send Message to SNS Topic`() {
        val snsClient = mockk<SnsClient>()
        val topicArn = ORDER_TOPIC
        val message = "Send Message to SNS Topic Test"

        val publishRequest = PublishRequest.builder()
            .topicArn(topicArn)
            .message(message)
            .build()

        val fakeResponse = PublishResponse.builder()
            .messageId("fakeMessageId")
            .build()

        every { snsClient.publish(publishRequest) } returns fakeResponse

        try {
            val publishResponse = snsClient.publish(publishRequest)
            println("Message sent. Message ID: ${publishResponse.messageId()}")
            verify { snsClient.publish(publishRequest) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}