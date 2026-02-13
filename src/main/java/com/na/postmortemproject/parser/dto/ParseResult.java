package com.na.postmortemproject.parser.dto;

import lombok.Getter;

@Getter
public class ParseResult {
  private final String extractedText;
  private final String fileName;
  private final String fileType;
  private final Long fileSize;
  private final boolean parseSuccess;
  private final String errorMessage;

  private ParseResult(String extractedText, String fileName, String fileType, Long fileSize, boolean parseSuccess, String errorMessage) {
    this.extractedText = extractedText;
    this.fileName = fileName;
    this.fileType = fileType;
    this.fileSize = fileSize;
    this.parseSuccess = parseSuccess;
    this.errorMessage = errorMessage;
  }

  public static ParseResult success(String extractedText, String fileName, String fileType, Long fileSize) {
    return new ParseResult(extractedText, fileName, fileType, fileSize, true, null);
  }

  public static ParseResult failure(String fileName, String fileType, Long fileSize, String errorMessage) {
    return new ParseResult(null, fileName, fileType, fileSize, false, errorMessage);
  }
}