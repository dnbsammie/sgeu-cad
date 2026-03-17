package com.sgeu.cad.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(
	name = "stations",
	uniqueConstraints = {
		@UniqueConstraint(name = "ux_station_name", columnNames = {"name"})
	}
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Station {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, length = 120)
	private String name;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "addressLine", column = @Column(name = "address_line", length = 240)),
		@AttributeOverride(name = "city", column = @Column(name = "city", length = 120)),
		@AttributeOverride(name = "zone", column = @Column(name = "zone", length = 64)),
		@AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
		@AttributeOverride(name = "longitude", column = @Column(name = "longitude"))
	})
	private Location location;
}

