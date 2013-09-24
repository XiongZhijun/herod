/**
 * @class ShopStatusComboBox
 * @extends Ext.form.field.ComboBox
 * @author Xiong Zhijun
 * @date 2011-12-08
 */
Ext.define('form.herod.order.ShopStatusComboBox', {
	extend : 'Ext.form.field.ComboBox',
	alias : [ 'widget.shopstatuscombobox' ],
	queryMode : 'local',
	displayField : 'name',
	valueField : 'id',
	listeners : {
		afterRender : function(combo) {
			combo.setValue('OPEN');
		}
	},
	initComponent : function() {
		var me = this;
		me.store = Ext.create('Ext.data.Store', {
			fields : [ 'id', 'name' ],
			data : [ {
				"id" : "OPEN",
				"name" : "正常营业"
			}, {
				"id" : "SUSPEND",
				"name" : "暂停营业"
			} ]
		});
		me.callParent(arguments);
	}
});