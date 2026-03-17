package com.sgeu.cad.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
	name = "units",
	uniqueConstraints = {
		@UniqueConstraint(name = "ux_unit_callsign", columnNames = {"callsign"})
	}
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Unit {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 16)
	private Agency agency;

	@Column(nullable = false, length = 32)
	private String callsign;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "home_station_id", nullable = false)
	private Station homeStation;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 24)
	private UnitStatus status;

	@Column(nullable = false)
	private int activePersonnel;

	@Column(nullable = false)
	private int fuelPercent;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "addressLine", column = @Column(name = "last_address_line", length = 240)),
		@AttributeOverride(name = "city", column = @Column(name = "last_city", length = 120)),
		@AttributeOverride(name = "zone", column = @Column(name = "last_zone", length = 64)),
		@AttributeOverride(name = "latitude", column = @Column(name = "last_latitude")),
		@AttributeOverride(name = "longitude", column = @Column(name = "last_longitude"))
	})
	private Location lastKnownLocation;

	@Column(name = "available_from")
	private Instant availableFrom;
}

