package org.smooth.systems.ec.dummy.component;

import lombok.extern.slf4j.Slf4j;
import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.dummy.DummyConstants;
import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.exceptions.ObjectAlreadyExistsException;
import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.Manufacturer;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.User;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Slf4j
@Component
public class DummyMigrationSystemWriter implements MigrationSystemWriter {

	@Override
	public void writeUsers(List<User> customers) throws ObjectAlreadyExistsException {
		throw new NotImplementedException();
	}

	@Override
	public void writeUsers(List<User> customers, boolean updateIfExists) throws ObjectAlreadyExistsException {
		throw new NotImplementedException();
	}

	@Override
	public void writeCategories(Category rootCategory) throws ObjectAlreadyExistsException {
		throw new NotImplementedException();
	}

	@Override
	public void writeCategories(Category rootCategory, boolean updateIfExists) throws ObjectAlreadyExistsException {
		throw new NotImplementedException();
	}

	@Override
	public void repairAndValidateData() {
		throw new NotImplementedException();
	}

	@Override
	public ObjectIdMapper getCategoriesObjectIdMapper() {
		throw new NotImplementedException();
	}

	@Override
	public ObjectIdMapper getProductsObjectIdMapper() {
		throw new NotImplementedException();
	}

	@Override
	public Product writeProduct(Product product) {
		log.info("writeProduct({})", product);
		product.setId(product.getId() % 100 + 5000);
		return product;
	}

	@Override
	public void uploadProductImages(Long prodId, File productImage) {
		log.info("Write image {} for product with id {}", productImage, prodId);
	}

	@Override
	public Manufacturer writeManufacturer(String manufacturerName) {
	  log.info("writeManufacturers({})", manufacturerName);
	  throw new NotImplementedException();
	}

	@Override
	public String getName() {
		return DummyConstants.DUMMY_PROFILE;
	}
}