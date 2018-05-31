package org.smooth.systems.ec.prestashop17.util;

import java.util.Set;

import org.smooth.systems.ec.prestashop17.component.PrestashopLanguageTranslatorCache;
import org.smooth.systems.ec.prestashop17.model.Category;
import org.smooth.systems.ec.prestashop17.model.LanguageAttribute;
import org.smooth.systems.ec.prestashop17.model.PrestashopLangAttribute;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Prestashop17ClientUtil {

	public static Long DEFAULT_LANG_ID = 3L;

  public static String convertToUTF8(String s) {
		String out = null;
		try {
			out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
		} catch (java.io.UnsupportedEncodingException e) {
			return null;
		}
		return out;
	}

	public static void fillUpEmptyAttributesInCategory(PrestashopLanguageTranslatorCache langCache, Category category) {
		fillUpMissingLangAttribute("names", category.getNames(), langCache.getLangIds());
		fillUpMissingLangAttribute("descriptions", category.getDescriptions(), langCache.getLangIds());
		fillUpMissingLangAttribute("friendlyUrls", category.getFriendlyUrls(), langCache.getLangIds());
	}

	private static void fillUpMissingLangAttribute(String attributeName, PrestashopLangAttribute attributes,
			Set<Long> langIds) {
		if (attributes == null || attributes.getValues().isEmpty()) {
			log.warn("Attribute sets '{}' are null or empty.");
			return;
		}
		langIds.forEach(id -> {
			if (!isLangAttributeSet(id, attributes)) {
				String defaultValue = getAttributeValueForId(attributes, DEFAULT_LANG_ID);
				attributes.addAttribute(id, defaultValue);
			}
		});
	}

	private static String getAttributeValueForId(PrestashopLangAttribute attributes, Long langId) {
		LanguageAttribute foundAttr = attributes.getValues().stream().filter(attr -> attr.getId().equals(langId))
				.findFirst().orElse(null);
		if (foundAttr == null) {
			throw new IllegalStateException("Default attribute not found for id:" + langId);
		}
		return foundAttr.getValue();
	}

	private static boolean isLangAttributeSet(Long id, PrestashopLangAttribute attributes) {
		for (LanguageAttribute attr : attributes.getValues()) {
			if (attr.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}
}