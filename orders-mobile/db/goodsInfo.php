<?php
include_once 'config/db_config.php';
function query_all_shops() {
	global $query_shops_sql;
	return query_goods_infos ( $query_shops_sql, array () );
}
function query_goods_types_by_shop($shopId) {
	global $query_goods_types_sql;
	return query_goods_infos ( $query_goods_types_sql, array (
			"SHOP_ID" => $shopId
	) );
}
function query_goodses_by_goods_type($goodsTypeId) {
	global $query_goodses_sql;
	return query_goods_infos ( $query_goodses_sql, array (
			"CATEGORY_ID" => $goodsTypeId
	) );
}
function query_goods_infos($sql, $conditions) {
	global $db_server_host, $db_server_name, $db_server_password, $db_db_name;
	$con = mysql_connect ( $db_server_host, $db_server_name, $db_server_password );
	if (! $con) {
		die ( "Could not connect to database server." );
	}

	mysql_select_db ( $db_db_name, $con );
	$where = "";
	foreach ( $conditions as $key => $value ) {
		if (strlen ( $where ) > 0) {
			$where .= " and ";
		}
		$where .= ($key . "=" . $value);
	}

	$executeSql = strlen ( $where ) == 0 ? $sql : $sql . " where " . $where;

	$result = mysql_query ( $executeSql, $con );
	$records = array ();
	while ( ($row = mysql_fetch_assoc ( $result )) != FALSE ) {
		$records [] = $row;
	}
	return $records;
}

?>