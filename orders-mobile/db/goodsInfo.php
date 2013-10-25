<?php
include_once 'config/db_config.php';
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
	while ( $row = mysql_fetch_assoc ( $result ) ) {
		$records [] = $row;
	}
	return $records;
}

?>