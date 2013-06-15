/**
 * @class form.herod.order.GoodsCategoryForm
 * @author Xiong Zhijun
 * @date 2011-7-4
 * @extends Ext.Component
 */
Ext.ns('form.herod.order');
Ext.define('form.herod.order.GoodsCategoryForm', {
	extend : 'form.Form',
	defaults : {
		labelAlign : 'right',
		margin : '5 5 5 5',
		labelWidth : 70,
		msgTarget : 'side',
	},
	initComponent : function() {
		var me = this;

		Ext.apply(me, {
			items : [ {
				id : 'NAME',
				name : 'NAME',
				fieldLabel : '分类名称',
				allowBlank : false,
				msgTarget : 'side',
			}, {
				id : 'ALIAS',
				name : 'ALIAS',
				fieldLabel : '别名',
			}, {
				id : 'SORT',
				name : 'SORT',
				fieldLabel : '排序',
				regex : /^[0-9]+$/,
				regexText : '请输入整数',
				xtype : 'numberfield',
				allowBlank : false,
				msgTarget : 'side',
				value : 255,
				minValue : 1
			} ]
		});
		me.callParent();
	},
	loadNode : function() {
		var me = this;
		me.callParent();
	},
	getFormData : function() {
		var me = this;
		var formData = me.callParent();
		formData.fields.push({
			name : 'SHOP_ID',
			value : me.gridPanel.shopId
		});
		return formData;
	},
});
