package com.na.postmortemproject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.na.postmortemproject.entity.Attachment;
import com.na.postmortemproject.entity.Incident;
import com.na.postmortemproject.repository.AttachmentRepository;
import com.na.postmortemproject.repository.IncidentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private static final String DEFAULT_SEVERITY = "PENDING";
    private static final String DEFAULT_STATUS = "DRAFT";
    private static final int MAX_FILES_PER_INCIDENT = 10;

    private final IncidentRepository incidentRepository;
    private final AttachmentRepository attachmentRepository;
    private final FileStorageService fileStorageService;

    @Transactional 
    public Incident createIncidentWithAttachments(String title, List<MultipartFile> files) {
        // Validate title
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (title.length() > 200) {
            throw new IllegalArgumentException("Title must not exceed 200 characters");
        }

        // Validate file count
        if (files != null && files.size() > MAX_FILES_PER_INCIDENT) {
            throw new IllegalArgumentException("Maximum " + MAX_FILES_PER_INCIDENT + " files allowed per incident");
        }

        // Step 1: Create and configure incident
        Incident incident = new Incident();
        incident.setTitle(title);
        incident.setSeverity(DEFAULT_SEVERITY); 
        incident.setPostmortemStatus(DEFAULT_STATUS);

        // Step 2: Save incident to generate ID
        incident = incidentRepository.save(incident);

        // Step 3: Process attachments if files provided
        if (files != null && !files.isEmpty()) {
            List<Attachment> attachments = new ArrayList<>();

            for (MultipartFile file : files) {
                if (!file.isEmpty()) { //HTML form might send empty file inputs
                    try {
                        // Upload file to Supabase Storage
                        String fileUrl = fileStorageService.uploadFile(file);

                        // Create attachment record
                        Attachment attachment = new Attachment();
                        attachment.setIncident(incident);
                        attachment.setFilePath(fileUrl);
                        attachment.setFileType(file.getContentType());
                        attachment.setFileSize(file.getSize());

                        attachments.add(attachment);

                    } catch (RuntimeException | java.io.IOException e) {
                        throw new RuntimeException("Failed to process file: " + file.getOriginalFilename() + " - " + e.getMessage(), e);
                    }
                }
            }

            // Step 4: Save all attachments
            if (!attachments.isEmpty()) {
                attachmentRepository.saveAll(attachments);
            }
        }

        // Step 5: Return the created incident
        return incident;
    }
}
