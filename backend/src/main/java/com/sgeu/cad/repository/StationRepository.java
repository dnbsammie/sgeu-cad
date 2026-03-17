package com.sgeu.cad.repository;

import com.sgeu.cad.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StationRepository extends JpaRepository<Station, UUID> {
}

