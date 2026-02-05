package com.na.postmortemproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "postmortem.text-reducer.compression-level=none")
class TextReducerServiceCompressNoneTEST {

    @Autowired
    private TextReducerService textReducerService;

    @Test
    void compress_noneLevel_returnsIdentity() {
        String input = "  hello   world  \n\n  foo  ";
        String result = textReducerService.compress(input);
        assertEquals(input, result);
    }
}