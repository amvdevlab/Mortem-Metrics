package com.na.postmortemproject.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.na.postmortemproject.entity.Timeline;
import com.na.postmortemproject.service.ParseUploadException;
import com.na.postmortemproject.service.ParseUploadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UploadController {

    private final ParseUploadService parseUploadService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<UploadResponse> upload(@RequestParam("file") MultipartFile file) throws IOException {
        Timeline timeline = parseUploadService.parseAndStore(file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new UploadResponse(timeline.getId(), timeline.getTitle()));
    }

    @ExceptionHandler(ParseUploadException.class)
    public ResponseEntity<ErrorResponse> handleParseUploadException(ParseUploadException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    public record UploadResponse(Long timelineId, String title) {}
    public record ErrorResponse(String error) {}
}
