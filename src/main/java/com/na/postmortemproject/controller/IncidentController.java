package com.na.postmortemproject.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.na.postmortemproject.dto.IncidentUploadResponse;
import com.na.postmortemproject.entity.Incident;
import com.na.postmortemproject.service.IncidentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<IncidentUploadResponse> uploadIncident(
            @RequestParam("title") String title,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        
        try {
            // Call service to create incident with attachments
            Incident incident = incidentService.createIncidentWithAttachments(title, files);
            
            // Count successfully uploaded files
            int filesCount = (files != null) ? (int) files.stream().filter(f -> !f.isEmpty()).count() : 0;
            
            // Build response DTO
            IncidentUploadResponse response = new IncidentUploadResponse();
            response.setIncidentId(incident.getId());
            response.setTitle(incident.getTitle());
            response.setSuccessMessage("Incident created successfully");
            response.setFilesUploaded(filesCount);
            
            // Return 201 Created with response body
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            // Handle validation errors (400 Bad Request)
            IncidentUploadResponse errorResponse = new IncidentUploadResponse();
            errorResponse.setSuccessMessage("Validation error: " + e.getMessage());
            errorResponse.setFilesUploaded(0);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            // Handle all other errors (500 Internal Server Error)
            IncidentUploadResponse errorResponse = new IncidentUploadResponse();
            errorResponse.setSuccessMessage("Error creating incident: " + e.getMessage());
            errorResponse.setFilesUploaded(0);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

