package com.example.demo.jws_session12_bai5.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleProductNotFoundReturnsJson404() {
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleProductNotFound(new ProductNotFoundException(7L));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(new ErrorResponse("Product not found with id: 7", 404));
    }

    @Test
    void handleUnexpectedExceptionReturnsJson500() {
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleUnexpectedException(new RuntimeException("boom"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(new ErrorResponse("Unexpected error occurred", 500));
    }
}
