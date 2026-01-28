package com.na.postmortemproject.parser;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ParserFactory {


  public FileParser getParser (String contentType) {
    
    if (contentType == null) {
      throw new IllegalArgumentException("Content type cannot be null");
    }
    
    // debug input
    log.debug("Getting parser for content type: {}", contentType);

    String baseType = contentType.split(";")[0].trim().toLowerCase();

    FileParser parser =  switch (baseType) {

      case "text/csv", "application/csv" -> new CsvParser();
      case "application/json", "text/json" -> new JsonParser();


      default -> throw new UnsupportedOperationException(
        "Unsupported file type: " + contentType + ". Currently supported: CSV, JSON"
      );
    };
    
    // debug output
    log.debug("Returning parser: {}", parser.getClass().getSimpleName());
    
    return parser;
  }
}
