/**
 * @class form.herod.AgentForm
 * @extends bear.control.ux.Sheet
 * @designControl
 */
Ext.define('form.herod.order.AgentForm', {
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
				fieldLabel : '名称',
			}, {
				id : 'BUSINESS_LICENSE',
				name : 'BUSINESS_LICENSE',
				fieldLabel : '营业执照',
				rowspan : 5,
				width : 230,
				height : 150,
				align : 'right',
				xtype : 'image',
			}, {
				id : 'LEGAL_REPRESENTATIVE',
				name : 'LEGAL_REPRESENTATIVE',
				fieldLabel : '法人代表',
			}, {
				xtype : 'form',
				border : false,
				defaults : {
					labelAlign : 'right'
				},
				items : [ {
					fieldLabel : '图片',
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
				id : 'ORGANIZATION_CODE',
				name : 'ORGANIZATION_CODE',
				fieldLabel : '组织机构代码',
				allowBlank : true,
			}, {
				id : 'LINKMAN',
				name : 'LINKMAN',
				fieldLabel : '联系人',
			}, {
				id : 'CONTACT_NUMBER',
				name : 'CONTACT_NUMBER',
				fieldLabel : '联系电话',
			}, {
				id : 'CONTACT_ADDRESS',
				name : 'CONTACT_ADDRESS',
				fieldLabel : '联系地址',
			}, {
				id : 'BANK_NAME',
				name : 'BANK_NAME',
				fieldLabel : '开户行',
				allowBlank : false,
			}, {
				id : 'BANK_ACCOUNT',
				name : 'BANK_ACCOUNT',
				fieldLabel : '银行账号',
				allowBlank : false,
			}, {
				id : 'ADMIN_ACCOUNT',
				name : 'ADMIN_ACCOUNT',
				fieldLabel : '管理员账号',
				allowBlank : false,
			} ]
		});

		me.callParent();
	},
	getFormData : function() {
		var me = this;
		var formData = me.callParent();
		formData.fields.push({
			name : 'BUSINESS_LICENSE',
			value : Ext.getCmp("BUSINESS_LICENSE").src
		});
		return formData;
	},
	afterLoad : function(fieldValueMap) {
		Ext.getCmp("BUSINESS_LICENSE").setSrc(
				fieldValueMap['BUSINESS_LICENSE'] == null ? ''
						: fieldValueMap['BUSINESS_LICENSE']);
	},
	clearValue : function() {
		var me = this;
		me.callParent();
		Ext.getCmp("BUSINESS_LICENSE").setSrc('');
	}
});