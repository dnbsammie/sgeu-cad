package com.sgeu.cad.dto;

import com.sgeu.cad.model.EmergencySeverity;
import com.sgeu.cad.model.EmergencyType;

import java.time.Instant;
import java.util.UUID;

public record EmergencyResponse(
	UUID id,
	EmergencyType type,
	EmergencySeverity severity,
	LocationDto location,
	Instant reportedAt,
	int estimatedInitialResponseMinutes,
	String notes
) {
}

