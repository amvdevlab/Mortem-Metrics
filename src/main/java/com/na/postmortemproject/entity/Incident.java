package com.na.postmortemproject.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "incidents")
@Getter
@Setter
@NoArgsConstructor

public class Incident {
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="incident_id")
  private Long id;

  @Column(nullable=false, length=200)
  private String title;

  @Column(nullable=false, length=50)
  private String severity;

  @Column(name = "postmortem_status", length=20 )
  private String postmortemStatus;

  @Column(name="created_at", updatable=false)
  private LocalDateTime createdAt;

  @Column(columnDefinition= "jsonb")
  private String tag;

  @Column(name = "created_by_user_id")
  private Integer createdByUserId;

  // Database Relationship Mapping
  @OneToMany(mappedBy= "incident", cascade=CascadeType.ALL, orphanRemoval=true)
  private List<Log> logs;

  @OneToMany(mappedBy= "incident", cascade=CascadeType.ALL, orphanRemoval=true)
  private List<Ticket> tickets;

  @OneToMany(mappedBy= "incident", cascade=CascadeType.ALL, orphanRemoval=true)
  private List<SlackMessage> messages;
  
  @OneToMany(mappedBy= "incident", cascade=CascadeType.ALL, orphanRemoval=true)
  private List<Attachment> attachments;

  @OneToMany(mappedBy= "incident", cascade=CascadeType.ALL, orphanRemoval=true)
  private List<Report> reports;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }
} 
