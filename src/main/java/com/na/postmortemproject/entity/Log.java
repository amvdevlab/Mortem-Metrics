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
@Table(name="logs")
@Getter
@Setter
@NoArgsConstructor

public class Log {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="log_id")
  private Long id;

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="incident_id", nullable=false)
  private Incident incident;

  @Column(name="log_level", nullable=false, length=50)
  private String logLevel;

  @Column(name="timestamp", nullable=false)
  private LocalDateTime timeStamp;

  @Column(name="created_at", nullable=false, updatable=false)
  private LocalDateTime createdAt;

  @Column(name="raw_log_text", nullable=false, columnDefinition="TEXT")
  private String rawLogText;

  @PrePersist
  protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    if (this.timeStamp == null){
      this.timeStamp = now;
    }
    this.createdAt = now;
  }
}
