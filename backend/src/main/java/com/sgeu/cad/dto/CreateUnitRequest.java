package com.sgeu.cad.dto;

import com.sgeu.cad.model.Agency;
import com.sgeu.cad.model.UnitStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateUnitRequest(
	@NotNull Agency agency,
	@NotBlank @Size(max = 32) String callsign,
	@NotNull UUID homeStationId,
	@NotNull UnitStatus status,
	@Min(0) @Max(1000) int activePersonnel,
	@Min(0) @Max(100) int fuelPercent
) {
}

