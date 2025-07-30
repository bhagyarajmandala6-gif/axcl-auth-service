package com.innocito.axcl.service;

import com.innocito.axcl.config.AWSSQSConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import static com.innocito.axcl.util.ApplicationConstants.CORRELATION_ID_HEADER;
import static com.innocito.axcl.util.ApplicationConstants.MDC_KEY;

@Slf4j
@RequiredArgsConstructor
@Service
public class SqsMessageListener {
    private AWSSQSConfigProperties awssqsConfigProperties;

    //@SqsListener(value = "${sentry.webhook.aws.sqs.queueName}")
    public void handleMessage(Message message) {
        String correlationId = message.messageAttributes()
                .getOrDefault(CORRELATION_ID_HEADER, MessageAttributeValue.builder()
                        .stringValue(null).build())
                .stringValue();

        if (correlationId != null) {
            MDC.put(MDC_KEY, correlationId);
        }

        try {
            log.info("Received message: {}", message.body());

        } finally {
            MDC.remove(MDC_KEY); // Always clear after processing
        }
    }
}