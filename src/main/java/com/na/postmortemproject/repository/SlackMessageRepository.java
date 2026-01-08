package com.na.postmortemproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.na.postmortemproject.entity.SlackMessage;

@Repository
public interface SlackMessageRepository extends JpaRepository<SlackMessage, Long> {
  List<SlackMessage> findByIncidentId(Long incidentId);
}

