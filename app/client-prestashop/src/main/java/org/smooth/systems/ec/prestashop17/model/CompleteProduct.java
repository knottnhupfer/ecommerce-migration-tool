package org.smooth.systems.ec.prestashop17.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties({ "delivery_in_stock", "delivery_out_stock", "meta_description",
												"meta_keywords", "meta_title", "available_now",  "available_later"

		// properties not writeable
		, "quantity", "manufacturer_name"
})
@XmlRootElement
public class CompleteProduct extends Product {

	@JsonProperty("id_supplier")
	private Long supplierId = 0L;

	@JsonProperty("new")
	private String newStatus;

	@JsonProperty("cache_default_attribute")
	private String cacheDefaultAttribute;

	@JsonProperty("id_default_image")
	private String defaultImageId;

	@JsonProperty("id_default_combination")
	private String defaultCombinationId;

	@JsonProperty("position_in_category")
	private String positionInCategory;

	@JsonProperty("type")
	private String type;

	@JsonProperty("id_shop_default")
	private String shopDefaultId;

	@JsonProperty("supplier_reference")
	private String supplierReference;

	@JsonProperty("location")
	private String location;

	@JsonProperty("width")
	private String width;

	@JsonProperty("height")
	private String height;

	@JsonProperty("depth")
	private String depth;

	@JsonProperty("quantity_discount")
	private String quantityDiscount;

	@JsonProperty("ean13")
	private String ean13;

	@JsonProperty("isbn")
	private String isbn;

	@JsonProperty("upc")
	private String upc;

	@JsonProperty("cache_is_pack")
	private String cacheIsPack;

	@JsonProperty("cache_has_attachments")
	private String cacheHasAttachments;

	@JsonProperty("is_virtual")
	private String isVirtual;

	@JsonProperty("on_sale")
	private String onSale;

	@JsonProperty("online_only")
	private String onlineOnly;

	@JsonProperty("ecotax")
	private String ecotax;

	@JsonProperty("low_stock_threshold")
	private String lowStockThreshold;

	@JsonProperty("wholesale_price")
	private String wholesalePrice;

	@JsonProperty("unity")
	private String unity;

	@JsonProperty("unit_price_ratio")
	private String unitPriceRatio;

	@JsonProperty("additional_shipping_cost")
	private String additionalShippingCost;

	@JsonProperty("customizable")
	private String customizable;

	@JsonProperty("text_fields")
	private String textFields;

	@JsonProperty("uploadable_files")
	private String uploadableFiles;

	@JsonProperty("id_type_redirected")
	private String typeRedirectedId;

	@JsonProperty("available_date")
	private String availableDate;

	@JsonProperty("show_condition")
	private String showCondition;

	@JsonProperty("condition")
	private String condition;

	@JsonProperty("advanced_stock_management")
	private String advancedStockManagement;

	@JsonProperty("date_add")
	private String dateAdd;

	@JsonProperty("date_upd")
	private String dateUpd;

	@JsonProperty("pack_stock_type")
	private String packStockType;
}