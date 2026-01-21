package com.na.postmortemproject.parser;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class JsonParser {
  ObjectMapper mapper = new ObjectMapper();
  JsonNode json = mapper.readTree(input);
  return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
}