package com.na.postmortemproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.na.postmortemproject.entity.Timeline;

public interface TimelineRepository extends JpaRepository<Timeline, Long>{
  
  // Get all timelines sorted by creation date (newest first)
  // Supports the History tab on the Frontend UI
  List<Timeline> findALLByOrderByCreatedAtDesc();
}
