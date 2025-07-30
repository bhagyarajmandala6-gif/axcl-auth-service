package com.innocito.axcl.service;

import com.innocito.axcl.config.AWSSQSConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SentryService {
    private final AWSSQSConfigProperties awssqsConfigProperties;
    private final SqsSender sqsSender;

    public void tripWebhook(String webhookRequest) {
        if (StringUtils.isBlank(webhookRequest)) {
            log.error("Webhook request is null or empty.");
            return;
        }
        log.info("Trip webhook request: {}", webhookRequest);
        try {
            sqsSender.sendMessage(awssqsConfigProperties.getEndpoint(), webhookRequest);
        } catch (Exception e) {
            log.error("Error sending sentry trip webhook request to SQS: {}", e.getMessage());
        }
    }
}
