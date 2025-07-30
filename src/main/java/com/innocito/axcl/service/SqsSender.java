package com.innocito.axcl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.Map;

import static com.innocito.axcl.util.ApplicationConstants.CORRELATION_ID_HEADER;
import static com.innocito.axcl.util.ApplicationConstants.MDC_KEY;

@Slf4j
@RequiredArgsConstructor
@Service
public class SqsSender {
    private final SqsAsyncClient sqsAsyncClient;

    public void sendMessage(String queueUrl, String messageBody) {
        String correlationId = MDC.get(MDC_KEY);

        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        if (correlationId != null) {
            messageAttributes.put(CORRELATION_ID_HEADER, MessageAttributeValue.builder()
                    .dataType(String.class.getName())
                    .stringValue(correlationId)
                    .build());
        }

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .messageAttributes(messageAttributes)
                .build();

        sqsAsyncClient.sendMessage(request).thenAccept(response ->
                        log.info("Message sent, ID: {}", response.messageId()))
                .exceptionally(throwable -> {
                    log.error("Failed to send message to SQS", throwable);
                    return null;
                });
    }
}
