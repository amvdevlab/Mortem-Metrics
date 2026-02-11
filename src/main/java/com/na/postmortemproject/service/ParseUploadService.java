package com.na.postmortemproject.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.na.postmortemproject.entity.Timeline;
import com.na.postmortemproject.parser.FileParser;
import com.na.postmortemproject.parser.ParserFactory;
import com.na.postmortemproject.parser.dto.ParseResult;
import com.na.postmortemproject.repository.TimelineRepository;
import com.na.postmortemproject.util.ContentTypeMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParseUploadService {

    private final ParserFactory parserFactory;
    private final TimelineRepository timelineRepository;
    private final TextReducerService textReducerService;

    /**
     * Parse uploaded file and persist as a Timeline. Content type is taken from
     * the file; if missing or generic, it is inferred from the filename via ContentTypeMapper.
     * Extracted text is optionally compressed (TextReducerService) before storing.
     *
     * @param file the uploaded multipart file
     * @return the saved Timeline with id set
     * @throws IllegalArgumentException if file is null, empty, or has no filename
     * @throws ParseUploadException if parsing fails or content type is unsupported
     */
    public Timeline parseAndStore(MultipartFile file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("File must have a valid filename");
        }

        String contentType = file.getContentType();
        if (contentType == null || contentType.isBlank() || "application/octet-stream".equalsIgnoreCase(contentType.trim())) {
            contentType = ContentTypeMapper.getContentTypeForFilename(originalFilename);
        }

        FileParser parser;
        try {
            parser = parserFactory.getParser(contentType);
        } catch (UnsupportedOperationException e) {
            throw new ParseUploadException("Unsupported file type. " + e.getMessage(), e);
        }

        try (InputStream input = file.getInputStream()) {
            long size = file.getSize();
            ParseResult result = parser.parse(input, originalFilename, contentType, size);

            if (!result.isParseSuccess()) {
                throw new ParseUploadException(result.getErrorMessage());
            }

            String extracted = result.getExtractedText();
            String contentForStorage = textReducerService.compress(extracted);

            Timeline timeline = new Timeline();
            timeline.setTitle(originalFilename);
            timeline.setTimelineContent(contentForStorage);
            timeline.setFileMetadata(buildFileMetadata(originalFilename, contentType, size));

            return timelineRepository.save(timeline);
        } catch (ParseUploadException e) {
            throw e;
        } catch (IOException e) {
            log.warn("IO error parsing file {}: {}", originalFilename, e.getMessage());
            throw new ParseUploadException("Error reading file: " + e.getMessage(), e);
        }
    }

    private static String buildFileMetadata(String fileName, String fileType, long fileSize) {
        return String.format("{\"fileName\":\"%s\",\"fileType\":\"%s\",\"fileSize\":%d}",
                escapeJsonString(fileName), escapeJsonString(fileType), fileSize);
    }

    private static String escapeJsonString(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}