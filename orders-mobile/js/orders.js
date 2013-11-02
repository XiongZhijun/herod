var order_cache = {
    shopId : -1,
    orderItems : [],
    load : function(shopId) {
        var str = localStorage.getItem("orderItems");
        this.orderItems = [];
        tmp = [];
        if (str) {
            tmp = JSON.parse(str);
        }
        for (index in tmp) {
            if (tmp[index] && tmp[index].shopId == shopId) {
                this.orderItems.push(tmp[index]);
            }
        }
    },
    increaseGoods : function(goods) {
        var orderItem = this.findOrderItem(goods.ID);
        if (orderItem) {
            return this.increaseOrderItem(orderItem);
        }
        orderItem = createOrderItem(goods);
        this.orderItems.push(orderItem);
        this.saveOrderItems();
        return orderItem.quantity;
    },
    decreaseGoods : function(goods) {
        var orderItem = this.findOrderItem(goods.ID);
        return this.decreaseOrderItem(orderItem);
    },
    increaseByGoodsId : function(goodsId) {
        var orderItem = this.findOrderItem(goodsId);
        return this.increaseOrderItem(orderItem);
    },
    decreaseByGoodsId : function(goodsId) {
        var orderItem = this.findOrderItem(goodsId);
        return this.decreaseOrderItem(orderItem);
    },
    increaseOrderItem : function(orderItem) {
        if (orderItem == null) {
            return 0;
        }
        orderItem.quantity++;
        this.saveOrderItems();
        return orderItem.quantity;
    },
    decreaseOrderItem : function(orderItem) {
        if (orderItem == null) {
            return 0;
        }
        orderItem.quantity--;
        if (orderItem.quantity > 0) {
            this.saveOrderItems();
            return orderItem.quantity;
        }
        for (index in this.orderItems) {
            if (this.orderItems[index].quantity <= 0) {
                this.orderItems.splice(index, 1);
            }
        }
        this.saveOrderItems();
    },
    findOrderItem : function(goodsId) {
        for (index in this.orderItems) {
            if (this.orderItems[index].goodsId == goodsId) {
                return this.orderItems[index];
            }
        }
        return null;
    },
    getGoodsCount : function(goodsId) {
        var item = this.findOrderItem(goodsId);
        return item ? item.quantity : 0;
    },
    getTotalCount : function() {
        var total = 0;
        for (index in this.orderItems) {
            total += this.orderItems[index].quantity;
        }
        return total;
    },
    getTotalAmount : function() {
        var total = 0;
        for (index in this.orderItems) {
            var orderItem = this.orderItems[index];
            total += orderItem.sellingPrice * orderItem.quantity;
        }
        return total;
    },
    getCostOfRunErrands : function(shopId) {
        var shop = shop_cache.findShop(shopId);
        if (shop == null) {
            return 0;
        }
        var totalAmount = this.getTotalAmount();
        return totalAmount >= parseFloat(shop.MIN_CHARGE_FOR_FREE_DELIVERY) ? 0
                : parseFloat(shop.COST_OF_RUN_ERRANDS);
    },
    saveOrderItems : function() {
        var str = JSON.stringify(this.orderItems);
        localStorage.setItem("orderItems", str);
    }
};

function createOrderItem(goods) {
    return {
        'goodsId' : goods.ID,
        'goodsCode' : goods.CODE,
        'goodsName' : goods.NAME,
        'unit' : goods.UNIT,
        'agentId' : goods.AGENT_ID,
        'shopId' : goods.SHOP_ID,
        'shopName' : goods.SHOP_NAME,
        'sellingPrice' : goods.SELLING_PRICE,
        'supplyPrice' : goods.SUPPLY_PRICE,
        'quantity' : 1
    };
};

var shop_cache = {
    shops : [],
    loaded : false,
    load : function() {
        if (this.loaded && this.shops.length > 0) {
            return;
        }
        var str = localStorage.getItem("shops");
        if (str) {
            this.shops = JSON.parse(str);
            this.loaded = true;
        }
    },
    addShops : function(shops) {
        this.shops = shops;
        var str = JSON.stringify(this.shops);
        localStorage.setItem("shops", str);
        this.loaded = true;
    },
    findShop : function(shopId) {
        this.load();
        for (index in this.shops) {
            if (this.shops[index].ID == shopId) {
                return this.shops[index];
            }
        }
        return null;
    }
};