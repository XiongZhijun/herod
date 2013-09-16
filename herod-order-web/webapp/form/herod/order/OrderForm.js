/**
 * @class form.herod.order.OrderForm
 * @extends bear.control.ux.Sheet
 * @designControl
 */
Ext.define('form.herod.order.OrderForm', {
	extend : 'Ext.form.Panel',
	bodyPadding : 5,
	layout : {
		type : 'table',
		columns : 2
	},
	defaultType : 'textfield',
	defaults : {
		labelAlign : 'right',
		margin : '5 5 5 5',
		labelWidth : 70,
		msgTarget : 'side',
		allowBlank : true,
		readOnly : true
	},
	initComponent : function() {
		var me = this;
		Ext.apply(me, {
			items : [ {
				id : 'serialNumber',
				name : 'serialNumber',
				fieldLabel : '流水号',
			}, {
				id : 'status',
				name : 'status',
				xtype : 'orderstatuscombobox',
				fieldLabel : '订单状态',
			}, {
				id : 'buyerName',
				name : 'buyerName',
				fieldLabel : '买家姓名',
			}, {
				id : 'buyerPhone',
				name : 'buyerPhone',
				fieldLabel : '买家电话',
			}, {
				id : 'deliveryAddress',
				name : 'deliveryAddress',
				fieldLabel : '送货地址',
			}, {
				id : 'shopName',
				name : 'shopName',
				fieldLabel : '商店名称',
			}, {
				id : 'shopPhone',
				name : 'shopPhone',
				fieldLabel : '商店电话',
			}, {
				id : 'submitTime',
				name : 'submitTime',
				fieldLabel : '下单时间',
				xtype : 'datefield',
				format : 'Y-m-d H:i'
			}, {
				id : 'completeTime',
				name : 'completeTime',
				fieldLabel : '完成时间',
				xtype : 'datefield',
				format : 'Y-m-d H:i'
			}, {
				id : 'costOfRunErrands',
				name : 'costOfRunErrands',
				fieldLabel : '配送费',
			}, {
				id : 'shopCostOfRunErrands',
				name : 'shopCostOfRunErrands',
				fieldLabel : '商店配送费',
			}, {
				id : 'shopMinChargeForFreeDelivery',
				name : 'shopMinChargeForFreeDelivery',
				fieldLabel : '商店免费起送额',
			}, {
				id : 'comment',
				name : 'comment',
				xtype : 'textareafield',
				fieldLabel : '备注',
				width : 500,
				height : 50,
				colspan : 2,
				allowBlank : true
			}, {
				id : 'orderItemsGrid',
				name : 'orderItemsGrid',
				fieldLabel : '订单项',
				xtype : 'grid',
				colspan : 2,
				store : {
					autolaod : true,
					fields : [ {
						name : 'goodsName',
						mapping : 'goodsName',
						type : 'string'
					}, {
						name : 'sellingPrice',
						mapping : 'sellingPrice',
						type : 'string'
					}, {
						name : 'supplyPrice',
						mapping : 'supplyPrice',
						type : 'string'
					}, {
						name : 'quantity',
						mapping : 'quantity',
						type : 'string'
					} ],
					reader : {
						type : 'json'
					}
				},
				columns : [ {
					header : '商品',
					dataIndex : 'goodsName',
					flex : 1
				}, {
					header : '销售单价',
					dataIndex : 'sellingPrice'
				}, {
					header : '供货价',
					dataIndex : 'supplyPrice'
				}, {
					header : '数量',
					dataIndex : 'quantity'
				} ],
				height : 150,
				width : 500,
			} ]
		});

		me.callParent();
	},
	loadNode : function() {
		var me = this;

		if (me.nodeId) {
			OrderAccessService.findOrderBySerialNumber(me.record.SERIAL_NUMBER,
					{
						callback : function(value) {
							me.setValues(value);
						},
						exceptionHandler : function(msg, exp) {
							Ext.Msg.show({
								title : '后台交互出错',
								msg : '获取订单信息出错',
								buttons : Ext.Msg.YES,
								icon : Ext.Msg.ERROR
							});
						}
					});
		} else {
		}
	},
	setValues : function(values) {
		var me = this;

		function setVal(fieldId, val) {
			if (fieldId == 'deliveryAddress') {
				val = val ? val.address : '';
			}
			var field = me.form.findField(fieldId);
			if (field) {
				field.value = val;
				field.initValue();
			}
		}

		if (Ext.isArray(values)) {
			Ext.each(values, function(val) {
				setVal(val.id, val.value);
			});
		} else {
			Ext.iterate(values, setVal);
		}
		var orderItemsGrid = Ext.getCmp('orderItemsGrid');
		orderItemsGrid.store.loadData(values.orderItems);

		return this;
	}
});