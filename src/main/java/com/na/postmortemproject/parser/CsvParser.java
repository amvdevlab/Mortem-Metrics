package com.na.postmortemproject.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CsvParser implements FileParser {
  @Override
  public String extractText(InputStream input) throws IOException {

    if (input == null) {
      throw new IllegalArgumentException("Input stream cannot be null");
    }

    byte[] bytes = input.readAllBytes();

    if (bytes.length == 0) {
      throw new IllegalArgumentException("File is empty");
    }

    String text = new String(bytes, StandardCharsets.UTF_8);
    
    return text;
  }
}
