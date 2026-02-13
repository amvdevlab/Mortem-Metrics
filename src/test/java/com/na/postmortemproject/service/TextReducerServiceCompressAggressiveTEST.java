package com.na.postmortemproject.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "postmortem.text-reducer.compression-level=aggressive")
class TextReducerServiceCompressAggressiveTEST {

    @Autowired
    private TextReducerService textReducerService;

    @Test
    void compress_aggressiveLevel_collapsesMultipleNewlines() {
        String input = "a\n\n\n\nb\n\n\nc";
        String result = textReducerService.compress(input);
        assertTrue(result.contains("a") && result.contains("b") && result.contains("c"));
        assertFalse(result.contains("\n\n\n"));
    }
}