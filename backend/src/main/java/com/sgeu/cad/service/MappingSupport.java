package com.sgeu.cad.service;

import com.sgeu.cad.dto.LocationDto;
import com.sgeu.cad.model.Location;

public final class MappingSupport {
	private MappingSupport() {
	}

	public static Location toLocation(LocationDto dto) {
		if (dto == null) return null;
		return Location.builder()
			.addressLine(dto.addressLine())
			.city(dto.city())
			.zone(dto.zone())
			.latitude(dto.latitude())
			.longitude(dto.longitude())
			.build();
	}

	public static LocationDto toLocationDto(Location location) {
		if (location == null) return null;
		return new LocationDto(
			location.getAddressLine(),
			location.getCity(),
			location.getZone(),
			location.getLatitude(),
			location.getLongitude()
		);
	}
}

