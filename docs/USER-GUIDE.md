# User documentation

The E-Commerce migation tool has been created for migrating categories and products from any e-commerce system to another. Therefore you need to configure the source and destination.

You can start the application via:

```
java <JAR-FILE-NAME-TODO>.jar <action> <configuration-file>
```

##### Merging categories
For merging categories the required command looks like following:
```
java <JAR-FILE-NAME-TODO>.jar --merge-categories --config=<PATH-TO-CONFIGURATION>/migration-config.yaml
```

##### Merging products
For merging categories the required command looks like following:
```
java <JAR-FILE-NAME-TODO>.jar --merge-products --config=<PATH-TO-CONFIGURATION>/migration-config.yaml
```

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

### Migrating categories to new system

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

Let's assume we have on our source system categories trees for an english and italian categories root tree as following:

```
  # English categories
  1: English categories
    2: Men
      4: Pants
      5: Shoes
    3: Women
      6: Pants
      7: Shoes


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
  11=1
  12=2
  13=3
  14=4
  15=5
  16=6
  17=7
```

### Migrating products to new system
