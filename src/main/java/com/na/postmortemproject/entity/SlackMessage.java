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
@Table(name="slack_messages")
@Getter
@Setter
@NoArgsConstructor

public class SlackMessage {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="message_id")
  private Long id;

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="incident_id", nullable=false)
  private Incident incident;

  @Column(name="channel_id", nullable=false)
  private String channelId;

  @Column(name="message_text", nullable=false, columnDefinition="TEXT")
  private String messageText;

  @Column(name="parent_message_id")
  private String parentMessageId;

  @Column(name="created_at", nullable=false, updatable=false)
  private LocalDateTime createdAt;

  @Column(columnDefinition="jsonb")
  private String reactions;

  @PrePersist
  protected void onCreate(){
    this.createdAt = LocalDateTime.now();
  }
}
