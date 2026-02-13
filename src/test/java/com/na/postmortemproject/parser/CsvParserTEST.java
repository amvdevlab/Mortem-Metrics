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

class CsvParserTEST {

    private final CsvParser csvParser = new CsvParser();

    @Test
    void testParseValidCsvContent() throws IOException {
        String csv = "name,age,city\nAlice,30,NYC\nBob,25,LA";
        InputStream input = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8));

        ParseResult result = csvParser.parse(input, "data.csv", "text/csv", 35L);

        assertTrue(result.isParseSuccess());
        assertNotNull(result.getExtractedText());
        assertEquals(csv, result.getExtractedText());
        assertNull(result.getErrorMessage());
        assertEquals("data.csv", result.getFileName());
        assertEquals("text/csv", result.getFileType());
        assertEquals(35L, result.getFileSize());
    }

    @Test
    void testParseNullInput() throws IOException {
        ParseResult result = csvParser.parse(null, "test.csv", "text/csv", 0L);

        assertFalse(result.isParseSuccess());
        assertNull(result.getExtractedText());
        assertNotNull(result.getErrorMessage());
        assertEquals("Input stream cannot be null", result.getErrorMessage());
        assertEquals("test.csv", result.getFileName());
        assertEquals("text/csv", result.getFileType());
        assertEquals(0L, result.getFileSize());
    }

    @Test
    void testParseEmptyFile() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);

        ParseResult result = csvParser.parse(input, "empty.csv", "text/csv", 0L);

        assertFalse(result.isParseSuccess());
        assertNull(result.getExtractedText());
        assertNotNull(result.getErrorMessage());
        assertEquals("File is empty", result.getErrorMessage());
        assertEquals("empty.csv", result.getFileName());
    }

    @Test
    void testMetadataPreservedOnSuccess() throws IOException {
        String csv = "a,b\n1,2";
        InputStream input = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8));
        String fileName = "meta.csv";
        String fileType = "text/csv";
        Long fileSize = 100L;

        ParseResult result = csvParser.parse(input, fileName, fileType, fileSize);

        assertTrue(result.isParseSuccess());
        assertEquals(fileName, result.getFileName());
        assertEquals(fileType, result.getFileType());
        assertEquals(fileSize, result.getFileSize());
    }

    @Test
    void testMetadataPreservedOnFailure() throws IOException {
        ParseResult result = csvParser.parse(null, "fail.csv", "text/csv", 50L);

        assertFalse(result.isParseSuccess());
        assertEquals("fail.csv", result.getFileName());
        assertEquals("text/csv", result.getFileType());
        assertEquals(50L, result.getFileSize());
    }
}