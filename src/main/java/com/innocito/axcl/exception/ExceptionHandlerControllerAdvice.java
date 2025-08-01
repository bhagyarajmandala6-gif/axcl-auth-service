package com.innocito.axcl.exception;

import com.innocito.axcl.annotations.Priority;
import com.innocito.axcl.enums.UserRole;
import com.innocito.axcl.model.ApiResponse;
import com.innocito.axcl.model.ErrorDetails;
import com.innocito.axcl.model.TenantContext;
import com.innocito.axcl.model.TenantData;
import com.innocito.axcl.util.BasicUtils;
import com.innocito.axcl.util.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.HandlerMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

import static com.innocito.axcl.util.ApplicationConstants.MDC_KEY;
import static com.innocito.axcl.util.MessageConstants.*;
import static com.innocito.axcl.util.PropertyNameConstants.SPRING_SERVLET_MULTIPART_MAX_FILE_SIZE;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerControllerAdvice {
    private final ResponseUtils responseUtils;
    private final BasicUtils basicUtils;
    @Value(SPRING_SERVLET_MULTIPART_MAX_FILE_SIZE)
    private String maxFileSize;

    @ExceptionHandler(ResourceNotFoundException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleResourceNotFound(final ResourceNotFoundException exception, final HttpServletRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails(exception.getKey(), exception.getMessage()));

        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleHttpMediaTypeNotAcceptableException(final HttpMediaTypeNotAcceptableException exception, final HttpServletRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails(ERROR, exception.getMessage()));

        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleException(final Exception exception, final HttpServletRequest request) {
        log.error(basicUtils.getLocalizedMessage(ERROR_OCCURRED_IN_REQUEST_URL, new Object[]{request.getRequestURI(), exception.getMessage()}));

        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails(ERROR, exception.getMessage()));

        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);
        logFilteredStackTrace(exception);
        triggerNotificationForException(request, exception);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void triggerNotificationForException(HttpServletRequest request, Exception exception) {
        String requestBody = getBody(request);

        // Get request parameters
        List<String> requestParams = getRequestParams(request);

        // Get request headers
        List<String> requestHeaders = getAllHeaders(request);

        // Get client IP address
        String ip = getClientIp(request);

        if (!request.getServerName().equalsIgnoreCase(LOCALHOST)) {

            // Determine priority by inspecting method or class-level annotations
            HandlerMethod handlerMethod = ((HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE));
            Priority priorityAnnotation = handlerMethod.getMethodAnnotation(Priority.class);

            // If not present at the method level, check at the controller (class) level
            if (priorityAnnotation == null) {
                priorityAnnotation = handlerMethod.getBeanType().getAnnotation(Priority.class);
            }

            // Default to "Low" if no annotation is found
            String priority = (priorityAnnotation != null) ? priorityAnnotation.value() : LOW_PRIORITY;

            // Prepare notification content
            String body = prepareNotificationBody(request, ip, requestParams, requestHeaders,
                    Arrays.toString(exception.getStackTrace()),
                    requestBody, exception.getMessage());

            // Extract the service class name (without package) from the stack trace
            String serviceName = Arrays.stream(exception.getStackTrace())
                    .filter(element -> element.getClassName().contains(SERVICE))
                    .findFirst()
                    .map(element -> {
                        try {
                            // Extract only the class name (e.g., "AppointmentService")
                            return Class.forName(element.getClassName()).getSimpleName();
                        } catch (ClassNotFoundException e) {
                            return UNKNOWN_SERVICE;
                        }
                    })
                    .orElse(UNKNOWN_SERVICE);

            // Send notifications
            // TODO
            /*notificationService.sendExceptionNotificationForEmail(priority, body, serviceName);*/
        }
    }

    private List<String> getRequestParams(HttpServletRequest request) {
        return request.getParameterMap().entrySet().stream()
                .map(entry -> entry.getKey() + DOUBLE_QUOTES_WITH_COLUMN_SYMBOL + entry.getValue()[0])
                .toList();
    }

    public List<String> getAllHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        return Collections.list(headerNames).stream()
                .map(headerName -> headerName + DOUBLE_QUOTES_WITH_COLUMN_SYMBOL + request.getHeader(headerName))
                .collect(Collectors.toList());
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (LOCAL_PORT_NUMBER_1.equals(ip) || LOCAL_PORT_NUMBER_2.equals(ip)) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.info(GOT_UNKNOWN_HOST);
            }
        }
        return ip;
    }

    private String prepareNotificationBody(HttpServletRequest request, String ip, List<String> requestParams,
                                           List<String> requestHeaders,
                                           String stackTrace,
                                           Object requestBody, String errorMessage) {
        TenantData tenantData = TenantContext.getTenantData();
        return "An unknown exception occurred.\n\n" +
                "Environment :  " + request.getServerName() + "\n" +
                "IP Address :  " + ip + "\n" +
                "Request URI :  " + request.getRequestURI() + "\n" +
                "Request Method :  " + request.getMethod() + "\n" +
                "User ID :  " + tenantData.getLoggedInUserId() + "\n" +
                "User Type :  " + UserRole.getByValue(tenantData.getLoggedInUserType()).getKey() + "\n" +
                "Request Params :  " + requestParams + "\n" +
                "Request Body :  " + requestBody + "\n" +
                "Headers :  " + requestHeaders + "\n" +
                "Error Message :  " + errorMessage + "\n" +
                "CorrelationId : " + MDC.get(MDC_KEY) + "\n" +
                "Stack Trace :   " + stackTrace;
    }

    private void logFilteredStackTrace(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith(FILE_LOCATIONS)) {
                log.error("Exception at {}.{}(Line {}): {}",
                        element.getClassName(),
                        element.getMethodName(),
                        element.getLineNumber(),
                        e.getMessage(), e);
                break;
            }
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(final MethodArgumentNotValidException exception, final HttpServletRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new ErrorDetails(fieldName, errorMessage.replace("{field_name}", fieldName)));
        });

        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(final ConstraintViolationException exception, final HttpServletRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        exception.getConstraintViolations().forEach((error) -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.add(new ErrorDetails(fieldName, errorMessage));
        });

        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EnumNotFoundException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleEnumNotFoundException(final EnumNotFoundException exception, final HttpServletRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails(exception.getKey(), exception.getMessage()));
        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UniqueConstraintViolationException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleUniqueConstraintViolationException(final UniqueConstraintViolationException exception, final HttpServletRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails(exception.getKey(), exception.getMessage()));

        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleValidationException(final ValidationException exception, final HttpServletRequest request) {
        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, exception.getErrors());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleAuthenticationException(final AuthenticationException exception, final HttpServletRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails(exception.getKey(), exception.getMessage()));

        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleBadCredentialsException(final BadCredentialsException exception, final HttpServletRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails(LOGIN_FAILED, exception.getMessage()));

        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleAuthorizationException(final AuthorizationException exception, final HttpServletRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails(exception.getKey(), exception.getMessage()));

        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);

        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleMissingServletRequestParameterException(final MissingServletRequestParameterException exception, final HttpServletRequest request) {
        String fieldName = exception.getParameterName();
        String errorMessage = exception.getMessage();
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails(fieldName, errorMessage));

        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleAccessDeniedException(final AccessDeniedException exception, final HttpServletRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails(ACCESS_DENIED, exception.getMessage()));

        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception, final HttpServletRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails(exception.getName(), PROVIDE_A_VALID_ARGUMENT_VALUE));

        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadableException(final HttpMessageNotReadableException exception, final HttpServletRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails(BODY, INVALID_REQUEST_BODY_PROVIDED));

        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleMissingServletRequestPartException(final MissingServletRequestPartException exception, final HttpServletRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails(exception.getRequestPartName(), exception.getMessage()));

        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleMaxUploadSizeExceededException(final MaxUploadSizeExceededException exception, final HttpServletRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails(FILE_SIZE, exception.getMessage() + MAX_SIZE_IS + maxFileSize));

        ApiResponse<?> exceptionResponse = responseUtils.error(ERROR_MESSAGE, errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException exception, final HttpServletRequest request) {
        ApiResponse<?> exceptionResponse = getExceptionResponse(exception.getMessage(), request);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MultipartException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleMultipartException(final MultipartException exception, final HttpServletRequest request) {
        ApiResponse<?> exceptionResponse = getExceptionResponse(exception.getMessage(), request);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public @ResponseBody ResponseEntity<ApiResponse<?>> handleHttpMediaTypeNotSupportedException(final HttpMediaTypeNotSupportedException exception, final HttpServletRequest request) {
        ApiResponse<?> exceptionResponse = getExceptionResponse(exception.getMessage(), request);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    private ApiResponse<?> getExceptionResponse(String message, final HttpServletRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails(REQUEST, message));

        return responseUtils.error(ERROR_MESSAGE, errors);
    }

    private String getBody(HttpServletRequest request) {
        if (request.getContentType() != null && request.getContentType().contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            Collection<Part> parts;
            try {
                parts = request.getParts();
            } catch (IOException | ServletException e) {
                throw new RuntimeException(e);
            }
            String formData = parts.stream()
                    .map(part -> {
                        try {
                            return part.getName() + EQUAL_TO + new BufferedReader(new InputStreamReader(part.getInputStream()))
                                    .lines().collect(Collectors.joining(DOUBLE_QUOTE_WITH_SLASH_N));
                        } catch (IOException e) {
                            return part.getName() + ERROR_READING_PART;
                        }
                    })
                    .collect(Collectors.joining(DOUBLE_QUOTE_WITH_COMMA));
            log.error(basicUtils.getLocalizedMessage(FORM_DATA_CONTROLLER_ADVICE, new Object[]{formData}));
            return formData;
        } else {
            try {
                String requestBody = new BufferedReader(request.getReader())
                        .lines()
                        .collect(Collectors.joining(System.lineSeparator()));
                log.error(REQUEST_BODY + requestBody);
                return requestBody;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}