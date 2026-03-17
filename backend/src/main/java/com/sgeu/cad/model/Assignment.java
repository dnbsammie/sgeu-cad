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
	name = "assignments",
	uniqueConstraints = {
		@UniqueConstraint(name = "ux_active_assignment_per_unit", columnNames = {"unit_id", "active"})
	}
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Assignment {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "incident_id", nullable = false)
	private Incident incident;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "unit_id", nullable = false)
	private Unit unit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dispatch_id")
	private Dispatch dispatch;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 16)
	private AssignmentRole role;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 16)
	private AssignmentStatus status;

	@Column(nullable = false)
	private Instant assignedAt;

	@Column
	private Instant releasedAt;

	@Column(nullable = false)
	private boolean active;
}

