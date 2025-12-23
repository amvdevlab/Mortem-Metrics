package com.na.postmortemproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class IncidentUploadResponse {
  private Long incidentId; // no exposed to user
  private String title;
  private String successMessage; 
  private Integer filesUploaded;
}
