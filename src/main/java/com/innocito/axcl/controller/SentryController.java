package com.innocito.axcl.controller;

import com.innocito.axcl.service.SentryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.innocito.axcl.util.PropertyNameConstants.SENTRY_WEBHOOK_CLIENT_ID;
import static com.innocito.axcl.util.PropertyNameConstants.SENTRY_WEBHOOK_CLIENT_SECRET;

@Slf4j
@RestController
@RequestMapping("/api/sentry")
public class SentryController {

    @Value(SENTRY_WEBHOOK_CLIENT_ID)
    private String sentryWebhookClientId;
    @Value(SENTRY_WEBHOOK_CLIENT_SECRET)
    private String sentryWebhookClientSecret;
    private SentryService sentryService;

    @PostMapping("trips/webhook")
    public ResponseEntity<String> receiveWebhook(
            @RequestHeader("client-id") String clientId,
            @RequestHeader("client-secret") String clientSecret,
            @RequestBody String requestBody) {
        if (!sentryWebhookClientId.equals(clientId) || !sentryWebhookClientSecret.equals(clientSecret)) {
            log.error("Invalid credentials for Sentry webhook - clientId: {}, clientSecret: {}",
                    clientId, clientSecret);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        sentryService.tripWebhook(requestBody);
        return ResponseEntity.ok("Webhook received");
    }
}