package com.na.postmortemproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidentRequest {
  private String title;
  private String severity;
  private String postmortemStatus;
}
