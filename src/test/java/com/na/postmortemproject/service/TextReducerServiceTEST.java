package com.na.postmortemproject.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TextReducerServiceTEST {

    @Autowired
    private TextReducerService textReducerService;

    @Test
    void compress_null_returnsEmptyString() {
        String result = textReducerService.compress(null);
        assertEquals("", result);
    }

    @Test
    void compress_empty_returnsEmptyString() {
        String result = textReducerService.compress("");
        assertEquals("", result);
    }

    @Test
    void compress_lightLevel_collapsesWhitespaceAndDedupesLines() {
        String input = "a   b\t\tc\n\n  d  \n  d  \ne";
        String result = textReducerService.compress(input);
        assertEquals("a b c\nd\ne", result);
    }

    @Test
    void approximateTokenCount_null_returnsZero() {
        assertEquals(0, textReducerService.approximateTokenCount(null));
    }

    @Test
    void approximateTokenCount_empty_returnsZero() {
        assertEquals(0, textReducerService.approximateTokenCount(""));
    }

    @Test
    void approximateTokenCount_knownLength_returnsCeilLengthOverFour() {
        assertEquals(0, textReducerService.approximateTokenCount(""));
        assertEquals(1, textReducerService.approximateTokenCount("a"));
        assertEquals(3, textReducerService.approximateTokenCount("1234567890"));
        assertEquals(3, textReducerService.approximateTokenCount("12"));
    }

    @Test
    void chunkIfNeeded_empty_returnsSingleEmptyChunk() {
        List<String> chunks = textReducerService.chunkIfNeeded("");
        assertNotNull(chunks);
        assertEquals(1, chunks.size());
        assertEquals("", chunks.get(0));
    }

    @Test
    void chunkIfNeeded_null_returnsSingleEmptyChunk() {
        List<String> chunks = textReducerService.chunkIfNeeded(null);
        assertNotNull(chunks);
        assertEquals(1, chunks.size());
        assertEquals("", chunks.get(0));
    }

    @Test
    void chunkIfNeeded_underMaxTokens_returnsSingleChunk() {
        String small = "one two three four";
        List<String> chunks = textReducerService.chunkIfNeeded(small);
        assertNotNull(chunks);
        assertEquals(1, chunks.size());
        assertTrue(chunks.get(0).contains("one"));
    }

    @Test
    void chunkIfNeeded_overMaxTokens_returnsMultipleChunksEachUnderLimit() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 30; i++) {
            sb.append("word").append(i).append(" ");
        }
        List<String> chunks = textReducerService.chunkIfNeeded(sb.toString());
        assertNotNull(chunks);
        assertTrue(chunks.size() >= 2);
        int maxTokens = 25;
        for (String chunk : chunks) {
            assertTrue(textReducerService.approximateTokenCount(chunk) <= maxTokens);
        }
    }

    @Test
    void chunkIfNeeded_largeInput_overOneMegabyte_returnsMultipleChunksWithoutFailure() {
        int targetBytes = 1_100_000;
        StringBuilder sb = new StringBuilder(targetBytes);
        while (sb.length() < targetBytes) {
            sb.append("line ").append(sb.length()).append(" some content here\n");
        }
        String large = sb.toString();
        assertTrue(large.length() > 1_000_000);

        List<String> chunks = textReducerService.chunkIfNeeded(large);
        assertNotNull(chunks);
        assertTrue(chunks.size() >= 2, "Should split into multiple chunks");
        int maxTokens = 25;
        for (String chunk : chunks) {
            assertTrue(textReducerService.approximateTokenCount(chunk) <= maxTokens);
        }
    }
}
