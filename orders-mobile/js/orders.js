var order_cache = {
	orderItems : [],

	increaseGoods : function(goods) {
		for (index in this.orderItems) {
			if (this.orderItems[index].goodsId == goods.ID) {
				this.orderItems[index].quantity++;
				return this.orderItems[index].quantity;
			}
		}
		var orderItem = createOrderItem(goods);
		this.orderItems.push(orderItem);
		return 1;
	},
	decreaseGoods : function(goods) {
		for (index in this.orderItems) {
			if (this.orderItems[index].goodsId == goods.ID) {
				this.orderItems[index].quantity--;
				if (this.orderItems[index].quantity == 0) {
					this.orderItems.splice(index,1);
					return 0;
				}
				return this.orderItems[index].quantity;
			}
		}
	}
};

function createOrderItem(goods) {
	return {
		'goodsId' : goods.ID,
		'goodsCode' : goods.CODE,
		'goodsName' : goods.NAME,
		'agentId' : goods.AGENT_ID,
		'shopId' : goods.SHOP_ID,
		'shopName' : goods.,
		'sellingPrice' : goods.SELLING_PRICE,
		'supplyPrice' : goods.SUPPLY_PRICE,
		'quantity' : 1
	};
}