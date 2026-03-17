package com.sgeu.cad.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

public record LocationDto(
	@Size(max = 240) String addressLine,
	@Size(max = 120) String city,
	@Size(max = 64) String zone,
	@DecimalMin(value = "-90.0", inclusive = true) @DecimalMax(value = "90.0", inclusive = true) Double latitude,
	@DecimalMin(value = "-180.0", inclusive = true) @DecimalMax(value = "180.0", inclusive = true) Double longitude
) {
}

