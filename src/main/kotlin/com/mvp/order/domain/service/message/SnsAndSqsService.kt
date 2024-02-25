package com.mvp.order.domain.service.message

import com.mvp.order.domain.configuration.AwsConfig
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.PublishRequest
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest

@Service
class SnsAndSqsService(
    private val snsClient: SnsClient,
    private val sqsClient: SqsClient,
    private val awsConfig: AwsConfig
) {

    fun sendMessage(message: String) {
        val publishRequest = PublishRequest.builder()
            .topicArn(awsConfig.topicArn)
            .message(message)
            .build()

        val publishResponse = snsClient.publish(publishRequest)
        println("Message sent. Message ID: ${publishResponse.messageId()}")
    }

    fun sendQueueStatusMessage(message: String) {
        val sendMessageRequest = SendMessageRequest.builder()
            .queueUrl(awsConfig.statusQueue)
            .messageBody(message)
            .delaySeconds(3)
            .build()

        sqsClient.sendMessage(sendMessageRequest)
    }
}