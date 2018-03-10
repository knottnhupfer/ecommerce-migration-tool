package org.smooth.systems.ec.prestashop17.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name = "prestashop")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImageUploadResponse {

  @XmlElement(name = "image")
  private UploadedImage uploadedImage;

  @Data
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class UploadedImage {
    
    @XmlElement(name = "id")
    private Long id;
    
    @XmlElement(name = "position")
    private Long position;
    
    @XmlElement(name = "id_product")
    private Long productId;
  }
}