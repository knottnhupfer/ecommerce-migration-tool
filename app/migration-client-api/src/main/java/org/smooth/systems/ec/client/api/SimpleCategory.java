package org.smooth.systems.ec.client.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCategory {

  @JsonProperty("category-id")
  private Long categoryId;

  @JsonProperty("category-language")
  private String categoryLanguage;
}