package com.group8.hsf302.bus_ticket_booking.Presentation.Exception;

import com.group8.hsf302.bus_ticket_booking.Domain.Exception.StaffBusinessException;
import com.group8.hsf302.bus_ticket_booking.Presentation.Controller.StaffController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice(
        assignableTypes = StaffController.class
)
public class StaffApiExceptionHandler {

    @ExceptionHandler(
            StaffBusinessException.class
    )
    public ResponseEntity<Map<String, Object>>
    handleStaffBusinessException(
            StaffBusinessException exception
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String, Object>>
    handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> fieldErrors =
                new LinkedHashMap<>();

        for (FieldError fieldError
                : exception
                .getBindingResult()
                .getFieldErrors()) {

            fieldErrors.put(
                    fieldError.getField(),
                    fieldError
                            .getDefaultMessage()
            );
        }

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now()
        );
        response.put(
                "status",
                HttpStatus.BAD_REQUEST.value()
        );
        response.put(
                "error",
                "Validation failed"
        );
        response.put(
                "fieldErrors",
                fieldErrors
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(
            HttpMessageNotReadableException.class
    )
    public ResponseEntity<Map<String, Object>>
    handleUnreadableRequest(
            HttpMessageNotReadableException exception
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Request body is invalid or has an invalid enum value"
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
    handleUnexpectedException(
            Exception exception
    ) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected server error occurred"
        );
    }

    private ResponseEntity<Map<String, Object>>
    buildResponse(
            HttpStatus status,
            String message
    ) {
        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now()
        );
        response.put(
                "status",
                status.value()
        );
        response.put(
                "error",
                status.getReasonPhrase()
        );
        response.put(
                "message",
                message
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }
}