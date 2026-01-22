package com.na.postmortemproject.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="timelines")
@Getter
@Setter
@NoArgsConstructor

public class Timeline {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="timeline_id")
  private Long id;

  @Column(nullable=false, length=200)
  private String title;

  // AI output storage
  @Column(name="timeline_content", nullable=false, columnDefinition="TEXT")
  private String timelineContent;

  @Column(name="file_metadata", columnDefinition="JSONB")
  private String fileMetadata;

  @Column(name="created_at", nullable=false, updatable=false)
  @CreationTimestamp
  private LocalDateTime createdAt;
}
