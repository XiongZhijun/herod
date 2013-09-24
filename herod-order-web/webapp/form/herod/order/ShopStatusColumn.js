/**
 * 
 * 
 * @class form.herod.order.ShopStatusColumn
 * @extends Ext.grid.column.Column
 * @author Xiong Zhijun
 * @date 2011-12-08
 */
Ext.define('form.herod.order.ShopStatusColumn', {
	extend : 'Ext.grid.column.Column',
	alias : [ 'widget.shopstatuscolumn' ],
	imgWidth : 16,
	imgHeight : 16,
	initComponent : function() {
		var me = this;
		me.callParent(arguments);
		me.renderer = function(value) {
			switch (value) {
			case 'OPEN':
				return '正常营业';
			case 'SUSPEND':
				return '暂停营业';
			default:
				return '';
			}

		}
	}
});