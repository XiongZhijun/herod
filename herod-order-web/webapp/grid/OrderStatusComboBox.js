/**
 * 
 * 
 * @class OrderStatusComboBox
 * @extends Ext.form.field.ComboBox
 * @author Xiong Zhijun
 * @date 2011-12-08
 */
Ext.define('grid.OrderStatusComboBox', {
	extend : 'Ext.form.field.ComboBox',
	alias : [ 'widget.orderstatuscombobox' ],
	queryMode : 'local',
	displayField : 'name',
	valueField : 'id',

	initComponent : function() {
		var me = this;
		me.store = Ext.create('Ext.data.Store', {
			fields : [ 'id', 'name' ],
			data : [ {
				"id" : "Unsubmit",
				"name" : "未提交"
			}, {
				"id" : "Submitted",
				"name" : "待受理"
			}, {
				"id" : "Acceptted",
				"name" : "待完成"
			}, {
				"id" : "Completed",
				"name" : "已完成"
			}, {
				"id" : "Rejected",
				"name" : "已回退"
			}, {
				"id" : "Cancelled",
				"name" : "已取消"
			} ]
		});

		me.callParent(arguments);
	}
});