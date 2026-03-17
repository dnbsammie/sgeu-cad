package com.sgeu.cad.service;

import com.sgeu.cad.dto.CreateEmergencyRequest;
import com.sgeu.cad.dto.EmergencyResponse;
import com.sgeu.cad.exception.NotFoundException;
import com.sgeu.cad.model.Emergency;
import com.sgeu.cad.model.Incident;
import com.sgeu.cad.model.IncidentEvent;
import com.sgeu.cad.model.IncidentEventType;
import com.sgeu.cad.model.IncidentStatus;
import com.sgeu.cad.repository.EmergencyRepository;
import com.sgeu.cad.repository.IncidentEventRepository;
import com.sgeu.cad.repository.IncidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static com.sgeu.cad.service.MappingSupport.toLocation;
import static com.sgeu.cad.service.MappingSupport.toLocationDto;

@Service
public class EmergencyService {
	private final EmergencyRepository emergencyRepository;
	private final IncidentRepository incidentRepository;
	private final IncidentEventRepository incidentEventRepository;

	public EmergencyService(
		EmergencyRepository emergencyRepository,
		IncidentRepository incidentRepository,
		IncidentEventRepository incidentEventRepository
	) {
		this.emergencyRepository = emergencyRepository;
		this.incidentRepository = incidentRepository;
		this.incidentEventRepository = incidentEventRepository;
	}

	@Transactional
	public EmergencyResponse logEmergency(CreateEmergencyRequest request, String actor) {
		Instant now = Instant.now();

		Emergency emergency = Emergency.builder()
			.type(request.type())
			.severity(request.severity())
			.location(toLocation(request.location()))
			.reportedAt(now)
			.estimatedInitialResponseMinutes(request.estimatedInitialResponseMinutes())
			.notes(request.notes())
			.build();

		emergency = emergencyRepository.save(emergency);

		int priorityScore = switch (request.severity()) {
			case HIGH -> 100;
			case MEDIUM -> 50;
			case LOW -> 10;
		};

		Incident incident = Incident.builder()
			.emergency(emergency)
			.status(IncidentStatus.LOGGED)
			.createdAt(now)
			.resolvedAt(null)
			.priorityScore(priorityScore)
			.build();
		incident = incidentRepository.save(incident);

		incidentEventRepository.save(IncidentEvent.builder()
			.incident(incident)
			.type(IncidentEventType.LOGGED)
			.at(now)
			.actor(actor)
			.payload("{\"emergencyId\":\"" + emergency.getId() + "\"}")
			.build());

		return new EmergencyResponse(
			emergency.getId(),
			emergency.getType(),
			emergency.getSeverity(),
			toLocationDto(emergency.getLocation()),
			emergency.getReportedAt(),
			emergency.getEstimatedInitialResponseMinutes(),
			emergency.getNotes()
		);
	}

	@Transactional(readOnly = true)
	public EmergencyResponse getEmergency(UUID id) {
		Emergency emergency = emergencyRepository.findById(id)
			.orElseThrow(() -> new NotFoundException("Emergency not found: " + id));
		return new EmergencyResponse(
			emergency.getId(),
			emergency.getType(),
			emergency.getSeverity(),
			toLocationDto(emergency.getLocation()),
			emergency.getReportedAt(),
			emergency.getEstimatedInitialResponseMinutes(),
			emergency.getNotes()
		);
	}
}

