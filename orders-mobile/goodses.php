<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" " http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html ng-app>
<head>

<link rel="stylesheet" href="css/goodses.css" />
<?php
include "includes.php";
$goodses = query_goods_infos ( $query_goodses_sql, array (
		"CATEGORY_ID" => $_GET ['goodsTypeId']
) );
$shopName = $_GET ['shopName'];
$goodsTypeName = $_GET ['goodsTypeName'];
$title = $shopName . " / " . $goodsTypeName;
?>
<title><?php echo $title?></title>

<script type="text/javascript">
	var goodses = <?php echo json_encode($goodses); ?>;

	function goodsListCtrl($scope) {
		 $scope.orderProp = 'PINYIN';
    		 return $scope.goodses = goodses;
	}

	$(document).ready(function() {
	    initLayout();
	    $(window).resize(function() {
	        initLayout();
	    });
	});
	function initLayout() {
		$('.ui-block-c').width($('.ui-block-c > fieldset').width()+20);
		$('.ui-block-a').width($('.ui-block-a > div').width()+5);
	    var w = $('li').width() - $(".ui-block-a").width() - $(".ui-block-c").width();
	    $('.ui-block-b').width(w);
	    $('.ui-block-a').height($('li').height());
	    $('.ui-block-b').height($('li').height());
	    $('.ui-block-c').height($('li').height());
	}

</script>


</head>
<body ng-controller="goodsListCtrl">
	<div id="page1" data-role="page">
		<div data-role="header" data-position="fixed">
			<a data-rel="back" data-icon="arrow-l" href="#">返回</a>
			<h1><?php echo $title ?></h1>
			<a href="#">购物车 <span
				class="ui-li-count ui-btn-up-c ui-btn-corner-all">10</span></a>
		</div>
		<div data-role="content">
			<ul data-role="listview" data-filter="true"
				data-filter-placeholder="搜索">
				<li ng-repeat="goods in goodses | filter:query | orderBy:orderProp">

					<div class="ui-grid-b">
						<div class="ui-block-a">
							<div data-mini="true" data-role="controlgroup">
								<img src="http://121.199.54.83/{{goods.THUMBNAIL}}" />
							</div>
						</div>
						<div class="ui-block-b" style="">
							<div data-role="controlgroup" style="margin: 0 0">
								<h2>{{goods.NAME}}</h2>
								<p>
									单价 : <em>￥{{ goods.SELLING_PRICE}}/{{goods.UNIT}}</em>
								</p>
							</div>

						</div>
						<div class="ui-block-c" style="float: right; height: 100%;">
							<fieldset data-role="controlgroup" data-type="horizontal"
								data-mini="true"
								style="float: right; height: 100%; padding: 10px 0px">
								<button data-icon="minus" data-iconpos="notext">减少</button>
								<button disabled="disabled">0</button>
								<button data-icon="plus" data-iconpos="notext">增加</button>
							</fieldset>

						</div>
					</div>
				</li>
			</ul>
		</div>

</body>