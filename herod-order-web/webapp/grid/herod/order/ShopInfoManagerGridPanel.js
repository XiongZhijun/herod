/**
 * @class grid.herod.order.ShopInfoManagerGridPanel
 * @extends bear.module.datasource.InfoManagerGridPanelControl
 * @author
 * @date 2011-10-17
 * @integrated true
 * @componentPath 宅人汇.商店管理列表
 * @configClass bear.module.datasource.config.InfoManagerGridPanelConfigControl
 */
Ext.require('bear.module.datasource.SearchPanelControl');
Ext.require('bear.module.datasource.InfoManagerGridPanelControl');
Ext.require('bear.control.ux.DWRProxy');
Ext.require('bear.module.datasource.Tools');

Ext.define('grid.herod.order.ShopInfoManagerGridPanel', {
	extend : 'grid.InfoManagerGridPanel',
	require : [ 'bear.module.datasource.SearchPanelControl',
			'bear.module.datasource.InfoManagerGridPanelControl' ],
	statics : {
		confirmWin : Ext.create('Ext.window.MessageBox', {
			buttonText : {
				yes : "确定",
				no : "取消",
				ok : '确定'
			}
		})
	},
	constructor : function(config) {
		var me = this;
		config.gridPanelConfig.operationItems = [ {
			icon : 'grid/herod/order/goods_edit.png',
			tooltip : '商品编辑',
			handler : function(grid, rowIndex, colIndex) {
				var record = grid.store.getAt(rowIndex).data;
				var config = {
					'shopId' : record.ID || record.id,
					'record' : record
				};
				var goodsEditor = Ext.create(
						'grid.herod.order.ShopGoodsEditor', config);
				var window = Ext.create('Ext.window.Window', {
					title : '商店商品管理',
					layout : 'fit',
					width : 800,
					height : 600,
					modal : true,
					items : [ goodsEditor ],
					buttons : [ {
						text : '关闭',
						handler : function() {
							window.close();
						}
					} ]
				});
				window.show();
			}
		} ];
		this.callParent([ config ]);
	}
});