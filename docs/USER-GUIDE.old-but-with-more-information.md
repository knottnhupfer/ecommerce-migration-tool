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
  ./scripts/migrate_data.sh merge-categories

  # generate manufacturers on destination system and write mapping file
  ./scripts/migration-utils.sh create-led-brands

  # merge products
  ./scripts/migration-utils.sh generate-merged-product-ids
  # migrate products
  ./scripts/migration-utils.sh migrate-products
  # upload product images
  ./scripts/migration-utils.sh upload-images
  # upload product tier prices
  ./scripts/migration-utils.sh migrate-product-tier-prices
```


## Usage

You can start the application via:

```
java ecommerce-utils-1.0.0-SNAPSHOT.jar <action> --config=configuration-file>
```

### Merging categories
For merging categories the required command looks like following:

```
java ecommerce-utils-1.0.0-SNAPSHOT.jar --merge-categories \
              --config=<PATH-TO-CONFIGURATION>/migration-config.yaml
```

### Merging products
For merging products from the source system to the destination system first of all all products needs to be merged. This currently assumes that the source system has 2 languages which will be merged into a single language with multilanguage attributes (TODO see chapter internal data model development description).


#### Prepare products merging on source system

---
**TODO**

describe how the mapping will be generated ...

---

Generates a mapping list from the alternative language product id to the main product id.
```
java ecommerce-utils-1.0.0-SNAPSHOT.jar --products-mapping \
              --config=<PATH-TO-CONFIGURATION>/migration-config.yaml
```

As result a file configured as variable `generated-products-merging-file`, will be written which contains the mapping. This file contains the mapping as following:
```
alternative_language_product_id = main_product_id
```

#### Merge and upload products to destination system
Following pre-prepared files must be done before calling this command:
+ `generated-created-categories-mapping-file` containing the mapping from source category id to destination category id
+ `generated-products-merging-file` containing the product mapping for merging
```
java ecommerce-utils-1.0.0-SNAPSHOT.jar --products-migrate \
              --config=<PATH-TO-CONFIGURATION>/migration-config.yaml
```
Following steps will be processed when merging and uploading the products:
+ Merges the products from the source system, using the `alternative_language_product_id` and merging the tanslatable attributes into the main product.
+ Replace the category id from the source system with the newly generated category id created on the destination system.
+ Uploading teh products to the destinatin system
+ Storing the mapping product id from the destination system

As result a file defined in `generated-products-migration-file` will be written which contains the mapping from `main_category_id_source_system` to created `main_category_id_destination_system`.

---
**Important**

+ Brand id must already be mapped.
+ Currently only products are mapped where both languages exists.

---

#### Upload product images to destination system

Only the images from the main system will be uploaded to the destination system. Images from alternative languages will be skipped.

Preparation steps:
+ images must be download to local file system and path configured as variable `products-images-directory`

```
java ecommerce-utils-1.0.0-SNAPSHOT.jar --products-image-migrate \
              --config=<PATH-TO-CONFIGURATION>/migration-config.yaml
```

Following steps will be processed when uploading images:
+ Reads destination products from main products in `generated-products-merging-file`
+ Reads the image paths from the source system
+ Maps the created product_id_source_system to product_id_destination_system
+ uploads the images from the local file system to the destination system

## Prepare connectors configuration

The connection details will be configured in the **application.properties** due to the underlying spring boot technology. Currently a very simple implementation has been done which is able to read from **magento2** and write to **prestashop17**. Additional read and write connectors can be added easily.

```
  migration.magento2.base-url=http://data-migration-magento.local/index.php
  migration.magento2.user=admin
  migration.magento2.password=<password>

  migration.prestashop17.base-url=http://prestashop.local/api
  migration.prestashop17.auth-token=WUIHXDKPX2WNUUBLAG6BLZ8FIAX6PM6P
  migration.prestashop17.base-category-id=1
```

## Migration configuration

### Migrating and merging categories to new system

```
java <JAR-FILE-NAME-TODO>.jar --merge-categories \
              --config=<PATH-TO-CONFIGURATION>/migration-config.yaml
```

The main configuration file **migration-config.yaml** looks like following:

```
  # migration configuration file: migration-config.yaml
  ---
  source-system-name: magento2
  destination-system-name: prestashop17
  root-category-id: 1
  root-category-language: en
  additional-categories:
    -
      category-id: 11
      category-language: de
  categories-mapping-file: /absolut/path/config/category-mapping.properties
  category-ids-merging-ignore-list:
    - 53
    - 127
    - 128
    - 126
    - 78
  category-ids-skipping:
  #  - 50
```

// TODO: add additional description for configuration file

> If the migration tool is not able to map a category the migration process will be aborted and the failing categories will be printed out.


#### Categories merging configuration file: category-mapping.properties

Let's assume we have on our source system category trees for an italian and german categories root tree as following:

```
  # Italian categories
  1: Italian categories
    2: Uomini
      4: Pantaloni
      5: Scarpe
    3: Donne
      6: Pantaloni
      7: Scarpe

  # German categories
  11: German categories
    12: Männer
      14: Hosen
      15: Schuhe
    13: Frauen
      16: Hosen
      17: Schuhe
```

For merging category root trees of different languages to a single multi-language category tree in the destination system the file **category-mapping.properties** will be used.

The file follows the following syntax:
```
sourceCategoryId=destinationCategoryId
```

For the example above to merge german categories to english categories the mapping file looks as following:
```
  # category merging ids mapping
  # secondaryCategoryId=mainCategoryId
  11=1
  12=2
  13=3
  14=4
  15=5
  16=6
  17=7
```

### Map products to main multilanguage product

To generate a mapping file for merge products from secondary languages to the main languages this command needs to be called. Currently we assume that the secondary languages are represanted with the same sku as the main product only with a "_" as prefix. Additional mapper needs to be added if in your case the mapping is differently (TODO: add documentation in developer guide and link it here).

```
java <JAR-FILE-NAME-TODO>.jar --products-mapping --config=<PATH-TO-CONFIGURATION>/migration-config.yaml
```

As a result the file configured in **migration-config.yaml** with parameter-name **generated-products-merging-file** will be generated. To support manual mapping beforehand users can define premapped product ids using the parameter **products-merging-file**.

```
  # migration configuration file: migration-config.yaml
  ---
  source-system-name: magento2
  destination-system-name: prestashop17
  root-category-id: 1
  root-category-language: en
  additional-categories:
    -
      category-id: 11
      category-language: de
  products-merging-file: <path/to/file>/config/products-merging.properties
  generated-products-merging-file: <path/to/file>/config/generated/generated-products-merging.properties
```

This files can also be generated by external tools or manually and will be used in the next step 

### Migrating products to new system

Using the productId-mapping from products-mappin

```
java <JAR-FILE-NAME-TODO>.jar --merge-products --config=<PATH-TO-CONFIGURATION>/migration-config.yaml
```