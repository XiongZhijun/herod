<?php
$db_server_host = "localhost";

$db_server_name = "root";

$db_server_password = "123456";

$db_db_name = "herod-order";

// sqls

$query_shops_sql = "select * from zrh_shop";

$query_goods_types_sql = "select * from zrh_goods_category";

$query_goodses_sql = "SELECT G.ID, G.NAME, G.CODE, G.ALIAS, G.SUPPLY_PRICE, G.SELLING_PRICE, G.UNIT, G.COMMENT,
 	G.LARGE_IMAGE, G.THUMBNAIL, G.CATEGORY_ID, G.SHOP_ID, G.AGENT_ID, G.PINYIN, G.SORT,S.NAME SHOP_NAME
 	FROM ZRH_GOODS G INNER JOIN ZRH_SHOP S ON G.SHOP_ID = S.ID";

?>