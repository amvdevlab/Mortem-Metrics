package com.na.postmortemproject.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="reports")
@Getter
@Setter
@NoArgsConstructor

public class Report {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="report_id")
  private Long id;

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="incident_id", nullable=false)
  private Incident incident;

  @Column(name="report_name", length=200)
  private String reportName;

  @Column(length=50) // PDF, JSON, etc.
  private String format;

  @Column(name="file_path", length=500)
  private String filePath;

  @Column(name="report_content", columnDefinition="TEXT")
  private String reportContent; 

  @Column(name="generated_at", nullable=false)
  private LocalDateTime generatedAt;

  @Column(name="created_at", nullable=false, updatable=false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    if(this.generatedAt == null) {
      this.generatedAt = now;
    }
  }
}
