package org.smooth.systems.ec.magento19.db.component;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.magento19.db.model.Magento19Attributes;
import org.smooth.systems.ec.magento19.db.model.Magento19Category;
import org.smooth.systems.ec.magento19.db.model.Magento19CategoryText;
import org.smooth.systems.ec.magento19.db.model.Magento19CategoryVarchar;
import org.smooth.systems.ec.magento19.db.repository.CategoryTextRepository;
import org.smooth.systems.ec.magento19.db.repository.CategoryVarcharRepository;
import org.smooth.systems.ec.migration.model.AbstractCategoryRecursiveProcessor;
import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.CategoryTranslateableAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 11.02.18.
 */
@Slf4j
@Component
public class Magento19DbCategoriesPopulater extends AbstractCategoryRecursiveProcessor<Category> {

	public static String DELIMITER = ",";

	@Autowired
	private CategoryTextRepository categoryTextRepo;

	@Autowired
	private CategoryVarcharRepository categoryVarcharRepo;

	private String languageCode = "undefined";

	@Override
	protected void executeTreeNode(Category category, int level) {
		log.info("executeTreeNode({}, {})", category, level);

		CategoryTranslateableAttributes attributes = new CategoryTranslateableAttributes(languageCode);

		List<Magento19CategoryText> categoryText = categoryTextRepo.findByEntityId(category.getId());
		attributes.setDescription(getStringByAttributeId(categoryText, Magento19CategoryText.DESCRIPTION_ATTR_ID));
		attributes.setTags(getStringListByAttributeId(categoryText, Magento19CategoryText.META_TAGS_ATTR_ID));
		attributes.setMetaTitle(getStringByAttributeId(categoryText, Magento19CategoryText.META_DESCRIPTION_ATTR_ID));

		List<Magento19CategoryVarchar> categoryVarchar = categoryVarcharRepo.findByEntityId(category.getId());
		attributes.setName(getStringByAttributeId(categoryVarchar, Magento19CategoryVarchar.NAME_ATTR_ID));
		attributes.setShortDescription(getStringByAttributeId(categoryVarchar, Magento19CategoryVarchar.SUB_TITLE_ATTR_ID));
		attributes.setFriendlyUrl(getStringByAttributeId(categoryVarchar, Magento19CategoryVarchar.FRIENDLY_URL_ATTR_ID));
		category.getAttributes().add(attributes);
	}

	private String getStringByAttributeId(List<? extends Magento19Attributes> attributes, Long attributeId) {
		Optional<? extends Magento19Attributes> result = attributes.stream().filter(attr -> attr.getAttributeId().equals(attributeId) && attr.getStoreId().equals(0L)).findAny();
		if(result.isPresent()) {
			return result.get().getValue();
		}
		return "";
	}

	private List<String> getStringListByAttributeId(List<? extends Magento19Attributes> attributes, Long attributeId) {
		String attributeValue = getStringByAttributeId(attributes, attributeId);
		if(attributeValue == null) {
			return Collections.emptyList();
		}
		String[] tokens = attributeValue.split(DELIMITER);
		return Arrays.stream(tokens).map(token -> token.trim()).collect(Collectors.toList());
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
}