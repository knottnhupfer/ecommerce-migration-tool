package org.smooth.systems.ec.client.api;

import java.util.List;

import org.smooth.systems.ec.client.util.ObjectIdMapper;
import org.smooth.systems.ec.exceptions.ObjectAlreadyExistsException;
import org.smooth.systems.ec.migration.model.Category;
import org.smooth.systems.ec.migration.model.User;

public interface MigrationSystemWriter extends RegisterableComponent {

  /**
   * Write customers to system, if at least a user exists writing the users will
   * fail. Existing users are compared by the email address since the id can
   * change over various systems.
   * 
   * @param customers
   *          customers to be created at the destination system
   */
  public void writeUsers(List<User> customers) throws ObjectAlreadyExistsException;

  /**
   * Write users to system, if at least a user exists writing the users will
   * fail if <code>updateIfExists</code> is false. Existing users are compared
   * by the email address since the id can change over various systems.
   * 
   * @param customers
   *          customers to be created at the destination system
   * @param updateIfExists
   *          if true customers will be added even if customers already exists,
   *          existing users will be updated. If false method call will fail if
   *          non administrative users exists. Administrative users will never
   *          be overwritten.
   */
  public void writeUsers(List<User> customers, boolean updateIfExists) throws ObjectAlreadyExistsException;

  /**
   * Write categories to system, if the customer already exists writing the
   * customers will fail. Existing elements are checked by the name of the
   * element since the id can change over various systems.
   * 
   * @param categories
   *          categories to be created at the destination system
   */
  public void writeCategories(Category rootCategory) throws ObjectAlreadyExistsException;

  /**
   * Write customers to system, if the customer already exists writing the
   * customers will fail if <code>updateIfExists</code> is false. Existing
   * categories are compared by the category name and parent category name since
   * the id can change over various systems.
   * 
   * @param categories
   *          categories to be created at the destination system
   * @param updateIfExists
   *          if true categories will be added even if categories already
   *          exists, existing categories will be updated. If false method call
   *          will fail if categories already exists. Administrative users will
   *          never be overwritten.
   */
  public void writeCategories(Category rootCategory, boolean updateIfExists) throws ObjectAlreadyExistsException;

  public void repairAndValidateData();

  public ObjectIdMapper getCategoriesObjectIdMapper();

  public ObjectIdMapper getProductsObjectIdMapper();
}