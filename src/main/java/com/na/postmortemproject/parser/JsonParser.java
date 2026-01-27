package com.na.postmortemproject.parser;

import java.io.IOException;
import java.io.InputStream;

import com.na.postmortemproject.parser.dto.ParseResult;

import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class JsonParser implements FileParser {
    
    private final ObjectMapper objectMapper;

    public JsonParser() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ParseResult parse(InputStream input, String fileName, String fileType, Long fileSize) throws IOException {
        // Validate input stream
        if (input == null) {
            return ParseResult.failure(fileName, fileType, fileSize, "Input stream cannot be null");
        }

        try {
            // Read and parse JSON
            JsonNode jsonNode = objectMapper.readTree(input);
            
            // Convert to pretty-printed formatted string
            String formattedJson = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(jsonNode);
            
            // Return successful result
            return ParseResult.success(formattedJson, fileName, fileType, fileSize);
            
        } catch (StreamReadException | DatabindException e) {
            // Handle JSON parsing/processing errors with specific messages
            String errorMessage = (e instanceof StreamReadException) 
                ? "Invalid JSON syntax: " + e.getMessage()
                : "JSON processing error: " + e.getMessage();
            
            return ParseResult.failure(fileName, fileType, fileSize, errorMessage);
                    
        } catch (Exception e) {
            // Catch any other unexpected errors
            return ParseResult.failure(fileName, fileType, fileSize, 
                    "Unexpected error parsing JSON: " + e.getMessage());
        }
    }
}