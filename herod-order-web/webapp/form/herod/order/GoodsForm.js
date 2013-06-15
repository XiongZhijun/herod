/**
 * @class form.herod.order.GoodsForm
 * @author Xiong Zhijun
 * @date 2011-7-4
 * @extends Ext.Component
 */
Ext.ns('form.herod.order');
Ext.define('form.herod.order.GoodsForm', {
	extend : 'form.Form',
	defaults : {
		labelAlign : 'right',
		margin : '5 5 5 5',
		labelWidth : 80,
		msgTarget : 'side',
		allowBlank : false,
	},
	images : null,
	initComponent : function() {
		var me = this;

		Ext.apply(me, {
			items : [ {
				id : 'NAME',
				name : 'NAME',
				fieldLabel : '商品名称',
				msgTarget : 'side',
			}, {
				id : 'LARGE_IMAGE',
				name : 'LARGE_IMAGE',
				fieldLabel : '图片',
				rowspan : 5,
				width : 200,
				height : 150,
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
					fieldLabel : '商品图片',
					labelWidth : 80,
					emptyText : '请选择图片……',
					buttonText : '选择图片',
					xtype : 'fileuploadfield',
					listeners : {
						'change' : function() {
							fileUploadField = this;
							me.uploadImage(this, function(images) {
								Ext.getCmp("LARGE_IMAGE").setSrc(images[0]);
								me.images = images;
							},{'BUILD_THUMBNAIL':true});
						}
					}
				} ]
			}, {
				id : 'CODE',
				name : 'CODE',
				fieldLabel : '商品编码',
				msgTarget : 'side',
			}, {
				id : 'ALIAS',
				name : 'ALIAS',
				fieldLabel : '商品别名',
				msgTarget : 'side',
				allowBlank : true,
			}, {
				id : 'SUPPLY_PRICE',
				name : 'SUPPLY_PRICE',
				xtype : 'numberfield',
				fieldLabel : '商店供货价',
				msgTarget : 'side',
				minValue : 0
			}, {
				id : 'SELLING_PRICE',
				name : 'SELLING_PRICE',
				xtype : 'numberfield',
				fieldLabel : '售价',
				msgTarget : 'side',
				minValue : 0
			}, {
				id : 'UNIT',
				name : 'UNIT',
				fieldLabel : '计量单位',
				msgTarget : 'side',
			}, {
				id : 'COMMENT',
				name : 'COMMENT',
				xtype : 'textareafield',
				fieldLabel : '备注',
				width : 500,
				colspan : 2,
				allowBlank : true
			} ]
		});

		me.callParent();
	},
	setLocation : function() {
		var mapPanel = Ext.create('Ext.panel.Panel', {
			id : 'allmap',
			loader : {
				url : '/form/herod/order/Map.html',
				scripts : true,
				autoLoad : true
			}
		});
		var window = Ext.create('Ext.window.Window', {
			title : '商店定位',
			layout : 'fit',
			width : 800,
			height : 600,
			modal : true,
			items : [ mapPanel ],
			buttons : [ {
				text : '确定',
				handler : function() {
					window.close();
				}
			} ]
		});
		window.show();
	},
	getFormData : function() {
		var me = this;
		var formData = me.callParent();
		formData.fields.push({
			name : 'LARGE_IMAGE',
			value : Ext.getCmp("LARGE_IMAGE").src
		});
		formData.fields.push({
			name : 'THUMBNAIL',
			value : me.images[1]
		});
		formData.fields.push({
			name : 'SHOP_ID',
			value : me.gridPanel.shopId
		});
		formData.fields.push({
			name : 'CATEGORY_ID',
			value : me.gridPanel.categoryId
		});
		return formData;
	},
	afterLoad : function(fieldValueMap) {
		Ext.getCmp("LARGE_IMAGE").setSrc(
				fieldValueMap['LARGE_IMAGE'] == null ? ''
						: fieldValueMap['LARGE_IMAGE']);
	},
	clearValue : function() {
		var me = this;
		me.callParent();
		Ext.getCmp("LARGE_IMAGE").setSrc('');
	}
});
