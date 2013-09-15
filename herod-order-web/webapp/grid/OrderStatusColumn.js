/**
 * 
 * 
 * @class grid.OrderStatusColumn
 * @extends Ext.grid.column.Column
 * @author Xiong Zhijun
 * @date 2011-12-08
 */
Ext.define('grid.OrderStatusColumn', {
	extend : 'Ext.grid.column.Column',
	alias : [ 'widget.orderstatuscolumn' ],
	imgWidth : 16,
	imgHeight : 16,
	initComponent : function() {
		var me = this;
		me.callParent(arguments);
		me.renderer = function(value) {
			switch (value) {
			case 'Unsubmit':
				return '未提交';
			case 'Submitted':
				return '待受理';
			case 'Acceptted':
				return '待完成';
			case 'Completed':
				return '已完成';
			case 'Rejected':
				return '已回退';
			case 'Cancelled':
				return '已取消';
			default:
				return '';
			}

		}
	}
});