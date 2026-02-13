package com.na.postmortemproject.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.na.postmortemproject.parser.dto.ParseResult;

class LogTxtParserTEST {

    private final LogTxtParser logTxtParser = new LogTxtParser();

    @Test
    void testParseValidTextContent() throws IOException {
        String text = "2024-01-15 10:00:00 ERROR Something failed\n2024-01-15 10:01:00 INFO Recovered";
        InputStream input = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));

        ParseResult result = logTxtParser.parse(input, "app.log", "text/x-log", 65L);

        assertTrue(result.isParseSuccess());
        assertNotNull(result.getExtractedText());
        assertEquals(text, result.getExtractedText());
        assertNull(result.getErrorMessage());
        assertEquals("app.log", result.getFileName());
        assertEquals("text/x-log", result.getFileType());
        assertEquals(65L, result.getFileSize());
    }

    @Test
    void testParseNullInput() throws IOException {
        ParseResult result = logTxtParser.parse(null, "test.log", "text/plain", 0L);

        assertFalse(result.isParseSuccess());
        assertNull(result.getExtractedText());
        assertNotNull(result.getErrorMessage());
        assertEquals("Input stream cannot be null", result.getErrorMessage());
        assertEquals("test.log", result.getFileName());
        assertEquals("text/plain", result.getFileType());
        assertEquals(0L, result.getFileSize());
    }

    @Test
    void testParseEmptyFile() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);

        ParseResult result = logTxtParser.parse(input, "empty.txt", "text/plain", 0L);

        assertFalse(result.isParseSuccess());
        assertNull(result.getExtractedText());
        assertNotNull(result.getErrorMessage());
        assertEquals("File is empty", result.getErrorMessage());
        assertEquals("empty.txt", result.getFileName());
    }

    @Test
    void testMetadataPreservedOnSuccess() throws IOException {
        String text = "line1\nline2";
        InputStream input = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        String fileName = "notes.txt";
        String fileType = "text/plain";
        Long fileSize = 200L;

        ParseResult result = logTxtParser.parse(input, fileName, fileType, fileSize);

        assertTrue(result.isParseSuccess());
        assertEquals(fileName, result.getFileName());
        assertEquals(fileType, result.getFileType());
        assertEquals(fileSize, result.getFileSize());
    }

    @Test
    void testMetadataPreservedOnFailure() throws IOException {
        ParseResult result = logTxtParser.parse(null, "fail.txt", "text/plain", 50L);

        assertFalse(result.isParseSuccess());
        assertEquals("fail.txt", result.getFileName());
        assertEquals("text/plain", result.getFileType());
        assertEquals(50L, result.getFileSize());
    }
}