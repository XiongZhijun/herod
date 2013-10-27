<?php
include_once 'config/db_config.php';
/**
 * 查询所有的商店
 *
 * @return 所有商店数组。
 */
function query_all_shops() {
	global $query_shops_sql;
	return query_by_sql_and_conditions ( $query_shops_sql, array () );
}
/**
 * 查询商店的所有商品类型
 *
 * @param $shopId 商店ID
 * @return 商店的所有商品类型的数组
 */
function query_goods_types_by_shop($shopId) {
	global $query_goods_types_sql;
	return query_by_sql_and_conditions ( $query_goods_types_sql, array (
			"SHOP_ID" => $shopId
	) );
}
/**
 * 查询某一商品类型的所有商品
 *
 * @param $goodsTypeId 商品类型ID
 * @return 某一类型的所有商品的数组。
 */
function query_goodses_by_goods_type($goodsTypeId) {
	global $query_goodses_sql;
	return query_by_sql_and_conditions ( $query_goodses_sql, array (
			"CATEGORY_ID" => $goodsTypeId
	) );
}
/**
 * 根据sql语句和条件进行数据库查找。返回查找的记录的数组。
 *
 * @param $sql 查询语句
 * @param $conditions 查询条件，是一个关联数组。关联数组的key就是条件字段名，而value就是查询的条件值。
 * @return 返回符合要求的记录数组。
 */
function query_by_sql_and_conditions($sql, $conditions) {
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