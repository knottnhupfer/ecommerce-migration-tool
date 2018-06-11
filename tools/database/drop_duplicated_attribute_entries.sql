
-- Only attributes for store_id = 0 will be considered
-- select value_id, entity_type_id, attribute_id, store_id, entity_id from catalog_product_entity_text WHERE store_id = 1;
-- delete from catalog_product_entity_text WHERE store_id = 1;





-- Remove mapping from product mapping 2969=2969, 2970=2970, 2971=2971, 3125=3125, 3316=3316, 1524=1524, 1525=1525, 1647=1647, 1648=1648, 1649=1649



select * from catalog_product_entity WHERE entity_id = 2971;
select * from catalog_category_product where product_id = 2971;
select * from catalog_category_entity where entity_id IN (2,3,11,17,127);



-- ######################################################################################
-- row to be removed
--

mysql> select * from catalog_category_entity where entity_id = 127;
+-----------+----------------+------------------+-----------+---------------------+---------------------+----------------+----------+-------+----------------+
| entity_id | entity_type_id | attribute_set_id | parent_id | created_at          | updated_at          | path           | position | level | children_count |
+-----------+----------------+------------------+-----------+---------------------+---------------------+----------------+----------+-------+----------------+
|       127 |              3 |                3 |        34 | 2014-02-25 12:42:57 | 2014-02-25 12:42:57 | 1/26/27/34/127 |        2 |     4 |              0 |
+-----------+----------------+------------------+-----------+---------------------+---------------------+----------------+----------+-------+----------------+
1 row in set (0,00 sec)


-- ######################################################################################
-- remove category which is linked to wrong language
--
mysql> delete FROM catalog_category_entity where entity_id = 127;
Query OK, 1 row affected (0,03 sec)