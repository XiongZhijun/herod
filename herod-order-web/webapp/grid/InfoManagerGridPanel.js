/**
 * @class grid.InfoManagerGridPanel
 * @extends bear.module.datasource.InfoManagerGridPanel
 * @author
 * @date 2011-10-17
 * @integrated true
 * @componentPath 宅人汇.宅人汇信息管理列表
 * @configClass bear.module.datasource.config.InfoManagerGridPanelConfigControl
 */
Ext.ns('grid');

Ext.require('bear.module.datasource.SearchPanelControl');
Ext.require('bear.module.datasource.InfoManagerGridPanelControl');
Ext.require('bear.control.ux.DWRProxy');
Ext.require('bear.module.datasource.Tools');

Ext.define('grid.InfoManagerGridPanel', {
	extend : 'bear.module.datasource.InfoManagerGridPanel',
	require : [ 'bear.module.datasource.SearchPanelControl',
			'bear.module.datasource.InfoManagerGridPanelControl' ],
	statics : {
		confirmWin : Ext.create('Ext.window.MessageBox', {
			buttonText : {
				yes : "确定",
				no : "取消",
				ok : '确定'
			}
		})
	},
	alias : [ 'widget.zrhinfomanagergridpanel' ],
	dwrFunction : OrderDataSetControl.search,

	showForm : function(config) {
		var me = this;
		me.form = Ext.create(this.gridPanel.formClass, config);
		me.formWindow = Ext.create('Ext.window.Window', {
			title : '编辑',
			layout : 'fit',
			items : me.form,
			closeAction : 'destroy',
			modal : 'true',
			listeners : {
				close : function() {
					me.gridPanel.store.load();
				}
			}
		});
		Ext.apply(me.form, config);
		me.form.loadNode();
		me.formWindow.show();
	},

	deleteSelectedRecords : function(event) {
		var me = this;
		var ids = [];
		for ( var i in event.records) {
			ids.push((event.records)[i].get('ID'));
		}
		OrderFormDataAccessService.deleteByIds(this.nodeTypeCode, ids, {
			callback : Ext.Function.bind(me.deleteCallback, me)
		});
	},

	deleteRecord : function(event) {
		var me = this;
		OrderFormDataAccessService.deleteById(this.nodeTypeCode,
				event.record.ID, {
					callback : Ext.Function.bind(me.deleteCallback, me)
				});
	},

	deleteCallback : function() {
		var me = this;
		me.self.confirmWin.show({
			title : "提示",
			msg : "删除数据成功！",
			icon : Ext.MessageBox.INFO,
			buttons : Ext.MessageBox.OK
		});
		me.gridPanel.store.load();
	}

});