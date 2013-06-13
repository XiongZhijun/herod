/**
 * @class bear.module.datasource.InfoManagerGridPanel
 * @extends Ext.panel.Panel
 * @author
 * @date 2011-10-17
 * @integrated true
 * @componentPath 信息服务工具.信息管理列表
 * @configClass bear.module.datasource.config.InfoManagerGridPanelConfigControl
 */
Ext.ns('bear.module.datasource');

Ext.require('bear.module.datasource.SearchPanelControl');
Ext.require('bear.module.datasource.InfoManagerGridPanelControl');
Ext.require('bear.control.ux.DWRProxy');
Ext.require('bear.module.datasource.Tools');

Ext.define('bear.module.datasource.InfoManagerGridPanel', {
	extend : 'Ext.panel.Panel',
	require : [ 'bear.module.datasource.SearchPanelControl',
			'bear.module.datasource.InfoManagerGridPanelControl' ],

	alias : [ 'widget.infomanagergridpanel' ],
	statics : {
		confirmWin : Ext.create('Ext.window.MessageBox', {
			buttonText : {
				yes : "确定",
				no : "取消",
				ok : '确定'
			}
		})
	},
	addflag : false,
	layout : 'border',
	searchPanel : null,
	gridPanel : null,

	selectFields : [],
	condition : null,
	basicCondition : null,
	pageSize : 20,
	dwrFunction : OrderDataSetControl.search,
	dataSetContext : null,
	dataSetCode : null,
	dataSetContextLocation : 'tableContext',
	nodeTypeCode : null,
	orderBy : null,
	formClass : null,
	autoScroll : true,
	forceFit : true,

	constructor : function(config) {
		var me = this;
		config.dataSetConfig.pageSize = bear.module.datasource.Tools
				.parseInt(config.dataSetConfig.pageSize);
		me.loadRequiredClass(config.dataSetConfig.requireControls);
		Ext.apply(me, config.dataSetConfig);
		bear.module.datasource.Tools.preHandleDefinitions([ me ]);

		bear.module.datasource.Tools.preHandleDefinitions([ config ]);
		this.callParent([ config ]);
	},

	loadRequiredClass : function(classes) {
		if (classes) {
			var requireClasses = classes.trim().split(',');
			for ( var i in requireClasses) {
				Ext.syncRequire(requireClasses[i]);
			}
		}
	},

	initComponent : function() {
		var me = this;
		me.selectFields = [];
		for ( var index in me.gridPanelConfig.columns) {
			me.selectFields.push(me.gridPanelConfig.columns[index].dataIndex);
		}
		me.dwrFunction = me.dwrFunction || me.dwrFunction;

		me.items = [];
		me.searchPanel = me.createSearchPanel(me);
		if (me.searchPanel) {
			me.items.push(me.searchPanel);
		}
		me.gridPanel = me.createGridPanel(me);
		if (me.gridPanel) {
			me.items.push(me.gridPanel);
		}
		me.condition = me.basicCondition;
		me.callParent(arguments);
	},

	createSearchPanel : function() {
		var me = this;
		var searchPanelConfig = me.searchPanelConfig;
		if (searchPanelConfig.items == null
				|| searchPanelConfig.items.length == 0) {
			return null;
		}
		searchPanelConfig.listeners = {
			search : Ext.Function.bind(me.search, me)
		};
		Ext.apply(searchPanelConfig, {
			region : 'north'
		})
		return Ext.create('bear.module.datasource.SearchPanelControl',
				searchPanelConfig);
	},

	createGridPanel : function() {
		var me = this;
		var gridPanelConfig = me.gridPanelConfig;
		if (!gridPanelConfig.listeners) {
			gridPanelConfig.listeners = {};
		}
		gridPanelConfig.listeners = Ext.apply(gridPanelConfig.listeners, {
			editRecord : Ext.Function.bind(me.editRecord, me),
			addRecord : Ext.Function.bind(me.addRecord, me),
			deleteSelectedRecords : Ext.Function.bind(me.deleteSelectedRecords,
					me),
			deleteRecord : Ext.Function.bind(me.deleteRecord, me),
			beforeitemclick : function(view, record, item, index, e) {
				me.fireEvent('nodeclick', {
					control : me,
					record : record.data
				});
			}
		});

		gridPanelConfig.store = me.createStore();
		Ext.apply(gridPanelConfig, {
			region : 'center'
		})
		return Ext.create('bear.module.datasource.InfoManagerGridPanelControl',
				gridPanelConfig);
	},

	editRecord : function(event) {
		var me = this;
		var config = {
			nodeTypeCode : this.nodeTypeCode,
			nodeId : event.record.ID || event.record.id,
			record : event.record,
			gridPanel : me
		};
		this.showForm(config);
	},

	addRecord : function(event) {
		var me = this;
		this.showForm({
			nodeTypeCode : this.nodeTypeCode,
			nodeId : null,
			gridPanel : me
		});
	},

	showForm : function(config) {
		var me = this;
		if (me.formWindow == null) {
			me.form = Ext.create(this.gridPanel.formClass, config);
			me.formWindow = Ext.create('Ext.window.Window', {
				title : '编辑',
				layout : 'fit',
				items : me.form,
				closeAction : 'hide',
				listeners : {
					close : function() {
						me.gridPanel.store.load();
					}
				}
			});
		}
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
		FormDataAccessService.deleteByIds(this.nodeTypeCode, ids, {
			callback : Ext.Function.bind(me.deleteCallback, me)
		});
	},

	deleteRecord : function(event) {
		var me = this;
		FormDataAccessService.deleteById(this.nodeTypeCode, event.record.ID, {
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
	},

	search : function() {
		var me = this;
		me.loadCondition();
		me.gridPanel.store.load();
	},
	loadCondition : function() {
		var me = this;
		if (me.searchPanel) {
			me.condition = me.searchPanel.getCondition();
		}
		if (me.basicCondition) {
			if (me.condition == null) {
				me.condition = me.basicCondition;
				return;
			}
			me.condition = {
				op : 'and',
				conditions : [ me.condition, me.basicCondition ]
			}
		}
	},
	addCondition : function(condition) {
		if (condition == null) {
			return;
		}
		if (this.basicCondition == null) {
			this.basicCondition = {
				op : 'and',
				conditions : []
			};
		} else if (this.basicCondition.op != 'and') {
			this.basicCondition = {
				op : 'and',
				conditions : [ this.basicCondition ]
			};
		}

		this.basicCondition.conditions.push[condition];
	},

	/**
	 * 创建列表的datastore。
	 * 
	 * @return {}
	 */
	createStore : function() {
		var me = this;
		return Ext.create('Ext.data.JsonStore', {
			fields : me.selectFields,
			autoLoad : true,
			pageSize : me.pageSize,
			proxy : new bear.control.ux.DWRProxy({
				dwrFunction : me.dwrFunction,
				paramCnt : 7,
				listeners : {
					beforerequest : function(proxy, operation) {
						me.loadCondition();
						proxy.params = [ me.dataSetContextLocation,
								me.dataSetCode, me.selectFields, me.condition,
								me.orderBy, operation.start,
								operation.limit + operation.start ];
					}
				},
				reader : {
					root : 'dataList',
					totalProperty : 'totalCount'
				}
			})
		});
	}

});