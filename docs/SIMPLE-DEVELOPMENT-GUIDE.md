# Simple development guide

## EcommerceMigrationApplication.java

### Migrate categories and log conversion

```
java ecommerce-migration-tool-<version>.jar --merge-category --config=<path>/config/migration-config.yaml
```

Reads the mapping configuration from **categories-merging-file**:

```
  categories-merging-file: <path>/config/categories-merging.properties
```

and generates **categories-merging-file** 

```
  generated-created-categories-mapping-file: <path>/config/generated/generated-created-categories-mapping.properties
```

which maps:

```
  original-category-id -> new-created-category-id
```

## EcommerceUtilApplication.java

### Create products mapping to single product id

```
java ecommerce-utils-<version>.jar --products-mapping --config=<path>/config/migration-config.yaml
```

Reads all products from the root category and all additional categories, checks the additional products mapping:

```
  products-merging-file: <path>/config/products-merging.properties
```

ignores all products to be skipped from:

```
  product-ids-skipping:
    - 926
    - 3159
```

and generates a list of merged product ids to root product id:

```
  generated-products-merging-file: <path>/config/generated/generated-products-merging.properties
```

If not all products can be merged an error occurs, not merged products must be added to **products-ids-skipping**.