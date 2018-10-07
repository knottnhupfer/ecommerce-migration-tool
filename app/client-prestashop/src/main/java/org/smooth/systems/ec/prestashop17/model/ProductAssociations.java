package org.smooth.systems.ec.prestashop17.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties({ "combinations", "product_option_values", "product_features", "product_bundle"

	// ignored properties when writing
	, "stock_availables"
})
public class ProductAssociations {

  @JacksonXmlElementWrapper(localName = "categories")
  private List<ProductCategory> categories = new ArrayList<>();

	@JacksonXmlElementWrapper(localName = "tags")
  private List<ProductTag> tags = new ArrayList<>();

	@JacksonXmlElementWrapper(localName = "images")
	private List<Id> images = new ArrayList<>();

	@JacksonXmlElementWrapper(localName = "accessories")
	private List<Id> accessories = new ArrayList<>();
}
