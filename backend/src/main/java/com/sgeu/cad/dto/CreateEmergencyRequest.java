package com.sgeu.cad.dto;

import com.sgeu.cad.model.EmergencySeverity;
import com.sgeu.cad.model.EmergencyType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateEmergencyRequest(
	@NotNull EmergencyType type,
	@NotNull EmergencySeverity severity,
	@NotNull @Valid LocationDto location,
	@Min(0) @Max(480) int estimatedInitialResponseMinutes,
	@Size(max = 2000) String notes
) {
}

