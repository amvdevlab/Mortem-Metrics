package com.na.postmortemproject.parser;

import java.io.IOException;
import java.io.InputStream;
public interface FileParser {
  String extractText(InputStream input) throws IOException;
}
