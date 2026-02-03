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
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import com.na.postmortemproject.parser.dto.ParseResult;

class JsonParserTest {

    private final JsonParser jsonParser = new JsonParser();

    @Test
    void testParseValidSimpleJson() throws IOException {
        // Given: A valid simple JSON string
        String json = "{\"name\":\"John\",\"age\":30}";
        InputStream input = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        
        // When: Parse the JSON
        ParseResult result = jsonParser.parse(input, "test.json", "application/json", 25L);
        
        // Then: Should succeed and return formatted JSON
        assertTrue(result.isParseSuccess(), "Parse should succeed for valid JSON");
        assertNotNull(result.getExtractedText(), "Extracted text should not be null");
        assertTrue(result.getExtractedText().contains("John"), "Extracted text should contain parsed data");
        assertTrue(result.getExtractedText().contains("age"), "Extracted text should contain all fields");
        assertNull(result.getErrorMessage(), "Error message should be null on success");
        
        // Verify metadata is preserved
        assertEquals("test.json", result.getFileName());
        assertEquals("application/json", result.getFileType());
        assertEquals(25L, result.getFileSize());
    }

    @Test
    void testParseValidComplexJson() throws IOException {
        // Given: Complex JSON with nested objects and arrays
        String complexJson = "{\"users\":[{\"name\":\"Alice\",\"roles\":[\"admin\",\"user\"]},{\"name\":\"Bob\",\"roles\":[\"user\"]}]}";
        InputStream input = new ByteArrayInputStream(complexJson.getBytes(StandardCharsets.UTF_8));
        
        // When: Parse complex JSON
        ParseResult result = jsonParser.parse(input, "complex.json", "application/json", 100L);
        
        // Then: Should successfully parse and format
        assertTrue(result.isParseSuccess(), "Parse should succeed for complex JSON");
        assertNotNull(result.getExtractedText());
        assertTrue(result.getExtractedText().contains("users"), "Should contain users array");
        assertTrue(result.getExtractedText().contains("Alice"), "Should contain nested data");
        assertTrue(result.getExtractedText().contains("roles"), "Should contain nested arrays");
    }

    @Test
    void testParseMalformedJson_MissingClosingBrace() throws IOException {
        // Given: Invalid JSON (missing closing brace)
        String invalidJson = "{\"name\":\"John\"";
        InputStream input = new ByteArrayInputStream(invalidJson.getBytes(StandardCharsets.UTF_8));
        
        // When: Parse the invalid JSON
        ParseResult result = jsonParser.parse(input, "bad.json", "application/json", 15L);
        
        // Then: Should fail with appropriate error message
        assertFalse(result.isParseSuccess(), "Parse should fail for malformed JSON");
        assertNotNull(result.getErrorMessage(), "Error message should not be null");
        assertTrue(result.getErrorMessage().contains("Invalid JSON syntax"), 
                "Error message should indicate syntax error");
        assertNull(result.getExtractedText(), "Extracted text should be null on failure");
        
        // Verify metadata is still preserved
        assertEquals("bad.json", result.getFileName());
        assertEquals("application/json", result.getFileType());
        assertEquals(15L, result.getFileSize());
    }

    @Test
    void testParseMalformedJson_InvalidSyntax() throws IOException {
        // Given: Invalid JSON with syntax error (trailing comma)
        String invalidJson = "{\"name\":\"John\",}";
        InputStream input = new ByteArrayInputStream(invalidJson.getBytes(StandardCharsets.UTF_8));
        
        // When: Parse the invalid JSON
        ParseResult result = jsonParser.parse(input, "invalid.json", "application/json", 18L);
        
        // Then: Should fail
        assertFalse(result.isParseSuccess());
        assertNotNull(result.getErrorMessage());
        assertTrue(result.getErrorMessage().contains("Invalid JSON syntax") || 
                   result.getErrorMessage().contains("JSON processing error"));
    }

    @Test
    void testParseNullInput() throws IOException {
        // Given: Null input stream
        // When: Parse null
        ParseResult result = jsonParser.parse(null, "test.json", "application/json", 0L);
        
        // Then: Should fail with validation error
        assertFalse(result.isParseSuccess(), "Parse should fail for null input");
        assertNotNull(result.getErrorMessage(), "Error message should not be null");
        assertEquals("Input stream cannot be null", result.getErrorMessage(), 
                "Error message should indicate null input");
        assertNull(result.getExtractedText(), "Extracted text should be null on failure");
    }

    @Test
    void testParseEmptyJson() throws IOException {
        // Given: Empty JSON object
        String emptyJson = "{}";
        InputStream input = new ByteArrayInputStream(emptyJson.getBytes(StandardCharsets.UTF_8));
        
        // When: Parse empty JSON
        ParseResult result = jsonParser.parse(input, "empty.json", "application/json", 2L);
        
        if (!result.isParseSuccess()) {
            fail("Empty JSON object should be valid. Error: " + result.getErrorMessage());
        }
        
        // Then: Should succeed (empty object is valid JSON)
        assertNotNull(result.getExtractedText());
        String normalized = result.getExtractedText().replaceAll("\\s+", "");
        assertTrue(normalized.equals("{}"), "Extracted text should represent empty JSON. Got: " + result.getExtractedText());
    }

    @Test
    void testParseJsonWithSpecialCharacters() throws IOException {
        // Given: JSON with special characters (quotes, newlines, unicode)
        String jsonWithSpecialChars = "{\"message\":\"Hello \\\"World\\\"\\nNew Line\",\"unicode\":\"\\u00A9\"}";
        InputStream input = new ByteArrayInputStream(jsonWithSpecialChars.getBytes(StandardCharsets.UTF_8));
        
        // When: Parse JSON with special characters
        ParseResult result = jsonParser.parse(input, "special.json", "application/json", 50L);
        
        // Then: Should successfully parse
        assertTrue(result.isParseSuccess(), "JSON with special characters should parse successfully");
        assertNotNull(result.getExtractedText());
        assertTrue(result.getExtractedText().contains("message"));
    }

    @Test
    void testParseJsonArray() throws IOException {
        // Given: JSON array (not object)
        String jsonArray = "[{\"id\":1,\"name\":\"Item1\"},{\"id\":2,\"name\":\"Item2\"}]";
        InputStream input = new ByteArrayInputStream(jsonArray.getBytes(StandardCharsets.UTF_8));
        
        // When: Parse JSON array
        ParseResult result = jsonParser.parse(input, "array.json", "application/json", 60L);
        
        // Then: Should successfully parse
        assertTrue(result.isParseSuccess(), "JSON array should parse successfully");
        assertNotNull(result.getExtractedText());
        assertTrue(result.getExtractedText().contains("Item1"));
        assertTrue(result.getExtractedText().contains("Item2"));
    }

    @Test
    void testParseJsonWithNumbers() throws IOException {
        // Given: JSON with various number types
        String jsonWithNumbers = "{\"integer\":42,\"decimal\":3.14,\"negative\":-10,\"scientific\":1.5e10}";
        InputStream input = new ByteArrayInputStream(jsonWithNumbers.getBytes(StandardCharsets.UTF_8));
        
        // When: Parse JSON with numbers
        ParseResult result = jsonParser.parse(input, "numbers.json", "application/json", 70L);
        
        // Then: Should successfully parse
        assertTrue(result.isParseSuccess());
        assertNotNull(result.getExtractedText());
        assertTrue(result.getExtractedText().contains("42"));
        assertTrue(result.getExtractedText().contains("3.14"));
    }

    @Test
    void testParseJsonWithBooleansAndNull() throws IOException {
        // Given: JSON with boolean and null values
        String json = "{\"active\":true,\"inactive\":false,\"value\":null}";
        InputStream input = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        
        // When: Parse JSON
        ParseResult result = jsonParser.parse(input, "booleans.json", "application/json", 40L);
        
        // Then: Should successfully parse
        assertTrue(result.isParseSuccess());
        assertNotNull(result.getExtractedText());
        assertTrue(result.getExtractedText().contains("true"));
        assertTrue(result.getExtractedText().contains("false"));
        assertTrue(result.getExtractedText().contains("null"));
    }

    @Test
    void testMetadataPreservedOnSuccess() throws IOException {
        // Given: Valid JSON with specific metadata
        String json = "{\"test\":\"data\"}";
        InputStream input = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        String fileName = "my-custom-file.json";
        String fileType = "application/json";
        Long fileSize = 12345L;
        
        // When: Parse with specific metadata
        ParseResult result = jsonParser.parse(input, fileName, fileType, fileSize);
        
        // Then: All metadata should be preserved
        assertEquals(fileName, result.getFileName(), "FileName should be preserved");
        assertEquals(fileType, result.getFileType(), "FileType should be preserved");
        assertEquals(fileSize, result.getFileSize(), "FileSize should be preserved");
    }

    @Test
    void testMetadataPreservedOnFailure() throws IOException {
        // Given: Invalid JSON with specific metadata
        String invalidJson = "{invalid}";
        InputStream input = new ByteArrayInputStream(invalidJson.getBytes(StandardCharsets.UTF_8));
        String fileName = "error-file.json";
        String fileType = "application/json";
        Long fileSize = 999L;
        
        // When: Parse invalid JSON
        ParseResult result = jsonParser.parse(input, fileName, fileType, fileSize);
        
        // Then: Metadata should still be preserved even on failure
        assertFalse(result.isParseSuccess());
        assertEquals(fileName, result.getFileName(), "FileName should be preserved on failure");
        assertEquals(fileType, result.getFileType(), "FileType should be preserved on failure");
        assertEquals(fileSize, result.getFileSize(), "FileSize should be preserved on failure");
    }

    @Test
    void testFormattedOutputIsPrettyPrinted() throws IOException {
        // Given: Compact JSON (no formatting)
        String compactJson = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        InputStream input = new ByteArrayInputStream(compactJson.getBytes(StandardCharsets.UTF_8));
        
        // When: Parse and format
        ParseResult result = jsonParser.parse(input, "compact.json", "application/json", 40L);
        
        // Then: Output should be pretty-printed (formatted with indentation)
        assertTrue(result.isParseSuccess());
        String formatted = result.getExtractedText();
        
        // Pretty-printed JSON should have newlines and indentation
        assertTrue(formatted.contains("\n") || formatted.contains("\r"), 
                "Formatted JSON should contain newlines");
        // Should have proper structure (not all on one line like input)
        assertTrue(formatted.length() > compactJson.length(), 
                "Formatted JSON should be longer than compact input");
    }
}