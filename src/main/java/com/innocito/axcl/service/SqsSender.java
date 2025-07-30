package com.innocito.axcl.service;

import com.innocito.axcl.util.MdcAware;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
                    .dataType("String")
                    .stringValue(correlationId)
                    .build());
        }

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .messageGroupId("sentry_webhook") // REQUIRED for FIFO
                .messageDeduplicationId(UUID.randomUUID().toString())
                .messageAttributes(messageAttributes)
                .build();

        sqsAsyncClient.sendMessage(request)
                .thenAccept(MdcAware.wrap(response ->
                        log.info("Webhook request sent to SQS successfully. Message sent, ID: {}"
                                , response.messageId())))
                .exceptionally(MdcAware.wrapFunction(throwable -> {
                    log.error("Failed to send message to SQS", throwable);
                    return null;
                }));
    }
}
