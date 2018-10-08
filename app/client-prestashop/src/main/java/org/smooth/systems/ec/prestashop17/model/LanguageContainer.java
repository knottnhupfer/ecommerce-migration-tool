package org.smooth.systems.ec.prestashop17.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class LanguageContainer {

	public LanguageContainer() {
		languages = new ArrayList<>();
	}

	@JsonProperty("language")
	@JacksonXmlElementWrapper(useWrapping = false)
	List<LanguageAttribute> languages;
}
