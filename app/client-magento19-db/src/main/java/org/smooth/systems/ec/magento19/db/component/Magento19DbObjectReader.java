package org.smooth.systems.ec.magento19.db.component;

import org.smooth.systems.ec.client.api.CategoryConfig;
import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.exceptions.NotImplementedException;
import org.smooth.systems.ec.magento19.db.Magento19Constants;
import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.Product;
import org.smooth.systems.ec.migration.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
@Component
@Profile(Magento19Constants.MAGENTO19_DB_PROFILE_NAME)
public class Magento19DbObjectReader implements MigrationSystemReader {
	@Override
	public String getName() {
		return Magento19Constants.MAGENTO19_DB_PROFILE_NAME;
	}

	@Override
	public List<User> readAllUsers() {
		throw new NotImplementedException();
	}

	@Override
	public List<User> readWebUsers() {
		throw new NotImplementedException();
	}

	@Override
	public List<User> readAdministrativeUsers() {
		throw new NotImplementedException();
	}

	@Override
	public List<Category> readAllCategories(List<CategoryConfig> categories) {
		throw new NotImplementedException();
	}

	@Override
	public List<Product> readProductsOfCategory(Long categoryId, boolean searchSubcategories) {
		throw new NotImplementedException();
	}
}
