package com.na.postmortemproject.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class IncidentResponse {
  private Long id;
  private String title;
  private String severity;
  private String postmortemStatus;
  private LocalDateTime createdAt;
  private String tag;
  private Integer createdByUserId;
}
