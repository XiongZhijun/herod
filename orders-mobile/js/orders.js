var order_cache = {
    orderItems : [],
    load : function() {
        var str = localStorage.getItem("orderItems");
        if (str) {
            this.orderItems = JSON.parse(str);
        }
    },
    increaseGoods : function(goods) {
        for (index in this.orderItems) {
            if (this.orderItems[index].goodsId == goods.ID) {
                this.orderItems[index].quantity++;
                this.saveOrderItems();
                return this.orderItems[index].quantity;
            }
        }
        var orderItem = createOrderItem(goods);
        this.orderItems.push(orderItem);
        this.saveOrderItems();
        return 1;
    },
    decreaseGoods : function(goods) {
        for (index in this.orderItems) {
            if (this.orderItems[index].goodsId == goods.ID) {
                this.orderItems[index].quantity--;
                if (this.orderItems[index].quantity == 0) {
                    this.orderItems.splice(index, 1);
                    this.saveOrderItems();
                    return 0;
                }
                this.saveOrderItems();
                return this.orderItems[index].quantity;
            }
        }
    },
    findGoodsCount : function(goodsId) {
        for (index in this.orderItems) {
            if (this.orderItems[index].goodsId == goodsId) {
                return this.orderItems[index].quantity;
            }
        }
        return 0;
    },
    findTotalCount : function() {
        var total = 0;
        for (index in this.orderItems) {
            total += this.orderItems[index].quantity;
        }
        return total;
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
        'agentId' : goods.AGENT_ID,
        'shopId' : goods.SHOP_ID,
        'shopName' : goods.SHOP_NAME,
        'sellingPrice' : goods.SELLING_PRICE,
        'supplyPrice' : goods.SUPPLY_PRICE,
        'quantity' : 1
    };
}