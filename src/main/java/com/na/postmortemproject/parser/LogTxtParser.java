package com.na.postmortemproject.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.na.postmortemproject.parser.dto.ParseResult;

public class LogTxtParser implements FileParser {

  @Override
  public ParseResult parse(InputStream input, String fileName, String fileType, Long fileSize) throws IOException {
    // Validate input stream
    if (input == null) {
      return ParseResult.failure(fileName, fileType, fileSize, "Input stream cannot be null");
    }

    try {
        // Read all bytes from the input stream
        byte[] bytes = input.readAllBytes();

        // Check if file is empty
        if (bytes.length == 0) {
          return ParseResult.failure(fileName, fileType, fileSize, "File is emtpy");
        }

        // Convert bytes to UTF-8 string
        String text = new String(bytes, StandardCharsets.UTF_8);

        // Return successful result with extracted text
        return ParseResult.success(text, fileName, fileType, fileSize);

    } catch (IOException e) {
      // Handle IO errors during reading
      return ParseResult.failure(fileName, fileType, fileSize, "Error reading file: " + e.getMessage());
   
    } catch (Exception e) {
      // Handle any other unexpected errors
      return ParseResult.failure(fileName, fileType, fileSize, "Unexpected error parsing text file: " + e.getMessage());
    }
  }
}
