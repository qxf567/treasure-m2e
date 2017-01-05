package com.github.treasure.m2e.plugins;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.Streams;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.yaml.YamlXContent;
import org.elasticsearch.env.Environment;

public class ExamplePluginConfiguration
{
  private String test = "not set in config";
  
  @Inject
  public ExamplePluginConfiguration(Environment env)
    throws IOException
  {
    Path configFile = env.configFile().resolve("jvm-example/example.yaml");
    String contents = Streams.copyToString(Files.newBufferedReader(configFile, StandardCharsets.UTF_8));
    XContentParser parser = YamlXContent.yamlXContent.createParser(contents);
    
    String currentFieldName = null;
    XContentParser.Token token = parser.nextToken();
    if ((token != XContentParser.Token.START_OBJECT)) {
      throw new AssertionError();
    }
    while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
      if (token == XContentParser.Token.FIELD_NAME) {
        currentFieldName = parser.currentName();
      } else if (!token.isValue()) {
        throw new ElasticsearchParseException("Unrecognized config key: {}", new Object[] { currentFieldName });
      }
    }
  }
  
  public String getTestConfig()
  {
    return this.test;
  }
}