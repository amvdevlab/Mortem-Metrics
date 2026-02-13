package com.na.postmortemproject.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class ParserFactoryTEST {

    private final ParserFactory parserFactory = new ParserFactory();

    @Test
    void testGetParser_csv_returnsCsvParser() {
        FileParser parser = parserFactory.getParser("text/csv");
        assertNotNull(parser);
        assertTrue(parser instanceof CsvParser);
    }

    @Test
    void testGetParser_json_returnsJsonParser() {
        FileParser parser = parserFactory.getParser("application/json");
        assertNotNull(parser);
        assertTrue(parser instanceof JsonParser);
    }

    @Test
    void testGetParser_textJson_returnsJsonParser() {
        FileParser parser = parserFactory.getParser("text/json");
        assertNotNull(parser);
        assertTrue(parser instanceof JsonParser);
    }

    @Test
    void testGetParser_xLog_returnsLogTxtParser() {
        FileParser parser = parserFactory.getParser("text/x-log");
        assertNotNull(parser);
        assertTrue(parser instanceof LogTxtParser);
    }

    @Test
    void testGetParser_plainText_returnsLogTxtParser() {
        FileParser parser = parserFactory.getParser("text/plain");
        assertNotNull(parser);
        assertTrue(parser instanceof LogTxtParser);
    }

    @Test
    void testGetParser_csvWithCharset_returnsCsvParser() {
        FileParser parser = parserFactory.getParser("text/csv; charset=utf-8");
        assertNotNull(parser);
        assertTrue(parser instanceof CsvParser);
    }

    @Test
    void testGetParser_nullContentType_throwsIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> parserFactory.getParser(null));
        assertEquals("Content type cannot be null", thrown.getMessage());
    }

    @Test
    void testGetParser_unsupportedContentType_throwsUnsupportedOperationException() {
        UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class,
                () -> parserFactory.getParser("application/pdf"));
        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Unsupported file type"));
        assertTrue(thrown.getMessage().contains("CSV"));
        assertTrue(thrown.getMessage().contains("JSON"));
    }
}