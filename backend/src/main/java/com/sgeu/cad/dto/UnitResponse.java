package com.sgeu.cad.dto;

import com.sgeu.cad.model.Agency;
import com.sgeu.cad.model.UnitStatus;

import java.time.Instant;
import java.util.UUID;

public record UnitResponse(
	UUID id,
	Agency agency,
	String callsign,
	UUID homeStationId,
	UnitStatus status,
	int activePersonnel,
	int fuelPercent,
	LocationDto lastKnownLocation,
	Instant availableFrom
) {
}

