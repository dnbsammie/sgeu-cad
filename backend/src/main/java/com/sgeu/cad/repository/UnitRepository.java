package com.sgeu.cad.repository;

import com.sgeu.cad.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, UUID> {
}

