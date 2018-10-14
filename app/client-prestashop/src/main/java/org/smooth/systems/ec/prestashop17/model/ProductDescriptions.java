package org.smooth.systems.ec.prestashop17.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDescriptions {

	@JsonProperty("description")
	private LanguageContainer descriptions;

	@JsonProperty("description_short")
	private LanguageContainer shortDescriptions;
}