/**
 * @class form.herod.DeliveryWorkerForm
 * @extends bear.control.ux.Sheet
 * @designControl
 */
Ext.define('form.herod.order.DeliveryWorkerForm', {
	extend : 'form.Form',
	defaults : {
		labelAlign : 'right',
		margin : '5 5 5 5',
		labelWidth : 70,
		msgTarget : 'side',
		allowBlank : false,
	},
	initComponent : function() {
		var me = this;
		Ext.apply(me, {
			items : [ {
				id : 'NAME',
				name : 'NAME',
				fieldLabel : '姓名',
			}, {
				id : 'PHONE',
				name : 'PHONE',
				fieldLabel : '手机号',
				regex : /^[0-9]+$/,
				regexText : '请输入合法电话号码（0-9）',
			}, {
				id : 'PASSWORD',
				name : 'PASSWORD',
				fieldLabel : '登陆密码',
				maxLength : 24,
				inputType : 'password',
			}, {
				id : 'ID_NUMBER',
				name : 'ID_NUMBER',
				fieldLabel : '身份证号',
				regex : /^[0-9a-zA-Z]+$/,
				regexText : '请输入合法身份证号',
				maxLength : 18,
			}, {
				xtype : 'fieldcontainer',
				fieldLabel : '状态',
				defaultType : 'radiofield',
				defaults : {
					flex : 1
				},
				width : 200,
				layout : 'hbox',
				items : [ {
					boxLabel : '在职',
					name : 'FLAG',
					inputValue : '1',
					id : 'radio1'
				}, {
					boxLabel : '离职',
					name : 'FLAG',
					inputValue : '2',
					id : 'radio2'
				} ]
			} ]
		});

		me.callParent();
	},
	afterLoad : function(fieldValueMap) {
		var me = this;
		me.callParent();
		if (fieldValueMap['FLAG'] == 1) {
			var radio = Ext.getCmp('radio1');
			radio.setValue(true);
		} else {
			var radio = Ext.getCmp('radio2');
			radio.setValue(true);
		}

	},
	clearValue : function() {
		var me = this;
		me.callParent();
		var radio = Ext.getCmp('radio1');
		radio.setValue(true);
	}
});