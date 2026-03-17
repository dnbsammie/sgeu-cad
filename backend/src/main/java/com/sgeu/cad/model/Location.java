package com.sgeu.cad.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Location {
	@Column(name = "address_line", length = 240)
	private String addressLine;

	@Column(name = "city", length = 120)
	private String city;

	@Column(name = "zone", length = 64)
	private String zone;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;
}

