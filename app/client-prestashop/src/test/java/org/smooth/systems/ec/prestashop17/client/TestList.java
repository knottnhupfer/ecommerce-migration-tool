package org.smooth.systems.ec.prestashop17.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import org.smooth.systems.ec.prestashop17.model.LanguageContainer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Data
@JsonRootName("product")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestList {

	@JsonProperty("name")
	private LanguageContainer names;

	@JsonProperty("link_rewrite")
	private LanguageContainer friendlyUrls;
}
