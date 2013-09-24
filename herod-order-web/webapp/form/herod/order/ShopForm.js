/**
 * @class form.herod.order.ShopForm
 * @author Xiong Zhijun
 * @date 2011-7-4
 * @extends Ext.Component
 */
Ext.ns('form.herod.order');
Ext.define('form.herod.order.ShopForm', {
	extend : 'form.Form',
	layout : {
		type : 'table',
		columns : 3
	},
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
				id : 'IMAGE_URL',
				name : 'IMAGE_URL',
				fieldLabel : '图片',
				rowspan : 4,
				width : 200,
				height : 150,
				src : '',
				align : 'center',
				xtype : 'image'
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
				id : 'BUSINESS_LICENSE',
				name : 'BUSINESS_LICENSE',
				fieldLabel : '营业执照',
				rowspan : 4,
				width : 200,
				height : 150,
				src : '',
				align : 'center',
				xtype : 'image'
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
				id : 'STATUS',
				name : 'STATUS',
				xtype : 'shopstatuscombobox',
				fieldLabel : '商店状态',
				msgTarget : 'side',
			}, {
				id : 'COMMENT',
				name : 'COMMENT',
				xtype : 'textareafield',
				fieldLabel : '备注',
				width : 500,
				colspan : 2,
				allowBlank : true
			}, {
				id : 'SERVICE_TIMES',
				name : 'SERVICE_TIMES',
				xtype : 'grid',
				height : 150,
				colspan : 3,
				store : new Ext.data.JsonStore({
					fields : [ 'start', 'end' ],
					data : []
				}),
				viewConifg : {
					stripeRows : true,
					forceFir : true,
				},
				columnLines : true,
				tbar : [ {
					xtype : 'label',
					text : '营业时间'
				}, {
					xtype : 'tbfill'
				}, {
					xtype : 'button',
					text : '添加',
					handler : Ext.Function.bind(me.addServiceTime, me)
				} ],
				columns : [ {
					header : '开始时间',
					dataIndex : 'start',
					flex : 1
				}, {
					header : '结束时间',
					dataIndex : 'end',
					flex : 1
				}, {
					header : '操作',
					flex : 1,
					xtype : 'actioncolumn',
					items : [ {
						icon : 'bear/module/datasource/images/delete.gif',
						text : '删除',
						handler : function(grid, rowIndex, colIndex) {
							grid.getStore().removeAt(rowIndex);
						}
					} ]
				} ],
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
		formData.fields.push({
			name : 'SERVICE_TIMES',
			value : me.getServiceTimes()
		});
		return formData;
	},

	getServiceTimes : function() {
		var serviceTimes = [];
		var store = Ext.getCmp('SERVICE_TIMES').getStore();
		if (store.getCount() < 1) {
			return null;
		}
		for ( var i = 0; i < store.getCount(); i++) {
			var record = store.getAt(i);
			serviceTimes.push({
				'start' : record.get('start'),
				'end' : record.get('end'),
			});
		}
		return JSON.stringify(serviceTimes);
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

		var serviceTime = JSON.parse(fieldValueMap['SERVICE_TIMES']);
		for (index in serviceTime) {
			Ext.getCmp('SERVICE_TIMES').getStore().add({
				'start' : serviceTime[index]['start'],
				'end' : serviceTime[index]['end']
			});
		}
	},
	clearValue : function() {
		var me = this;
		me.callParent();
		Ext.getCmp("IMAGE_URL").setSrc('');
		Ext.getCmp("BUSINESS_LICENSE").setSrc('');
	},
	addServiceTime : function() {
		new Ext.Window({
			title : '服务时间',
			width : 300,
			height : 150,
			id : 'serviceTime',
			layout : 'fit',
			items : [ {
				xtype : 'form',
				frame : true,
				bodyPadding : 10,
				layout : 'anchor',
				items : [ {
					xtype : 'timefield',
					name : 'opentime',
					fieldLabel : '开始时间',
					minValue : '0:00 AM',
					maxValue : '11:50 PM',
					increment : 10,
					value : '8:00 AM',
					format : 'H:i',
					labelWidth : 65,
					width : 175,
					anchor : '100%'
				}, {
					xtype : 'timefield',
					name : 'closetime',
					fieldLabel : '结束时间',
					minValue : '0:00 AM',
					maxValue : '11:50 PM',
					increment : 10,
					value : '6:00 PM',
					format : 'H:i',
					labelWidth : 65,
					width : 175,
					anchor : '100%'
				} ],
				buttons : [ {
					text : '确定',
					handler : function() {
						var form = this.up('form').getForm();
						var starttime = form.findField('opentime').getValue();
						var endtime = form.findField('closetime').getValue();
						Ext.getCmp('SERVICE_TIMES').getStore().add({
							'start' : Ext.Date.format(starttime, 'H:i'),
							'end' : Ext.Date.format(endtime, 'H:i')
						});
						this.up('window').close();
					}

				} ]
			} ]
		}).show();
	}
});
