数据集定义：
AGENT_ORDER_MANGER_LIST_VIEW

SELECT O.ID, O.SERIAL_NUMBER, O.DELIVERY_ADDRESS,O.AGENT_ID, S.NAME SHOP_NAME,S.ID SHOP_ID, O.SUBMIT_TIME,O.COMPLETE_TIME,O.BUYER_PHONE ,O.STATUS ,O.BUYER_NAME,S.CONTACT_NUMBER FROM ZRH_ORDER O LEFT JOIN ZRH_SHOP S ON O.SHOP_ID = S.ID

菜单管理：
order_manager
订单管理
代理商订单管理列表配置
agent_order_manager_grid_config
bear.module.datasource.InfoManagerGridPanel
{"dataSetConfig":{"dataSetContextLocation":"basicDataSetContext","dataSetCode":"AGENT_ORDER_MANGER_LIST_VIEW","nodeTypeCode":"AGENT_ORDER_MANGER_LIST_VIEW","pageSize":"20","advancedConfig":"","requireControls":"","dataSetContext":{"code":"AGENT_ORDER_MANGER_LIST_VIEW"}},"gridPanelConfig":{"advancedConfig":"","formClass":"form.herod.order.OrderForm","addable":false,"deletable":false,"deletselectedable":false,"updatable":true,"checkboxable":false,"columns":[{"header":"ID","dataIndex":"ID","xtype":null,"format":null,"hidden":true,"advancedConfig":"","width":""},{"header":"\u6d41\u6c34\u53f7","dataIndex":"SERIAL_NUMBER","xtype":null,"format":null,"hidden":false,"advancedConfig":"","width":""},{"header":"\u8ba2\u5355\u72b6\u6001","dataIndex":"STATUS","xtype":"orderstatuscolumn","format":null,"hidden":"","advancedConfig":"","width":""},{"header":"\u5546\u5e97","dataIndex":"SHOP_NAME","xtype":null,"format":null,"hidden":"","advancedConfig":"","width":""},{"header":"\u5546\u5e97\u8054\u7cfb\u7535\u8bdd","dataIndex":"CONTACT_NUMBER","xtype":null,"format":null,"hidden":"","advancedConfig":"","width":""},{"header":"\u4e70\u5bb6\u9001\u8d27\u5730\u5740","dataIndex":"DELIVERY_ADDRESS","xtype":null,"format":null,"hidden":"","advancedConfig":"","width":""},{"header":"\u4e70\u5bb6\u8054\u7cfb\u7535\u8bdd","dataIndex":"BUYER_PHONE","xtype":null,"format":"","hidden":"","advancedConfig":"","width":""},{"header":"\u4e0b\u5355\u65f6\u95f4","dataIndex":"SUBMIT_TIME","xtype":"datecolumn","format":"Y-m-d H:i","hidden":false,"advancedConfig":"","width":null}],"toolbarItems":[],"operationItems":[]},"searchPanelConfig":{"columnCount":11,"advancedConfig":"","items":[{"fieldLabel":"\u6d41\u6c34\u53f7","name":"SERIAL_NUMBER","op":"like","format":null,"xtype":"textfield","colspan":1,"advancedConfig":"","type":"string"},{"fieldLabel":"\u8ba2\u5355\u72b6\u6001","name":"STATUS","op":"=","format":null,"xtype":"orderstatuscombobox","colspan":1,"advancedConfig":"","type":"string"},{"fieldLabel":"\u6240\u5c5e\u5546\u5e97","name":"SHOP_ID","op":"=","format":null,"xtype":"datasetcombobox","colspan":1,"advancedConfig":"{\ncontextLocation:'basicDataSetContext',\ndataSetCode:'SHOP_VIEW',\ndwrFunction:OrderDataSetControl.search,\ncontainAllItem:false\n}","type":"string"},{"fieldLabel":"\u63d0\u4ea4\u65f6\u95f4","name":"SUBMIT_TIME","op":">=","format":null,"xtype":"datetimefield","colspan":1,"advancedConfig":"","type":"date"},{"fieldLabel":"\u5230","propertyName":"","op":"<=","xtype":"datetimefield","type":"date","format":null,"colspan":1,"name":"SUBMIT_TIME","advancedConfig":""}]}}

权限
订单查看权限
simpleDefine
AGENT_ORDER_MANGER_LIST_VIEW:VIEW