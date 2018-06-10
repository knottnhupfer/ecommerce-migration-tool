package org.smooth.systems.ec.prestashop17.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@XmlRootElement(name = "prestashop")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties({"content"})
public class ImageUploadResponse {

  @JsonProperty("image")
  private UploadedImage uploadedImage;

  @Data
  @JsonIgnoreProperties({"cover", "legend"})
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class UploadedImage {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("position")
    private Long position;

    @JsonProperty("id_product")
    private Long productId;
  }
}