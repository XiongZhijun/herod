/**
 * @class form.herod.order.ShopTypeForm
 * @author Xiong Zhijun
 * @date 2011-7-4
 * @extends Ext.Component
 */
Ext.ns('form.herod.order');
Ext.define('form.herod.order.ShopTypeForm', {
	requires : [ 'form.Form' ],
	extend : 'form.Form',
	initComponent : function() {
		var me = this;

		Ext.apply(me, {
			items : [ {
				id : 'NAME',
				name : 'NAME',
				fieldLabel : '类型名称',
				labelWidth : 70,
				allowBlank : false,
				msgTarget : 'side',
			}, {
				id : 'IMAGE_URL',
				name : 'IMAGE_URL',
				fieldLabel : '图片',
				labelWidth : 70,
				rowspan : 2,
				width : 200,
				height : 100,
				src : '',
				align : 'center',
				xtype : 'image'
			}, {
				xtype : 'form',
				border : false,
				defaults : {
					labelAlign : 'right'
				},
				items : [ {
					fieldLabel : '图片',
					labelWidth : 70,
					emptyText : '请选择图片……',
					buttonText : '选择图片',
					xtype : 'fileuploadfield',
					listeners : {
						'change' : function() {
							fileUploadField = this;
							me.uploadImage(this, function(images) {
								Ext.getCmp("IMAGE_URL").setSrc(images[0]);
							});
						}
					}
				} ]
			}, {
				id : 'COMMENT',
				name : 'COMMENT',
				xtype : 'textareafield',
				fieldLabel : '备注',
				labelWidth : 70,
				width : 500,
				colspan : 2
			} ]
		});

		me.callParent();
	},

	getFormData : function() {
		var me = this;
		var formData = me.callParent();
		formData.fields.push({
			name : 'IMAGE_URL',
			value : Ext.getCmp("IMAGE_URL").src
		});
		return formData;
	},
	afterLoad : function(fieldValueMap) {
		Ext.getCmp("IMAGE_URL").setSrc(
				fieldValueMap['IMAGE_URL'] == null ? ''
						: fieldValueMap['IMAGE_URL']);
	},
	clearValue : function() {
		var me = this;
		me.callParent();
		Ext.getCmp("IMAGE_URL").setSrc('');
	}
});
