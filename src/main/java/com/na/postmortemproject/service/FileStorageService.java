package com.na.postmortemproject.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.na.postmortemproject.util.ContentTypeMapper;
@Service
public class FileStorageService {

    private final WebClient webClient;
    private final String supabaseUrl;
    private final String bucketName;

    public FileStorageService(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.key}") String supabaseKey,
            @Value("${supabase.storage.bucket}") String bucketName) {
        
        this.supabaseUrl = supabaseUrl;
        this.bucketName = bucketName;
        
        // Build WebClient with Supabase configuration
        this.webClient = WebClient.builder()
                .baseUrl(supabaseUrl + "/storage/v1")
                .defaultHeader("Authorization", "Bearer " + supabaseKey)
                .build();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // Validate file is not empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }

        // Validate filename exists
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("File must have a valid filename");
        }

        // Generate unique filename to prevent collisions
        String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        // Security: Prevent path traversal attacks
        if (uniqueFilename.contains("..")) {
            throw new IllegalArgumentException("Invalid filename");
        }

        // Validate file size (10MB limit)
        long maxSize = 10 * 1024 * 1024; // 10MB in bytes
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("File too large. Maximum size: 10MB");
        }

        try {
            // Convert MultipartFile to byte array
            byte[] fileBytes = file.getBytes();

            // Handle content type (default to binary if null)
            String contentType = file.getContentType();
            if (contentType == null || contentType.isBlank() || "application/octet-stream".equalsIgnoreCase(contentType.trim())) {
                contentType = ContentTypeMapper.getContentTypeForFilename(originalFilename);
            }

            // Upload to Supabase Storage
            webClient.post()
                    .uri("/object/{bucket}/{filename}", bucketName, uniqueFilename)
                    .contentType(MediaType.parseMediaType(contentType))
                    .bodyValue(fileBytes)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Build and return public URL
            String publicUrl = supabaseUrl + "/storage/v1/object/public/" 
                             + bucketName + "/" + uniqueFilename;
            
            return publicUrl;

        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + originalFilename, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to storage: " + originalFilename, e);
        }
    }
}
