package com.na.postmortemproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.na.postmortemproject.entity.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long>{
  List<Attachment> findByIncidentId(Long incidentId);
}
