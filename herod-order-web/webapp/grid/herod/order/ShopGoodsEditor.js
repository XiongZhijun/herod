Ext.define('grid.herod.order.ShopGoodsEditor', {
	extend : 'Ext.panel.Panel',
	layout : 'border',

	initComponent : function() {
		var me = this;

		Ext.apply(me, {
			items : [ {
				id : 'categoryGrid',
				region : 'west',
				xtype : 'zrhinfomanagergridpanel',
				width : 200,
				shopId : me.shopId,
				basicCondition : {
					op : '=',
					propertyName : 'SHOP_ID',
					value : me.shopId
				},
				"dataSetConfig" : {
					"dataSetContextLocation" : "tableContext",
					"dataSetCode" : "ZRH_GOODS_CATEGORY",
					"nodeTypeCode" : "ZRH_GOODS_CATEGORY",
					"pageSize" : "20",
					"advancedConfig" : "",
					"requireControls" : "",
					"dataSetContext" : {
						"code" : "ZRH_GOODS_CATEGORY"
					}
				},
				"gridPanelConfig" : {
					"advancedConfig" : "",
					"formClass" : 'form.herod.order.GoodsCategoryForm',
					"addable" : true,
					"deletable" : true,
					"deletselectedable" : true,
					"updatable" : true,
					"checkboxable" : true,
					"columns" : [ {
						"header" : "ID",
						"dataIndex" : "ID",
						"hidden" : true,
					}, {
						"header" : "\u540d\u79f0",
						"dataIndex" : "NAME",
					} ],
					"toolbarItems" : [],
					"operationItems" : [],
					listeners : {
						itemclick : Ext.Function.bind(me.categoryOnClick, me),
					}
				},
				"searchPanelConfig" : {
					"columnCount" : 5,
					"advancedConfig" : "",
					"items" : []
				}
			}, {
				id : 'goodsGrid',
				region : 'center',
				xtype : 'zrhinfomanagergridpanel',
				shopId : me.shopId,
				basicCondition : {
					op : '=',
					propertyName : 'SHOP_ID',
					value : -1
				},
				"dataSetConfig" : {
					"dataSetContextLocation" : "tableContext",
					"dataSetCode" : "ZRH_GOODS",
					"nodeTypeCode" : "ZRH_GOODS",
					"pageSize" : "20",
					"advancedConfig" : "",
					"requireControls" : "",
					"dataSetContext" : {
						"code" : "ZRH_GOODS"
					}
				},
				"gridPanelConfig" : {
					"advancedConfig" : "",
					"formClass" : "form.herod.order.GoodsForm",
					"addable" : true,
					"deletable" : true,
					"deletselectedable" : true,
					"updatable" : true,
					"checkboxable" : true,
					"columns" : [ {
						"header" : "ID",
						"dataIndex" : "ID",
						"hidden" : true,
					}, {
						"header" : "\u540d\u79f0",
						"dataIndex" : "NAME",
					}, {
						"header" : "\u7f16\u7801",
						"dataIndex" : "CODE",
					}, {
						"header" : "\u4f9b\u5e94\u4ef7\u683c",
						"dataIndex" : "SUPPLY_PRICE",
					}, {
						"header" : "\u552e\u4ef7",
						"dataIndex" : "SELLING_PRICE",
					}, {
						"header" : "\u8ba1\u91cf\u5355\u4f4d",
						"dataIndex" : "UNIT",
					} ],
					"toolbarItems" : [],
					"operationItems" : []
				},
				"searchPanelConfig" : {
					"columnCount" : 5,
					"advancedConfig" : "",
					"items" : [ {
						"fieldLabel" : "\u540d\u79f0",
						"propertyName" : "",
						"op" : "like",
						labelWidth : 50,
						"xtype" : "textfield",
						"type" : "string",
						"colspan" : 1,
						"name" : "NAME",
					}, {
						"fieldLabel" : "\u7f16\u7801",
						"propertyName" : "",
						labelWidth : 50,
						"op" : "like",
						"xtype" : "textfield",
						"type" : "string",
						"colspan" : 1,
						"name" : "CODE",
					} ]
				}
			} ],
		});

		me.callParent();
	},

	categoryOnClick : function(view, record, item, index, e, eOpts) {
		var goodsGrid = Ext.getCmp('goodsGrid');
		var categoryId = record.data.ID || record.data.id;
		goodsGrid.categoryId = categoryId;
		goodsGrid.basicCondition = {
			op : '=',
			propertyName : 'CATEGORY_ID',
			value : categoryId
		};
		goodsGrid.search();
	}
});