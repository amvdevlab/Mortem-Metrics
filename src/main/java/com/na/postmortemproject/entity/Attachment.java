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
@Table(name="attachments")
@Getter
@Setter
@NoArgsConstructor

public class Attachment {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="attachment_id")
  private Long id;

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="incident_id", nullable=false)
  private Incident incident;

  @Column(name="file_path", length=200)
  private String filePath;

  @Column(name="file_type", length=50)
  private String fileType;

  @Column(name="uploaded_at", updatable=false)
  private LocalDateTime uploadedAt;

  @Column(name="file_size")
  private Long fileSize;

  @PrePersist
  protected void onCreate() {
    this.uploadedAt = LocalDateTime.now();
  }
}
