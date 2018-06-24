# User documentation

## Generic application description

The E-Commerce migation tool has been created for migrating categories and products from any e-commerce system to another. Therefore you need to configure the source and destination. The first implementation has been implemented to migrate two magento 1.9 stores, same store in different languages, to a single prestashop 17 store.

This application not only does a migration from a source system to a destination system but further can migrate multiple stores in different languages to a single store with multiple languages.

### Steps where this application runs trough

For a successful migration the user needs to process following steps:
1. define source and destination system
2. define the source system main category
  * [OPTIONAL] define alternative categories which will be merged to main categories
  * create a mapping file for mapping alternative catgories to main categories
3. migration of categories from source system to destination system
  * [OPTIONAL] if alternative categories are defined, categories in different languages on the source system will be merged beforehand to a single multi language category
4. [OPTIONAL] if alternative categories are defined, a products mapping file must be generated beforehand
5. [CURRENTLY] create manufacturers on destination system and also the mapping file therefore
6. migration of products from source system to destination system
  * [OPTIONAL] if alternative categories are defined, products in different languages on the source system will be merged beforehand to a single multi language product
7. migrate product images from main categories products of the source system to destination system products
8. migrate product tier prices from main categories products of the source system to destination system products

Legend:

Keyword  | Description
:-------------:|:-----
OPTIONAL  | if the feature is enabled this step will also be executed
CURRENTLY | there are already issues defined in the github bug-tracker to remove this step


### Applying application

Following commands need to be executed to migrate data from A to B in root folder:
```
  # build application
  ./scripts/build_migration_tool.sh

  # merge and migrate categories
  ./scripts/migrate_data.sh migrate-categories

  # generate manufacturers on destination system and write mapping file for exactly this store
  ./scripts/migration-utils.sh create-led-brands

  # merge products
  ./scripts/migration-utils.sh generate-merged-product-ids
  # migrate products
  ./scripts/migration-utils.sh migrate-products
  # upload product images
  ./scripts/migration-utils.sh migrate-product-images
  # upload product tier prices
  ./scripts/migration-utils.sh migrate-product-tier-prices
```