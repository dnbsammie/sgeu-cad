package com.sgeu.cad.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
	name = "incidents",
	uniqueConstraints = {
		@UniqueConstraint(name = "ux_incident_emergency", columnNames = {"emergency_id"})
	}
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Incident {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "emergency_id", nullable = false)
	private Emergency emergency;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 24)
	private IncidentStatus status;

	@Column(nullable = false)
	private Instant createdAt;

	@Column
	private Instant resolvedAt;

	@Column(nullable = false)
	private int priorityScore;
}

