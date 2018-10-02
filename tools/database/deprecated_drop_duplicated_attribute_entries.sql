
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
mysql> 
delete FROM catalog_category_product WHERE category_id = 127 AND product_id = 2971;

delete FROM catalog_category_entity where entity_id = 127;
Query OK, 1 row affected (0,03 sec)


mariadb -h magento19-db -u root -p illuminazione

-- check product categories
select * from catalog_category_product where product_id = 3316;

-- remove concrete product category
delete FROM catalog_category_product WHERE category_id = 110 AND product_id = 3316;



-- Remove invalid short description containing youtube video
select * from catalog_product_entity WHERE catalog_product_entity.sku = 'L6BCU0';
select * from catalog_product_entity_text WHERE entity_id = 2394;
UPDATE catalog_product_entity_text SET value = '<p>Profilo per cartongesso Versione Cove Lighting U, 2m</p>' WHERE value_id = 19208 AND entity_id = 2394;
select * from catalog_product_entity WHERE catalog_product_entity.sku = '_L6BCU0';
select * from catalog_product_entity_text WHERE entity_id = 2670;
UPDATE catalog_product_entity_text SET value = '<p>Cove Lighting LED Profil U f&uuml;r Gipskarton</p>' WHERE value_id = 21687 AND entity_id = 2670;
select * from catalog_product_entity WHERE catalog_product_entity.sku = '_L6BCU0R';
select * from catalog_product_entity_text WHERE entity_id = 2672;
UPDATE catalog_product_entity_text SET value = '<p>Rundes Cove Lighting LED Profil in U-Form f&uuml;r Gipskarton</p>' WHERE value_id = 21707 AND entity_id = 2672;

SELECT * FROM catalog_product_entity_text WHERE value LIKE '%youtube%';
UPDATE catalog_product_entity_text SET value = '<p>Faro LED da incasso Downlight Cabinet S bianco caldo</p>' WHERE value_id = 11587 AND entity_id = 1670;
UPDATE catalog_product_entity_text SET value = '<p>Faro LED da incasso Downlight 8 Watt bianco caldo</p>' WHERE value_id = 11611 AND entity_id = 1673;
UPDATE catalog_product_entity_text SET value = '<p>Glass Touch PWM Dimmer RGB 3 canali</p>' WHERE value_id = 19591 AND entity_id = 2440;
UPDATE catalog_product_entity_text SET value = '<p>Faro LED da incasso Downlight Sirius 195-3000 39W bianco caldo con anello di copertura bianco</p>' WHERE value_id = 20104 AND entity_id = 2504;
UPDATE catalog_product_entity_text SET value = '<p>Faro LED da incasso Downlight Sirius 195-3000 39W bianco caldo con anello di copertura grigio</p>' WHERE value_id = 20112 AND entity_id = 2505;
UPDATE catalog_product_entity_text SET value = '<p>Faro LED da incasso Downlight Sirius 195-3000 39W bianco caldo con anello di copertura nero</p>' WHERE value_id = 20120 AND entity_id = 2506;
UPDATE catalog_product_entity_text SET value = '<p>Mini Led Dimmer PWM analogico a Pulsante 1 canale / un taster funzionamento 12VDC o 24VDC DLC1248</p>' WHERE value_id = 20287 AND entity_id = 2527;
UPDATE catalog_product_entity_text SET value = '<p>Controller RGB per strisce LED con telecomando touch RF Color 12/24VDC max. 15A</p>' WHERE value_id = 20313 AND entity_id = 2529;
UPDATE catalog_product_entity_text SET value = '<p>LED Cove Lighting Profil in L-Form f&uuml;r Gipskarton</p>' WHERE value_id = 21719 AND entity_id = 2674;
UPDATE catalog_product_entity_text SET value = '<p>Controller RGBW per strisce LED con telecomando touch RF Color/White 12/24VDC max. 15A</p>' WHERE value_id = 29161 AND entity_id = 3296;
UPDATE catalog_product_entity_text SET value = '<p>Amplificatore di potenza per LED in tensione DLA1248 1CV</p>' WHERE value_id = 29237 AND entity_id = 3302;
UPDATE catalog_product_entity_text SET value = '<p>Lampada da tavolo LED pendente custom RGBW</p>' WHERE value_id = 30306 AND entity_id = 3333;



+-------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| sku                     | value                                                                                                                                                                                                                               |
+-------------------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+

<div><iframe width="100%" height="100%" src="https://www.youtube.com/embed/XA72-mzHkAY" frameborder="0"></iframe></div>


| L710086  | <iframe src="http://www.youtube.com/embed/mQVCFgF-fr8" height="100%" width="100%" frameborder="0"></iframe>
| L710396  | <iframe src="http://www.youtube.com/embed/QdELYQ_Cxz4" height="100%" width="100%" frameborder="0"></iframe>
<div><iframe width="100%" height="250px" src="https://www.youtube.com/embed/QdELYQ_Cxz4" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></div>
| L6BCU0   | <iframe src="http://www.youtube.com/embed/krcNjC4cYgU" height="100%" width="100%" frameborder="0"></iframe>
<div><iframe width="100%" height="250px" src="https://www.youtube.com/embed/krcNjC4cYgU" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></div>
| _L6BCU0  | <iframe src="http://www.youtube.com/embed/krcNjC4cYgU" height="100%" width="100%" frameborder="0"></iframe>
<div><iframe width="100%" height="250px" src="https://www.youtube.com/embed/krcNjC4cYgU" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></div>
| _L6BCU0R | <iframe src="http://www.youtube.com/embed/krcNjC4cYgU" height="100%" width="100%" frameborder="0"></iframe>
<div><iframe width="100%" height="250px" src="https://www.youtube.com/embed/krcNjC4cYgU" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></div>
| L51313   | <iframe src="http://www.youtube.com/embed/WFeoqkZVUng" height="100%" width="100%" frameborder="0"></iframe>
<div><iframe width="100%" height="250px" src="https://www.youtube.com/embed/WFeoqkZVUng" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></div>
| L71B346W | <iframe src="http://www.youtube.com/embed/AcQV82qccfk" height="100%" width="100%" frameborder="0"></iframe>
<div><iframe width="100%" height="250px" src="https://www.youtube.com/embed/AcQV82qccfk" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></div>
| L71B346S | <iframe src="http://www.youtube.com/embed/AcQV82qccfk" height="100%" width="100%" frameborder="0"></iframe>
<div><iframe width="100%" height="250px" src="https://www.youtube.com/embed/AcQV82qccfk" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></div>
| L71B346B | <iframe src="http://www.youtube.com/embed/AcQV82qccfk" height="100%" width="100%" frameborder="0"></iframe>
<div><iframe width="100%" height="250px" src="https://www.youtube.com/embed/AcQV82qccfk" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></div>
| L50002B1 | <iframe src="http://www.youtube.com/embed/D2tjszcVYU8" height="100%" width="100%" frameborder="0"></iframe>
<div><iframe width="100%" height="250px" src="https://www.youtube.com/embed/D2tjszcVYU8" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></div>
| L6BCL0   | <iframe src="http://www.youtube.com/embed/a_z29v8ioc4" height="100%" width="100%" frameborder="0"></iframe>
| _L6BCL0  | <iframe src="http://www.youtube.com/embed/a_z29v8ioc4" height="100%" width="100%" frameborder="0"></iframe>
<div><iframe width="100%" height="250px" src="https://www.youtube.com/embed/a_z29v8ioc4" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></div>



# not found
| K843140  | <iframe src="http://www.youtube.com/embed/sLdFdaMFkoA" height="100%" width="100%" frameborder="0"></iframe>
<div><iframe width="100%" height="250px" src="https://www.youtube.com/embed/sLdFdaMFkoA" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></div>
| lampada pendente custom | <iframe src="http://www.youtube.com/embed/x0zLEJF39jQ" height="100%" width="100%" frameborder="0"></iframe>
| K843141  | <iframe src="http://www.youtube.com/embed/sLdFdaMFkoA" height="100%" width="100%" frameborder="0"></iframe>
<div><iframe width="100%" height="250px" src="https://www.youtube.com/embed/sLdFdaMFkoA" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></div>
| DLA12481 | <iframe src="http://www.youtube.com/embed/YbbUPLcSvHM" height="100%" width="100%" frameborder="0"></iframe>
<div><iframe width="100%" height="250px" src="https://www.youtube.com/embed/YbbUPLcSvHM" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></div>


-- invalid name
select * from catalog_product_entity_varchar WHERE attribute_id = 56 AND value LIKE '%>%';

select * from catalog_product_entity WHERE catalog_product_entity.sku = 'L6526DP';
select * from catalog_product_entity_varchar WHERE entity_id = '2382';
UPDATE catalog_product_entity_varchar SET value = '' WHERE entity_id = 2386 AND value_id = 78676;
UPDATE catalog_product_entity_varchar SET value = 'Striscia Led flessibile bianco caldo CRI 90+ 5m 24V HD Double 144W' WHERE entity_id = 2386 AND value_id = 78676;
UPDATE catalog_product_entity_varchar SET value = 'Striscia Led flessibile bianco caldo 5m 24V 144W CRI 90+' WHERE entity_id = 2386 AND value_id = 78677;
UPDATE catalog_product_entity_varchar SET value = 'Striscia Led flessibile bianco caldo 5m 24V 144W CRI 90+' WHERE entity_id = 2386 AND value_id = 78678;


select * from catalog_product_entity WHERE catalog_product_entity.sku = '_L6506DP';
select * from catalog_product_entity_varchar WHERE entity_id = '2985';

UPDATE catalog_product_entity_varchar SET value = 'LED Flex Strip HD Double Mono Premium, warm weiß, CRI 90+ 5m 24VDC' WHERE entity_id = 2985 AND value_id = 103961;
UPDATE catalog_product_entity_varchar SET value = 'LED Flex Strip HD Double Mono Premium, warm weiß, CRI 90+ 5m 24VDC' WHERE entity_id = 2985 AND value_id = 103960;
UPDATE catalog_product_entity_varchar SET value = 'Striscia Led flessibile bianco neutrale 5m 24V 24W CRI 90+' WHERE value_id = 20313;
UPDATE catalog_product_entity_varchar SET value = 'Striscia Led flessibile super bianco caldo 2700K 5m 24V 48W CRI 90+' WHERE value_id = 20340;
UPDATE catalog_product_entity_varchar SET value = 'Striscia Led flessibile bianco caldo 5m 24V 48W CRI 90+' WHERE value_id = 20367;
UPDATE catalog_product_entity_varchar SET value = 'Striscia Led flessibile bianco neutrale 5m 24V 48W CRI 90+' WHERE value_id = 20394;
UPDATE catalog_product_entity_varchar SET value = 'Striscia Led flessibile super bianco caldo 2700K 5m 24V 72W CRI 90+ HD' WHERE value_id = 20421;
UPDATE catalog_product_entity_varchar SET value = 'Striscia Led flessibile bianco caldo 24V 72W CRI 90+ HD' WHERE value_id = 20448;
UPDATE catalog_product_entity_varchar SET value = 'Striscia Led flessibile bianco neutrale 24V 72W CRI 90+ HD' WHERE value_id = 20478;
UPDATE catalog_product_entity_varchar SET value = 'Proled Led Flex Strip warm weiß 24W CRI 90+' WHERE value_id = 20535;
UPDATE catalog_product_entity_varchar SET value = 'Proled Led Flex Strip neutral weiß 24W CRI 90+' WHERE value_id = 20562;
UPDATE catalog_product_entity_varchar SET value = 'Proled Led Flex Strip neutral weiß 48W CRI 90+' WHERE value_id = 20606;
UPDATE catalog_product_entity_varchar SET value = 'Proled Led Flex Strip warm weiß 48W CRI 95+' WHERE value_id = 20633;
UPDATE catalog_product_entity_varchar SET value = 'Proled Led Flex Strip warm weiß 2700K 48W CRI 90+' WHERE value_id = 20714;
UPDATE catalog_product_entity_varchar SET value = 'Proled Led Flex Strip warm weiß 2700K 72W CRI 90+' WHERE value_id = 20753;
UPDATE catalog_product_entity_varchar SET value = 'Proled Led Flex Strip warm weiß 72W CRI 90+' WHERE value_id = 20780;
UPDATE catalog_product_entity_varchar SET value = 'Proled Led Flex Strip neutral weiß 72W CRI 90+' WHERE value_id = 20807;
UPDATE catalog_product_entity_varchar SET value = 'Striscia Led flessibile 2700K 5m 24V 144W CRI 90+' WHERE value_id = 78575;
UPDATE catalog_product_entity_varchar SET value = 'Striscia Led flessibile bianco neutrale CRI 90+ 5m 24V HD Double 144W' WHERE value_id = 78717;
UPDATE catalog_product_entity_varchar SET value = 'Striscia Led flessibile 5630 super bianco caldo 2700K 5m 24V 126W CRI 90+ (alta resa cromatica)' WHERE value_id = 78751;
UPDATE catalog_product_entity_varchar SET value = 'Striscia Led flessibile 5630 bianco caldo 5m 24V 126W CRI 90+' WHERE value_id = 78785;
UPDATE catalog_product_entity_varchar SET value = 'Striscia Led flessibile 5630 bianco neutrale 5m 24V 126W CRI 90+' WHERE value_id = 78826;

-- TODO upload product with sku L69F00R
-- prestashop crashs constantly