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
@Table(name="tickets")
@Getter
@Setter
@NoArgsConstructor

public class Ticket {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="ticket_id")
  private Long id;

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="incident_id", nullable=false)
  private Incident incident;

  @Column(nullable=false, length=200)
  private String title;

  @Column(length=100)
  private String assignee;

  @Column(name="ticket_source", length=50)
  private String ticketSource;

  @Column(name="created_at", nullable=false, updatable=false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }
}
