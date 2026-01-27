package com.na.postmortemproject.parser;

import java.io.IOException;
import java.io.InputStream;

import com.na.postmortemproject.parser.dto.ParseResult;

public interface FileParser {
  ParseResult parse(InputStream input, String fileName, String fileType, Long fileSize) throws IOException;
}
