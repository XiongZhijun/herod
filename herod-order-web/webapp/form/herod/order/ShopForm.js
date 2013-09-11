/**
 * @class form.herod.order.ShopForm
 * @author Xiong Zhijun
 * @date 2011-7-4
 * @extends Ext.Component
 */
Ext.ns('form.herod.order');
Ext.define('form.herod.order.ShopForm', {
	extend : 'form.Form',
	defaults : {
		labelAlign : 'right',
		margin : '5 5 5 5',
		labelWidth : 80,
		msgTarget : 'side',
		allowBlank : false,
	},
	initComponent : function() {
		var me = this;

		Ext.apply(me, {
			items : [ {
				id : 'NAME',
				name : 'NAME',
				fieldLabel : '商店名称',
				msgTarget : 'side',
			}, {
				id : 'IMAGE_URL',
				name : 'IMAGE_URL',
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
					fieldLabel : '商店图片',
					labelWidth : 80,
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
				id : 'SHOP_TYPE_ID',
				name : 'SHOP_TYPE_ID',
				xtype : 'datasetcombobox',
				contextLocation : 'basicDataSetContext',
				dataSetCode : 'SHOP_TYPE_VIEW',
				fieldLabel : '商店类型',
				msgTarget : 'side',
			}, {
				id : 'ADDRESS',
				name : 'ADDRESS',
				fieldLabel : '商店地址',
				msgTarget : 'side',
			}, {
				id : 'ORGANIZATION_CODE',
				name : 'ORGANIZATION_CODE',
				fieldLabel : '组织机构代码',
				msgTarget : 'side',
				allowBlank : true
			}, {
				xtype : 'form',
				border : false,
				defaults : {
					labelAlign : 'right'
				},
				items : [ {
					fieldLabel : '营业执照',
					labelWidth : 80,
					emptyText : '请选择图片……',
					buttonText : '选择图片',
					xtype : 'fileuploadfield',
					listeners : {
						'change' : function() {
							fileUploadField = this;
							me.uploadImage(this, function(images) {
								var cmp = Ext.getCmp("BUSINESS_LICENSE");
								cmp.setSrc(images[0]);
							});
						}
					}
				} ]
			}, {
				id : 'BUSINESS_LICENSE',
				name : 'BUSINESS_LICENSE',
				fieldLabel : '营业执照',
				rowspan : 5,
				width : 200,
				height : 150,
				src : '',
				align : 'center',
				xtype : 'image'
			}, {
				id : 'LINKMAN',
				name : 'LINKMAN',
				fieldLabel : '联系人',
				msgTarget : 'side',
			}, {
				id : 'CONTACT_NUMBER',
				name : 'CONTACT_NUMBER',
				fieldLabel : '联系电话',
				msgTarget : 'side',
			}, {
				id : 'BANK_NAME',
				name : 'BANK_NAME',
				fieldLabel : '开户行',
				msgTarget : 'side',
				allowBlank : true
			}, {
				id : 'BANK_ACCOUNT',
				name : 'BANK_ACCOUNT',
				fieldLabel : '银行账号',
				msgTarget : 'side',
				allowBlank : true
			}, {
				id : 'COST_OF_RUN_ERRANDS',
				name : 'COST_OF_RUN_ERRANDS',
				xtype : 'numberfield',
				fieldLabel : '配送费',
				msgTarget : 'side',
				allowBlank : true,
				minValue : 0
			}, {
				id : 'MIN_CHARGE_FOR_FREE_DELIVERY',
				name : 'MIN_CHARGE_FOR_FREE_DELIVERY',
				xtype : 'numberfield',
				fieldLabel : '免配送费消费金额',
				msgTarget : 'side',
				allowBlank : true,
				minValue : 0
			}, {
				id : 'LOCATION',
				name : 'LOCATION',
				fieldLabel : '经纬度',
				readOnly : 'true',
				disabled : true,
				msgTarget : 'side',
			}, {
				xtype : 'button',
				text : 'GPS定位',
				scale : 'medium',
				handler : function() {
					me.setLocation();
				}
			}, {
				id : 'SERVICE_RADIUS',
				name : 'SERVICE_RADIUS',
				xtype : 'numberfield',
				fieldLabel : '服务半径(KM)',
				msgTarget : 'side',
				value : 3,
				minValue : 0,
				maxValue : 99
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
			}, {
				id : 'SERVICE_TIMES',
				name : 'SERVICE_TIMES',
				xtype : 'textareafield',
				fieldLabel : '服务时间',
				width : 500,
				colspan : 2,
				allowBlank : true
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
		var location = Ext.getCmp('LOCATION').getValue();
		if (location && location.length > 0) {
			var split = location.split(',');
			if (split.length > 1) {
				formData.fields.push({
					name : 'LONGITUDE',
					value : split[0]
				});
				formData.fields.push({
					name : 'LATITUDE',
					value : split[1]
				});
			}
		}
		formData.fields.push({
			name : 'IMAGE_URL',
			value : Ext.getCmp("IMAGE_URL").src
		});
		formData.fields.push({
			name : 'BUSINESS_LICENSE',
			value : Ext.getCmp("BUSINESS_LICENSE").src
		});
		return formData;
	},
	afterLoad : function(fieldValueMap) {
		var lon = fieldValueMap['LONGITUDE'];
		var lat = fieldValueMap['LATITUDE'];
		var location = '';
		if (lon && lat) {
			location = lon + ',' + lat;
		}
		Ext.getCmp("LOCATION").setValue(location);
		Ext.getCmp("IMAGE_URL").setSrc(
				fieldValueMap['IMAGE_URL'] == null ? ''
						: fieldValueMap['IMAGE_URL']);
		Ext.getCmp("BUSINESS_LICENSE").setSrc(
				fieldValueMap['BUSINESS_LICENSE'] == null ? ''
						: fieldValueMap['BUSINESS_LICENSE']);
	},
	clearValue : function() {
		var me = this;
		me.callParent();
		Ext.getCmp("IMAGE_URL").setSrc('');
		Ext.getCmp("BUSINESS_LICENSE").setSrc('');
	}
});
