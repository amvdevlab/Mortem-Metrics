package com.na.postmortemproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.na.postmortemproject.entity.Incident;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long>{
  // --- Add queries as necessary ---
}
