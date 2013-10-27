<!DOCTYPE html>
<html ng-app>
<head>
<title>宅人汇</title>

<?php
include_once 'includes.php';
$shops = query_all_shops ();
?>
<script type="text/javascript">
	var shops = <?php echo json_encode($shops); ?>;

	function shopListCtrl($scope) {
    		 return $scope.shops = shops;
	}
</script>

</head>
<body ng-controller="shopListCtrl">
	<div id="page1" data-role="page">
		<div data-role="header" data-position="fixed">
			<h1>宅人汇</h1>
		</div>
		<div data-role="content">
			<ul data-role="listview" data-inset="true" data-filter="true"
				data-filter-placeholder="搜索">
				<li ng-repeat="shop in shops"><a
					href="goodsTypes.php?shopId={{shop.ID}}&shopName={{shop.NAME}}"
					rel="external">{{ shop.NAME }}</a></li>
			</ul>
		</div>
		<div data-role="footer" data-position="fixed">
			<h1>www.zhairenhui.com</h1>
		</div>
	</div>
</body>