package com.sgeu.cad.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "emergencies")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Emergency {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 16)
	private EmergencyType type;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 8)
	private EmergencySeverity severity;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "addressLine", column = @Column(name = "address_line", length = 240)),
		@AttributeOverride(name = "city", column = @Column(name = "city", length = 120)),
		@AttributeOverride(name = "zone", column = @Column(name = "zone", length = 64)),
		@AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
		@AttributeOverride(name = "longitude", column = @Column(name = "longitude"))
	})
	private Location location;

	@Column(nullable = false)
	private Instant reportedAt;

	@Column(nullable = false)
	private int estimatedInitialResponseMinutes;

	@Column(length = 2000)
	private String notes;
}

