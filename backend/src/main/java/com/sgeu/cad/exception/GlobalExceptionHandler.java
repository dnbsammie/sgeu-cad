package com.sgeu.cad.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiError> notFound(NotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(ApiError.of("NOT_FOUND", ex.getMessage(), Map.of()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex) {
		Map<String, Object> details = new LinkedHashMap<>();
		Map<String, String> fieldErrors = new LinkedHashMap<>();
		for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
			fieldErrors.put(fe.getField(), fe.getDefaultMessage());
		}
		details.put("fields", fieldErrors);
		return ResponseEntity.badRequest()
			.body(ApiError.of("VALIDATION_ERROR", "Request validation failed", details));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiError> constraintViolation(ConstraintViolationException ex) {
		return ResponseEntity.badRequest()
			.body(ApiError.of("VALIDATION_ERROR", ex.getMessage(), Map.of()));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiError> integrity(DataIntegrityViolationException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(ApiError.of("DATA_INTEGRITY_VIOLATION", "Operation violates data constraints", Map.of()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> unknown(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ApiError.of("INTERNAL_ERROR", "Unexpected error", Map.of()));
	}
}

