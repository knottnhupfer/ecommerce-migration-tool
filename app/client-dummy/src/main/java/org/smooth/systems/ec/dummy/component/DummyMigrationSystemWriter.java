package org.smooth.systems.ec.dummy.component;

import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.dummy.DummyConstants;
import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.exceptions.ObjectAlreadyExistsException;
import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

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
	public String getName() {
		return DummyConstants.DUMMY_PROFILE;
	}
}