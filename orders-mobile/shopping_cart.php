<!DOCTYPE html>
<html ng-app>
<head>

<?php
include "includes.php";
$shopId = $_GET ['shopId'];
$shopName = $_GET ['shopName'];
?>
<title><?php echo $shopName ?></title>
<script type="text/javascript" src="js/orders.js"></script>
<script type="text/javascript">
    var shopId = <?php echo $shopId?>;
    $(document).bind('pagechange', function(event, options){
        if (options.toPage[0].id == 'page1' && options.options
                && options.options.fromPage && options.options.fromPage[0]
                && options.options.fromPage[0].id == 'deleteOrderItemDialog') {
            location.reload();
        }
    });
    function orderItemListCtrl($scope) {
        order_cache.load(shopId);
        $scope.totalQuantity = order_cache.getTotalCount();
        $scope.costOfRunErrands = order_cache.getCostOfRunErrands(shopId);
        $scope.totalAmount = order_cache.getTotalAmount();
        $scope.orderItems = order_cache.orderItems;
        $scope.shop = shop_cache.findShop(shopId);
    }
    function orderItemController($scope, $element) {
        var goodsId = $scope.orderItem.goodsId;
        $scope.quantity = order_cache.getGoodsCount(goodsId);
        $scope.decreaseGoods = function(orderItem){
            var quantity = order_cache.getGoodsCount(orderItem.goodsId);
            if (quantity == 1) {
                $('#deleteOrderItemButton').click(function() {
                    order_cache.decreaseByGoodsId(orderItem.goodsId);
                });
                $.mobile.changePage('#deleteOrderItemDialog', 'dialog', true, true);
                return;
            }
            $scope.quantity = order_cache.decreaseByGoodsId(orderItem.goodsId);
            $scope.$parent.$parent.totalQuantity = order_cache.getTotalCount();
            $scope.$parent.$parent.costOfRunErrands = order_cache.getCostOfRunErrands(shopId);
            $scope.$parent.$parent.totalAmount = order_cache.getTotalAmount();

        };
        $scope.increaseGoods = function(orderItem) {
            $scope.quantity = order_cache.increaseByGoodsId(orderItem.goodsId);
            $scope.$parent.$parent.totalQuantity = order_cache.getTotalCount();
            $scope.$parent.$parent.costOfRunErrands = order_cache.getCostOfRunErrands(shopId);
            $scope.$parent.$parent.totalAmount = order_cache.getTotalAmount();
        };
    }
</script>
<style type="text/css">
[data-role="content"]>p>span {
    color: red;
}
</style>
</head>
<body ng-controller="orderItemListCtrl">
    <div id="page1" data-role="page">
        <div data-role="header" data-position="fixed">
            <a data-rel="back" data-icon="arrow-l" href="#">返回</a>
            <h1>购物车</h1>
        </div>
        <div data-role="content">
            <a id='showDeleteOrderItemDialog'
                href="#deleteOrderItemDialog" data-rel="dialog"
                data-transition="pop" style='display: none;'></a>
            <p>
                消费总计：<span>￥{{totalAmount + costOfRunErrands}}</span>（其中配送费：<span>￥{{costOfRunErrands}}</span>）商品总数：<span>{{totalQuantity}}</span>
            </p>
            <ul data-role="listview">
                <li ng-repeat="orderItem in orderItems track by $index"
                    ng-controller="orderItemController">
                    <div class="ui-grid-b">
                        <div class="ui-block-a" style="">
                            <div data-role="controlgroup"
                                style="margin: 0 0">
                                <h2>{{orderItem.goodsName}}</h2>
                            </div>
                        </div>
                        <div class="ui-block-b" style="">
                            <div data-role="controlgroup"
                                style="margin: 0 0">
                                <h2>￥{{orderItem.sellingPrice}}/{{orderItem.unit}}</h2>
                            </div>
                        </div>
                        <div class="ui-block-c"
                            style="float: right; height: 100%;">
                            <fieldset data-role="controlgroup"
                                data-type="horizontal" data-mini="true"
                                style="float: right; height: 100%; padding: 10px 0px">
                                <a data-role="button" data-icon="minus"
                                    data-iconpos="notext"
                                    ng-click="decreaseGoods(orderItem)">减少</a>
                                <a data-role="button"><span
                                    disabled="disabled"
                                    ng-model="quantity">{{quantity}}</span></a>
                                <a data-role="button" data-icon="plus"
                                    data-iconpos="notext"
                                    ng-click="increaseGoods(orderItem)">增加</a>
                            </fieldset>
                        </div>
                    </div>
                </li>
            </ul>
            <p>
                商店配送费<span>￥{{shop.COST_OF_RUN_ERRANDS}}</span>，消费满<span>￥{{shop.MIN_CHARGE_FOR_FREE_DELIVERY}}</span>免配送费
            </p>
        </div>
    </div>
    <div id="deleteOrderItemDialog" data-role="dialog">
        <div data-role="header" data-position="fixed">
            <h1>提示</h1>
        </div>
        <div data-role="content">
            <p>确定将该商品从购物车中删除吗？</p>
            <a data-role="button" id="deleteOrderItemButton"
                data-rel="back" href="#page1">确定删除</a> <a
                data-rel="back" data-role="button"
                href="#showDeleteOrderItemDialog">返回</a>
        </div>
    </div>
</body>