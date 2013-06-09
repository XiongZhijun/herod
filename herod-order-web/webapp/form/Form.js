/**
 * 表单基类
 * 
 * @class scud.form.Form
 * @extends Ext.form.Panel
 * @author junbo_xu
 * @date 2012-4-18
 * 
 */
Ext.define('form.Form', {
    extend: 'Ext.form.Panel',
    updateDwrFunction: OrderFormDataAccessService.update,
    addDwrFunction: OrderFormDataAccessService.add,
    loadDwrFunction: OrderFormDataAccessService.getNodeById,
    bodyPadding: 5,
    layout : {
		type : 'table',
		columns : 2
	},
	defaultType : 'textfield',
	defaults : {
		labelAlign : 'right',
		margin : '5 5 5 5'
	},
    onSaved: function(){},
    initComponent: function() {
    	var me = this;
    	
    	if(!me.bbar) {
    		Ext.apply(me, {
    			fbar: ['->', {
	    			xtype: 'button',
	    			text: '保存',
	    			scale: 'medium',
	    			iconCls: 'save',
	    			handler: function() {
	    				me.save();
	    			}
	    		}, {
	    			xtype: 'button',
	    			text: '重置',
	    			scale: 'medium',
	    			iconCls: 'reset',
	    			handler: function() {
	    				me.getForm().reset();
	    			}
	    		}]
    		});
    	}
    	
    	me.callParent();
    },
    /**
     * 加载节点内容，并填充表单
     */
    loadNode: function() {
    	var me = this;
        
        if (me.nodeId) {
            me.loadDwrFunction(me.nodeTypeCode, me.nodeId, {
                callback: function(value) {
                	me.setValues(value.fieldValueMap);
                    me.afterLoad && me.afterLoad(value.fieldValueMap);
                },
                exceptionHandler: function(msg, exp) {
					Ext.Msg.show({
						title:'后台交互出错',
						msg: '获取节点信息出错',
						buttons: Ext.Msg.YES,
						icon: Ext.Msg.ERROR
					});
                }
            });
        } else {
        	me.clearValue();
        }
    },
    setValues: function(values) {
    	var me = this;

        function setVal(fieldId, val) {
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
        return this;
    },
    /**
     * 保存节点
     */
    save: function() {
        var me = this;
        if (!me.getForm().isValid()) {
            return;
        }
        
        var submitData = me.getFormData();
	        
        var dwrFunction = me.nodeId ? me.updateDwrFunction : me.addDwrFunction,
        	actionText = me.nodeId ? '更新' : '新增'
        
        dwrFunction(submitData, {
            callback: function() {
            	var win = me.up('window');
            	win && win.close();
                me.onSaved();
            },
            exceptionHandler: function(msg, exp) {
				Ext.Msg.show({
					title:'后台交互出错',
					msg:  actionText + '节点信息出错',
					buttons: Ext.Msg.YES,
					icon: Ext.Msg.ERROR
				});
            }
        });
    },
    /**
     * 获取表单数据，并转换为平台需要的表单数据格式
     * @return {}
     */
    getFormData: function() {
        var me = this,
        	formValueMap = me.getFormValues(),
        	fieldValues = [];
        
        for (var fieldCode in formValueMap) {
        	fieldValues.push({
        		name: fieldCode,
        		value: formValueMap[fieldCode],
        		type: this.form.findField(fieldCode).type || null
        	});
        }
        
        if(me.nodeId) {
        	fieldValues.push({
        		name: 'ID',
        		type: 'long',
        		value: me.nodeId
        	});
        }
        
        return {
            nodeTypeCode: me.nodeTypeCode,
            fields: fieldValues
        }
    },
    getFormValues: function() {
    	return this.getForm().getValues(false, false, true, true);
    },
    clearValue: function() {
    	this.form.getFields().each(function(field) {
    		if(Ext.isFunction(field.setValue)) {
    			field.setValue('');
    			field.resetOriginalValue();
    		}
        });
    }
});