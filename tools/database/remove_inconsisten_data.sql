-- an product was added to german and italian categories and the german id matches which of course is not migrated
-- analyse error with following
select * from catalog_category_entity WHERE entity_id = 127;
SELECT * FROM catalog_category_entity_text WHERE entity_id = 127; -- shows that category is german once


select * from catalog_product_entity WHERE entity_id = 2971;
select * from catalog_category_product where product_id = 2971;
select * from catalog_category_entity where entity_id IN (2,3,11,17,127);





mysql> select * from catalog_product_entity WHERE entity_id = 2971;
+-----------+----------------+------------------+---------+-------+---------------------+---------------------+-------------+------------------+
| entity_id | entity_type_id | attribute_set_id | type_id | sku   | created_at          | updated_at          | has_options | required_options |
+-----------+----------------+------------------+---------+-------+---------------------+---------------------+-------------+------------------+
|      2971 |              4 |                4 | simple  | L1308 | 2013-09-18 09:48:15 | 2015-08-07 09:37:32 |           0 |                0 |
+-----------+----------------+------------------+---------+-------+---------------------+---------------------+-------------+------------------+
1 row in set (0.00 sec)

mysql> select * from catalog_category_product where product_id = 2971;
+-------------+------------+----------+
| category_id | product_id | position |
+-------------+------------+----------+
|           2 |       2971 |        1 |
|           3 |       2971 |       99 |
|          11 |       2971 |      108 |
|          17 |       2971 |       69 |
|         127 |       2971 |        3 |
+-------------+------------+----------+
5 rows in set (0.00 sec)

mysql> select * from catalog_product_entity WHERE entity_id = 3316;
+-----------+----------------+------------------+---------+----------+---------------------+---------------------+-------------+------------------+
| entity_id | entity_type_id | attribute_set_id | type_id | sku      | created_at          | updated_at          | has_options | required_options |
+-----------+----------------+------------------+---------+----------+---------------------+---------------------+-------------+------------------+
|      3316 |              4 |                4 | simple  | L690S3MX | 2015-07-13 10:00:32 | 2016-06-28 17:55:31 |           0 |                0 |
+-----------+----------------+------------------+---------+----------+---------------------+---------------------+-------------+------------------+
1 row in set (0.00 sec)

mysql> select * from catalog_category_product where product_id = 3316;
+-------------+------------+----------+
| category_id | product_id | position |
+-------------+------------+----------+
|           2 |       3316 |        1 |
|           7 |       3316 |        1 |
|          89 |       3316 |       66 |
|         110 |       3316 |        0 |
+-------------+------------+----------+
4 rows in set (0.00 sec)

delete FROM catalog_category_product WHERE category_id = 127 AND product_id = 1524;
delete FROM catalog_category_product WHERE category_id = 127 AND product_id = 1525;
delete FROM catalog_category_product WHERE category_id = 127 AND product_id = 2969;
delete FROM catalog_category_product WHERE category_id = 127 AND product_id = 2970;
delete FROM catalog_category_product WHERE category_id = 127 AND product_id = 2971;
delete FROM catalog_category_product WHERE category_id = 110 AND product_id = 3125;
delete FROM catalog_category_product WHERE category_id = 127 AND product_id = 3316;