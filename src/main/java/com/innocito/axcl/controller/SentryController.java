package com.innocito.axcl.controller;

import com.innocito.axcl.service.SentryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.innocito.axcl.util.PropertyNameConstants.SENTRY_WEBHOOK_CLIENT_SECRET;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sentry")
public class SentryController {

    @Value(SENTRY_WEBHOOK_CLIENT_SECRET)
    private String sentryWebhookClientSecret;
    private final SentryService sentryService;

    @PostMapping("webhook/tripreceiver")
    public ResponseEntity<String> receiveWebhook(
            @RequestParam("client-secret") String clientSecret,
            @RequestBody String requestBody) {
        if (!sentryWebhookClientSecret.equals(clientSecret)) {
            log.error("Invalid client secret for Sentry webhook - expected: {}, received: {}",
                    sentryWebhookClientSecret, clientSecret);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        sentryService.tripWebhook(requestBody);
        return ResponseEntity.ok("Webhook received");
    }
}