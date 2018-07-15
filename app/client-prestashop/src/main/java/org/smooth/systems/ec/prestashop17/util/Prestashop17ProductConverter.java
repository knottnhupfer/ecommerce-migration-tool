package org.smooth.systems.ec.prestashop17.util;

import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.IProductMetaData;
import org.springframework.util.Assert;

public final class Prestashop17ProductConverter {

	private Prestashop17ProductConverter() {
	}

	public static org.smooth.systems.ec.prestashop17.model.Product convertToPrestashop17Product(Product product) {
		org.smooth.systems.ec.prestashop17.model.Product prod = new org.smooth.systems.ec.prestashop17.model.Product();
		product.getCategories();
		product.getAttributes(); // multiple attributes
		product.getSku();
		product.getBrandId();
		product.getCostPrice();
		product.getDimension();
		product.getNetPrice();
		product.getVisibility();
		product.getCreationDate();
		product.getRelatedProducts();
		product.getType();
		throw new NotImplementedException();
	}

	public static IProductMetaData convertFromPrestashop17Product(org.smooth.systems.ec.prestashop17.model.Product prod) {
		return new ProductMetaData(prod);
	}

	private static class ProductMetaData implements IProductMetaData {

		private final org.smooth.systems.ec.prestashop17.model.Product prod;

		private ProductMetaData(org.smooth.systems.ec.prestashop17.model.Product prod) {
			this.prod = prod;
		}

		@Override
		public String getSku() {
			return prod.getReference();
		}

		@Override
		public Long getProductId() {
			return prod.getId();
		}

		@Override
		public Long getCategoryId() {
			Assert.notEmpty(prod.getAssociations().getCategories(), "no category set on retrieved product with id: " + prod.getId());
			return prod.getAssociations().getCategories().get(0).getCategoryId();
		}
	}
}
