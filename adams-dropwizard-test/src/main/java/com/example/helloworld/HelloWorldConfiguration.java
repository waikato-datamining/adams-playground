package com.example.helloworld;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import jakarta.validation.constraints.NotEmpty;

public class HelloWorldConfiguration extends Configuration {
  @NotEmpty
  private String template;

  @NotEmpty
  private String defaultName = "Stranger";

//  public HelloWorldConfiguration() {
//    HttpConnectorFactory http = new HttpConnectorFactory();
//    http.setPort(8080);
//    if (getServerFactory() instanceof DefaultServerFactory) {
//      ((DefaultServerFactory) getServerFactory()).setApplicationConnectors(List.of(http));
//    }
//  }

  @JsonProperty
  public String getTemplate() {
    return template;
  }

  @JsonProperty
  public void setTemplate(String template) {
    this.template = template;
  }

  @JsonProperty
  public String getDefaultName() {
    return defaultName;
  }

  @JsonProperty
  public void setDefaultName(String name) {
    this.defaultName = name;
  }
}