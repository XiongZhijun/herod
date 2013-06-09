/**
 * @class form.herod.order.ShopTypeForm
 * @author Xiong Zhijun
 * @date 2011-7-4
 * @extends Ext.Component
 */
Ext.ns('form.herod.order');
// 树型导航器
Ext.define('form.herod.order.ShopTypeForm', {
	requires:['form.Form'],
	extend : 'form.Form',
	initComponent: function() {
		var me = this;
		
		Ext.apply(me, {
			items: [{
			id : 'NAME',
			name : 'NAME',
			fieldLabel : '类型名称',
			labelWidth : 70,
			allowBlank : false,
			msgTarget : 'side',
		}, {
			fieldLabel : '图片',
			labelWidth : 70,
			rowspan : 2,
			width : 200,
			height : 100,
			src : '',
			align : 'center',
			xtype : 'image'
		}, {
			id : 'IMAGE_URL',
			name : 'IMAGE_URL',
			fieldLabel : '图片',
			labelWidth : 70,
			xtype : 'fileuploadfield'
		}, {
			id : 'COMMENT',
			name : 'COMMENT',
			xtype : 'textareafield',
			fieldLabel : '备注',
			labelWidth : 70,
			width : 500,
			colspan : 2
		}]
		});
		
		me.callParent();
	}
});
