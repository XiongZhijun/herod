/**
 * @class form.herod.order.ShopTypeForm
 * @author Xiong Zhijun
 * @date 2011-7-4
 * @extends Ext.Component
 */
Ext.ns('bear.control.ux');
// 树型导航器
Ext.define('form.herod.order.ShopTypeForm', {
	extend : 'Ext.form.Panel',
	bodyPadding : 10,
	layout : {
		type : 'table',
		columns : 2
	},
	defaultType : 'textfield',
	defaults : {
		labelAlign : 'right',
		margin : '5 5 5 5'
	},
	items : [ {
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
	} ],
	buttons : [ {
		text : '保存',
		formBind : true,
		disabled : true,
		handler : function() {
			var form = this.up('form').getForm();
			form.findField("shopCode").enable(true);
			if (form.isValid()) {
				form.submit({
					url : '/rest/ProductOrAgentService/addOrUpdateAgentNode/'
							+ form.id,
					method : 'post',
					success : function() {
						Ext.Msg.alert("提示", "添加/修改成功");
						Ext.getCmp(form.id).up("window").close();
						Ext.getCmp('agentEdit').getStore().load();
					}

				})
			}
		}
	} ],
	loadNode : function() {
	}
});
