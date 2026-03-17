package com.sgeu.cad.exception;

import java.time.Instant;
import java.util.Map;

public record ApiError(
	Instant at,
	String code,
	String message,
	Map<String, Object> details
) {
	public static ApiError of(String code, String message, Map<String, Object> details) {
		return new ApiError(Instant.now(), code, message, details == null ? Map.of() : details);
	}
}

