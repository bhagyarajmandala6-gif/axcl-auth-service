package com.innocito.axcl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class SqsSender {
    private final SqsAsyncClient sqsAsyncClient;

    public void sendMessage(String queueUrl, String messageBody) {
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();

        sqsAsyncClient.sendMessage(request).thenAccept(response ->
                log.info("Message sent, ID: {}", response.messageId()))
                .exceptionally(throwable -> {
            log.error("Failed to send message to SQS", throwable);
            return null;
        });
    }
}
