package com.projects.order_create.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.IOException;

public class YamlUtil {
  private static final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

  public static <T> T getObjectWithKebabCase(String jsonString, Class<T> clazz) throws IOException {
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
    JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
    String jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNodeTree);
    return objectMapper.readValue(jsonAsYaml, clazz);
  }
}
