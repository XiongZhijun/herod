<?php
$db_server_host = "172.16.254.15";

$db_server_name = "root";

$db_server_password = "123456";

$db_db_name = "herod-order-release";

// sqls

$query_shops_sql = "SELECT * FROM ZRH_SHOP";

$query_goods_types_sql = "SELECT * FROM ZRH_GOODS_CATEGORY";

$query_goodses_sql = "SELECT G.ID, G.NAME, G.CODE, G.ALIAS, G.SUPPLY_PRICE, G.SELLING_PRICE, G.UNIT, G.COMMENT,
 	G.LARGE_IMAGE, G.THUMBNAIL, G.CATEGORY_ID, G.SHOP_ID, G.AGENT_ID, G.PINYIN, G.SORT,S.NAME SHOP_NAME
 	FROM ZRH_GOODS G INNER JOIN ZRH_SHOP S ON G.SHOP_ID = S.ID";

?>