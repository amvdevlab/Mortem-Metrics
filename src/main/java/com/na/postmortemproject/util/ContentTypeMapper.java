package com.na.postmortemproject.util;

import java.util.Map;

public final class ContentTypeMapper {

  private static final Map<String, String> EXTENSION_TO_CONTENT_TYPE = Map.of(
    "csv", "text/csv",
    "json", "application/json",
    "log", "text/x-log",
    "txt", "text/plain"
    
  );

  private static final String FALLBACK_CONTENT_TYPE = "application/octet-stream";

  private ContentTypeMapper() {

  }

  public static String getContentTypeForFilename(String filename) {
    if (filename == null || filename.isBlank()) {
      return FALLBACK_CONTENT_TYPE;
    }

    String trimmed = filename.trim();
    int lastDot = trimmed.lastIndexOf('.');
    if (lastDot < 0 || lastDot == trimmed.length() - 1) {
      return FALLBACK_CONTENT_TYPE;
    }
    
    String extension = trimmed.substring(lastDot + 1).toLowerCase();
    return EXTENSION_TO_CONTENT_TYPE.getOrDefault(extension, FALLBACK_CONTENT_TYPE);

  }
}
