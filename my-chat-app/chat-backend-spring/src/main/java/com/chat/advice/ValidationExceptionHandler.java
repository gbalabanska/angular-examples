package com.chat.advice;

import com.chat.reponse.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.chat.errors.*;

import java.util.HashMap;
import java.util.Map;

/*
 * Аспект-ориентиран подход за обработка на грешни заявки и вътрешни грешки в приложението.
 * */
@ControllerAdvice
public class ValidationExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ValidationExceptionHandler.class);

    /**
     * Обработва невалидни параметри в заявките.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // Log the validation errors
        logger.error("Validation failed: {}", errors);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработва неочаквани грешки.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAllExceptions(Exception ex) {
        // Log the unexpected error
        logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>> Unexpected error occurred", ex);

        // Use reflection to check the package name
        String packageName = ex.getClass().getPackage().getName();

        ApiResponse response = new ApiResponse();

        // Handle exceptions based on the package name dynamically
        if (packageName.startsWith("com.chat.errors")) {
            // Handle custom exceptions from com.chat.errors package
            response.setMessage(ex.getMessage());
            response.setSuccess(false);

            // Customize HTTP status based on exception type or default
            HttpStatus status = getStatusForException(ex);
            return new ResponseEntity<>(response, status);
        } else {
            // Handle all other exceptions generically
            response.setMessage("An unexpected error occurred.");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private HttpStatus getStatusForException(Exception ex) {
        // You can add specific logic for HTTP status based on exception type
        if (ex instanceof ChannelNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof UserNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof ChannelPermissionException) {
            return HttpStatus.FORBIDDEN;
        } else if (ex instanceof UserAlreadyInChannelException) {
            return HttpStatus.BAD_REQUEST;
        } else if(ex instanceof UsersAreNotFriendsException){
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR; // Default status
    }
}