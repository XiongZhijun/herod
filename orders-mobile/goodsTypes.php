<!DOCTYPE html>
<html ng-app>
<head>

<?php
include "includes.php";
$goodsTypes = query_goods_types_by_shop ( $_GET ['shopId'] );
$shopName = $_GET ['shopName'];
?>
<title><?php echo $shopName ?></title>
<script type="text/javascript">
	var goodsTypes = <?php echo json_encode($goodsTypes); ?>;

	function goodsTypeListCtrl($scope) {
    		 return $scope.goodsTypes = goodsTypes;
	}
</script>
</head>
<body ng-controller="goodsTypeListCtrl">
    <div id="page1" data-role="page">
        <div data-role="header" data-position="fixed">
            <a href="shops.php" data-rel="back" data-icon="arrow-l">首页</a>
            <h1><?php echo $shopName ?></h1>
            <a href="#">购物车 <span
                class="ui-li-count ui-btn-up-c ui-btn-corner-all">10</span></a>
        </div>
        <div data-role="content">
            <ul data-role="listview" data-filter="true"
                data-filter-placeholder="搜索">
                <li ng-repeat="goodsType in goodsTypes"><a
                    href="goodses.php?goodsTypeId={{goodsType.ID}}&shopName=<?php echo $shopName ?>&goodsTypeName={{goodsType.NAME}}"
                    rel="external">{{ goodsType.NAME }}</a></li>
            </ul>
        </div>
    </div>
</body>