package org.smooth.systems.ec.prestashop17.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@Data
@JsonRootName("product")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestList {

//	@JacksonXmlProperty(localName = "language")
	@JacksonXmlElementWrapper(localName = "link_rewrite")
	private List<LanguageAttribute> friendlyUrls;
}
