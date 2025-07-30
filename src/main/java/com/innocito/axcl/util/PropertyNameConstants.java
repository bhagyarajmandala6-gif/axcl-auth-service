package com.innocito.axcl.util;

public class PropertyNameConstants {

    private PropertyNameConstants() {
    }

    public static final String SPRING_PROFILES_ACTIVE = "${spring.profiles.active}";
    public static final String UNHANDLED_ISSUES_NOTIFICATIONS_EMAIL_USERS = "${unhandled.issues.notifications.email.users}";
    public static final String UNHANDLED_ISSUES_NOTIFICATIONS_EMAIL = "${unhandled.issues.notifications.email}";
    public static final String SPRING_SERVLET_MULTIPART_MAX_FILE_SIZE = "${spring.servlet.multipart.max-file-size}";
    public static final String JWT_SIGNING_KEY = "${jwt.signing.key}";
    public static final String APP_VERSION = "${app.version}";
    public static final String IMAGE_UPLOAD_ALLOWED_FILE_EXTENSIONS = "${image.upload.allowed.file.extensions}";
    public static final String IMAGE_UPLOAD_ALLOWED_CONTENT_TYPES = "${image.upload.allowed.content.types}";
    public static final String SPRING_ACTIVE_PROFILE = "spring.profiles.active";
    public static final String SENTRY_WEBHOOK_CLIENT_ID = "${sentry.webhook.client.id}";
    public static final String SENTRY_WEBHOOK_CLIENT_SECRET = "${sentry.webhook.client.secret}";
}
