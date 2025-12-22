package com.na.postmortemproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.na.postmortemproject.entity.Log;


@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
  // --- Add queries as necessary ---

  List<Log> findByIncidentId(Long incidentId);
}
