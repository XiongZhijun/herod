/**
 * @class form.herod.DeliveryWorkerForm
 * @extends bear.control.ux.Sheet
 * @designControl
 */
Ext.define('form.herod.DeliveryWorkerForm', {
    requires: ['bear.control.ux.Sheet','bear.control.ux.DWRProxyComboBox', 'bear.designer.form.config.templates.SimpleDependency'],
    extend: 'bear.control.ux.Sheet',
     /**
     * @prpt nodeTypeCode
     * @readOnly
     */
    nodeTypeCode:'ZRH_AGENT_DELIVERY_WORKER',
    nodeTypeName : '代理商配送人员',
    nodeId:0,
    updateDwrFunction : FormDataAccessService.update,
	addDwrFunction : FormDataAccessService.add,
    loadDwrFunction : FormDataAccessService.getNodeById,
    width : 530, 
    isUpdate : false, 
    layoutHtml:'<table width="100%" cellspacing="0" cellpadding="0" border="0" class="fpitableCls"><tr><td class="formFiledNameCls">姓名</td><td class="formFieldControlCls">$ctl("NAME")</td><td class="formFiledNameCls">电话</td><td class="formFieldControlCls">$ctl("PHONE")</td></tr><tr><td class="formFiledNameCls">密码</td><td class="formFieldControlCls">$ctl("PASSWORD")</td><td class="formFiledNameCls">所属代理商</td><td class="formFieldControlCls">$ctl("AGENT_ID")</td></tr><tr><td class="formFiledNameCls">状态标记</td><td class="formFieldControlCls">$ctl("FLAG")</td><td></td><td></td></tr><tr><td></td><td></td><td></td><td><table><tr><td>$ctl("saveButton")</td><td>$ctl("resetButton")</td></tr></td></tr></table>',
    items:{
	saveButton:{
	    xtype:'button',
	    text:'保存',
	    margin:'0 10 0 10',
        handler:function(){
        var me = this;
        me.owner.saveNode();
        }
	}, 
	resetButton:{
	    xtype:'button',
	    text:'重置',
	    margin:'0 10 0 10',
	    handler:function(){
	    var sheet = this.owner;
	    if(sheet.defaultValue){
	    sheet.setValue(sheet.defaultValue);
	    }else{
	    	var values = {};
	    	for(var code in sheet.items){
	    		values[code]="";
	    	}
	    	sheet.setValue(values);
	    }
	    }
	},
	ID:{xtype:'textfield'},
NAME:{xtype:'textfield'},
PHONE:{xtype:'textfield'},
PASSWORD:{xtype:'textfield'},
AGENT_ID:{xtype:'dwrproxycombobox',
nodeTypeCode:'ZRH_AGENT'},
FLAG:{xtype:'textfield'}
    },
    eventSettings:{},
    
    /**
     * @method setNodeId
     */
    setNodeId:function(id){
	    this.nodeId = id;
    },
    
    /**
     * @method getNodeId
     */
    getNodeId:function(){
	    return this.nodeId;
    },
    
    /**
	 * @method loadNode
	 */
	loadNode : function() {
		var me = this;
		me.clearValue();
		me.isUpdate = false;
		if (me.nodeId) {
			me.isUpdate = true;
			me.loadDwrFunction(me.nodeTypeCode, me.nodeId, {
						callback : function(value) {
							console.log(value);
							me.defaultValue = value.fieldValueMap;
							me.setValue(value.fieldValueMap);
						},
						exceptionHandler : function(msg, exp) {
							Ext.Msg.alert('警告', '后台操作异常:' + msg + exp);
						}
					});
		}
	},

	clearValue : function() {
		var me = this;
		for (var fieldCode in me.controlMap) {
			var item = me.controlMap[fieldCode];
			if (item) {
				if (Ext.isFunction(item.setValue)) {
					item.setValue('');
				}
			}
		}
		
		me.defaultValue = {};
	},
    
    /**
     * @method saveNode
     */
    saveNode : function() {
		var me = this;
		if (!me.isValid()) {
			return;
		}
		var formData = me.getFormData();
		var dwrFunction = me.isUpdate
				? me.updateDwrFunction
				: me.addDwrFunction;
		var confirmWin = Ext.create('Ext.window.MessageBox', {
					buttonText : {
						ok : '确定'
					}
				});
		var msg = "新增" + me.nodeTypeName + "成功！";
		if (me.isUpdate) {
			msg = "更新" + me.nodeTypeName + "成功！";
		}
		dwrFunction(formData, {
					callback : function() {
						confirmWin.show({
									title : "提示",
									msg : msg,
									icon : Ext.MessageBox.INFO,
									buttons : Ext.MessageBox.OK
								});
						me.up('window').close();
					}
				});
	},

	getFormData : function() {
		var me = this;
		var fieldValues = [];
		for (var fieldCode in me.controlMap) {
			var item = me.controlMap[fieldCode];
			if (item && Ext.isFunction(item.getValue)) {
				if (fieldCode == 'ID' && !me.isUpdate) {
					continue;
				}
				fieldValues.push({
							name : fieldCode,
							type : item.type || item.xtype,
							value : item.getValue() === "" ? null : item.getValue() 
						});
			}
		}
		return {
			nodeTypeCode : me.nodeTypeCode,
			fields : fieldValues
		}
	},
    
    /**
     * @method deleteNode
     */
	deleteNode: function(){
		var me = this;
		FormDesignerServiceUtils.deleteNode(me.nodeTypeCode, me.nodeId, {
			callback: function(value) {
				if(value){
					Ext.Msg.alert('提示', '删除成功！');
				} else{
					Ext.Msg.alert('提示', '删除失败！');
				}
			},
			exceptionHandler: function(msg, exp) {
				Ext.Msg.alert('警告', '后台操作异常:' + msg + exp);
			}
		});
	},	
    
    /**
     *@method isValid
     */
    isValid: function() {
        var me = this;
        for (var code in me.controlMap) {
            var cmp = me.controlMap[code];
            if (Ext.isFunction(cmp.isValid)) {
                if (!cmp.isValid()) return false;
            }
        }
        return true;
    },
    
	listeners:{
		afterrender:function(comp){
			for(var ctl in comp.controlMap){
				comp.controlMap[ctl].owner = comp;
			}
			var el = comp.getEl();
			var table = el.dom.firstChild;
			var rows = table.rows;
			for(i=0;i<rows.length-1;i++){
				rows[i].setAttribute('bgcolor','#EEEEEE');
			}			
		}
	}
});