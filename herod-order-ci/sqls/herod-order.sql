/*
SQLyog Community Edition- MySQL GUI v6.07
Host - 5.0.19-nt : Database - herod-order
*********************************************************************
Server version : 5.0.19-nt
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `herod-order`;

USE `herod-order`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `action` */

DROP TABLE IF EXISTS `action`;

CREATE TABLE `action` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `CODE` varchar(255) NOT NULL,
  `PINYIN` varchar(255) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `CODE` (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `action` */

insert  into `action`(`ID`,`NAME`,`CODE`,`PINYIN`) values (1,'增加','ADD',NULL),(2,'删除','DELETE',NULL),(4,'更新','UPDATE',NULL),(10000,'查询','VIEW',NULL);

/*Table structure for table `appsystem` */

DROP TABLE IF EXISTS `appsystem`;

CREATE TABLE `appsystem` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `CODE` varchar(255) default NULL,
  `SYSTEMIMG` varchar(255) default NULL,
  `BACKGROUNDCOLOR` varchar(255) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `TITLEOFFSET` int(11) default NULL,
  `MENULAYOUT` varchar(255) default NULL,
  `PINYIN` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `appsystem` */

/*Table structure for table `component` */

DROP TABLE IF EXISTS `component`;

CREATE TABLE `component` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL default '',
  `CODE` varchar(255) default '',
  `FUNCCLASS` varchar(255) default '',
  `PARAMS` text,
  `DESCRIPTION` varchar(255) default '',
  `CONFIGCLASS` varchar(255) default NULL,
  `PINYIN` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `component` */

insert  into `component`(`ID`,`NAME`,`CODE`,`FUNCCLASS`,`PARAMS`,`DESCRIPTION`,`CONFIGCLASS`,`PINYIN`) values (10000,'数据集信息列表','dataSetInfoList','bear.module.datasource.InfoManagerGridPanel','{\"dataSetConfig\":{\"dataSetContextLocation\":\"tableContext\",\"dataSetCode\":\"DATA_SET\",\"nodeTypeCode\":\"DATA_SET\",\"pageSize\":\"20\",\"advancedConfig\":\"\",\"dataSetContext\":{\"code\":\"DATA_SET\"}},\"gridPanelConfig\":{\"advancedConfig\":\"\",\"formClass\":\"bear.module.datasource.dataset.DataSetForm\",\"addable\":true,\"deletable\":true,\"deletselectedable\":true,\"updatable\":true,\"checkboxable\":true,\"columns\":[{\"header\":\"ID\",\"dataIndex\":\"ID\",\"xtype\":null,\"format\":null,\"hidden\":true,\"advancedConfig\":\"\"},{\"header\":\"\\u540d\\u79f0\",\"dataIndex\":\"NAME\",\"xtype\":null,\"format\":null,\"hidden\":false,\"advancedConfig\":\"\"},{\"header\":\"\\u7f16\\u7801\",\"dataIndex\":\"CODE\",\"xtype\":null,\"format\":null,\"hidden\":\"\",\"advancedConfig\":\"\"}],\"toolbarItems\":[],\"operationItems\":[]},\"searchPanelConfig\":{\"columnCount\":5,\"items\":[{\"fieldLabel\":\"\\u540d\\u79f0\",\"propertyName\":\"\",\"op\":\"like\",\"xtype\":\"textfield\",\"type\":\"string\",\"format\":null,\"colspan\":1,\"name\":\"NAME\",\"advancedConfig\":\"\"},{\"fieldLabel\":\"\\u7f16\\u7801\",\"propertyName\":\"\",\"op\":\"like\",\"xtype\":\"textfield\",\"type\":\"string\",\"format\":null,\"colspan\":1,\"name\":\"CODE\",\"advancedConfig\":\"\"}]}}','',NULL,NULL),(10001,'商家类型管理列表配置','shop_type_manager_grid_config','bear.module.datasource.InfoManagerGridPanel','{\"dataSetConfig\":{\"dataSetContextLocation\":\"tableContext\",\"dataSetCode\":\"ZRH_SHOP_TYPE\",\"nodeTypeCode\":\"ZRH_SHOP_TYPE\",\"pageSize\":\"25\",\"advancedConfig\":\"\",\"requireControls\":\"\",\"dataSetContext\":{\"code\":\"ZRH_SHOP_TYPE\"}},\"gridPanelConfig\":{\"advancedConfig\":\"\",\"formClass\":\"form.herod.order.ShopTypeForm\",\"addable\":true,\"deletable\":true,\"deletselectedable\":true,\"updatable\":true,\"checkboxable\":true,\"columns\":[{\"header\":\"ID\",\"dataIndex\":\"ID\",\"xtype\":null,\"format\":null,\"hidden\":true,\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u540d\\u79f0\",\"dataIndex\":\"NAME\",\"xtype\":null,\"format\":null,\"hidden\":false,\"advancedConfig\":\"\",\"width\":\"\"}],\"toolbarItems\":[],\"operationItems\":[]},\"searchPanelConfig\":{\"columnCount\":5,\"advancedConfig\":\"\",\"items\":[{\"fieldLabel\":\"\\u5546\\u5e97\\u7c7b\\u578b\\u540d\\u79f0\",\"name\":\"NAME\",\"op\":\"like\",\"format\":null,\"xtype\":\"textfield\",\"colspan\":1,\"advancedConfig\":\"\",\"type\":\"string\"}]}}','',NULL,NULL),(10002,'代理商管理列表配置','agent_manger_grid_config','bear.module.datasource.InfoManagerGridPanel','{\"dataSetConfig\":{\"dataSetContextLocation\":\"tableContext\",\"dataSetCode\":\"ZRH_AGENT\",\"nodeTypeCode\":\"ZRH_AGENT\",\"pageSize\":\"25\",\"advancedConfig\":\"\",\"requireControls\":\"\",\"dataSetContext\":{\"code\":\"ZRH_AGENT\"}},\"gridPanelConfig\":{\"advancedConfig\":\"\",\"formClass\":\"form.herod.order.AgentForm\",\"addable\":true,\"deletable\":true,\"deletselectedable\":true,\"updatable\":true,\"checkboxable\":true,\"columns\":[{\"header\":\"ID\",\"dataIndex\":\"ID\",\"xtype\":null,\"format\":null,\"hidden\":true,\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u540d\\u79f0\",\"dataIndex\":\"NAME\",\"xtype\":null,\"format\":null,\"hidden\":false,\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u8054\\u7cfb\\u4eba\",\"dataIndex\":\"LINKMAN\",\"xtype\":null,\"format\":null,\"hidden\":\"\",\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u8054\\u7cfb\\u7535\\u8bdd\",\"dataIndex\":\"CONTACT_NUMBER\",\"xtype\":null,\"format\":null,\"hidden\":\"\",\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u7ba1\\u7406\\u5458\\u8d26\\u53f7\",\"dataIndex\":\"ADMIN_ACCOUNT\",\"xtype\":null,\"format\":null,\"hidden\":\"\",\"advancedConfig\":\"\",\"width\":\"\"}],\"toolbarItems\":[],\"operationItems\":[]},\"searchPanelConfig\":{\"columnCount\":5,\"advancedConfig\":\"\",\"items\":[{\"fieldLabel\":\"\\u540d\\u79f0\",\"name\":\"NAME\",\"op\":\"like\",\"format\":null,\"xtype\":\"textfield\",\"colspan\":1,\"advancedConfig\":\"\",\"type\":\"string\"}]}}','',NULL,NULL),(10003,'商家管理列表配置','shop_manger_grid_config','bear.module.datasource.InfoManagerGridPanel','{\"dataSetConfig\":{\"dataSetContextLocation\":\"basicDataSetContext\",\"dataSetCode\":\"SHOP_VIEW\",\"nodeTypeCode\":\"ZRH_SHOP\",\"pageSize\":\"25\",\"advancedConfig\":\"\",\"requireControls\":\"\",\"dataSetContext\":{\"code\":\"SHOP_VIEW\"}},\"gridPanelConfig\":{\"advancedConfig\":\"\",\"formClass\":\"form.herod.order.ShopForm\",\"addable\":true,\"deletable\":true,\"deletselectedable\":true,\"updatable\":true,\"checkboxable\":true,\"columns\":[{\"header\":\"ID\",\"dataIndex\":\"ID\",\"xtype\":null,\"format\":null,\"hidden\":true,\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u540d\\u79f0\",\"dataIndex\":\"NAME\",\"xtype\":null,\"format\":null,\"hidden\":false,\"advancedConfig\":\"{flex:1}\",\"width\":\"\"},{\"header\":\"\\u5546\\u5e97\\u7c7b\\u578b\",\"dataIndex\":\"SHOP_TYPE_NAME\",\"xtype\":null,\"format\":\"\",\"hidden\":\"\",\"advancedConfig\":\"{flex:1}\",\"width\":null},{\"header\":\"\\u5730\\u5740\",\"dataIndex\":\"ADDRESS\",\"xtype\":null,\"format\":\"\",\"hidden\":\"\",\"advancedConfig\":\"{flex:1}\",\"width\":\"\"},{\"header\":\"\\u8054\\u7cfb\\u7535\\u8bdd\",\"dataIndex\":\"CONTACT_NUMBER\",\"xtype\":null,\"format\":null,\"hidden\":\"\",\"advancedConfig\":\"{flex:1}\",\"width\":\"\"}],\"toolbarItems\":[],\"operationItems\":[]},\"searchPanelConfig\":{\"columnCount\":5,\"advancedConfig\":\"\",\"items\":[{\"fieldLabel\":\"\\u5546\\u5e97\\u540d\\u79f0\",\"name\":\"NAME\",\"op\":\"like\",\"format\":null,\"xtype\":\"textfield\",\"colspan\":1,\"advancedConfig\":\"\",\"type\":\"string\"}]}}','',NULL,NULL),(10004,'配送员管理列表配置','delivery_worker_grid_config','bear.module.datasource.InfoManagerGridPanel','{\"dataSetConfig\":{\"dataSetContextLocation\":\"tableContext\",\"dataSetCode\":\"ZRH_AGENT_DELIVERY_WORKER\",\"nodeTypeCode\":\"ZRH_AGENT_DELIVERY_WORKER\",\"pageSize\":\"30\",\"advancedConfig\":\"\",\"requireControls\":\"\",\"dataSetContext\":{\"code\":\"ZRH_AGENT_DELIVERY_WORKER\"}},\"gridPanelConfig\":{\"advancedConfig\":\"\",\"formClass\":\"form.herod.order.DeliveryWorkerForm\",\"addable\":true,\"deletable\":true,\"deletselectedable\":true,\"updatable\":true,\"checkboxable\":true,\"columns\":[{\"header\":\"ID\",\"dataIndex\":\"ID\",\"xtype\":null,\"format\":null,\"hidden\":true,\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u59d3\\u540d\",\"dataIndex\":\"NAME\",\"xtype\":null,\"format\":null,\"hidden\":false,\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u7535\\u8bdd\\u53f7\\u7801\",\"dataIndex\":\"PHONE\",\"xtype\":null,\"format\":null,\"hidden\":\"\",\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u72b6\\u6001\",\"dataIndex\":\"FLAG\",\"xtype\":null,\"format\":null,\"hidden\":\"\",\"advancedConfig\":\"{\\nrenderer: function(value) {\\n            return value == 1 ? \'\\u5728\\u804c\' : \'\\u79bb\\u804c\';\\n        }\\n}\",\"width\":\"\"}],\"toolbarItems\":[],\"operationItems\":[]},\"searchPanelConfig\":{\"columnCount\":5,\"advancedConfig\":\"\",\"items\":[{\"fieldLabel\":\"\\u59d3\\u540d\",\"name\":\"NAME\",\"op\":\"like\",\"format\":null,\"xtype\":\"textfield\",\"colspan\":1,\"advancedConfig\":\"\",\"type\":\"string\"},{\"fieldLabel\":\"\\u7535\\u8bdd\\u53f7\\u7801\",\"name\":\"PHONE\",\"op\":\"=\",\"format\":null,\"xtype\":\"textfield\",\"colspan\":1,\"advancedConfig\":\"\",\"type\":\"string\"}]}}','',NULL,NULL),(10005,'商家管理列表配置','shop_manger_grid_config_2','grid.herod.order.ShopInfoManagerGridPanel','{\"dataSetConfig\":{\"dataSetContextLocation\":\"basicDataSetContext\",\"dataSetCode\":\"SHOP_VIEW\",\"nodeTypeCode\":\"ZRH_SHOP\",\"pageSize\":\"25\",\"advancedConfig\":\"\",\"requireControls\":\"\",\"dataSetContext\":{\"code\":\"SHOP_VIEW\"}},\"gridPanelConfig\":{\"advancedConfig\":\"\",\"formClass\":\"form.herod.order.ShopForm\",\"addable\":true,\"deletable\":true,\"deletselectedable\":true,\"updatable\":true,\"checkboxable\":true,\"columns\":[{\"header\":\"ID\",\"dataIndex\":\"ID\",\"xtype\":null,\"format\":null,\"hidden\":true,\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u540d\\u79f0\",\"dataIndex\":\"NAME\",\"xtype\":null,\"format\":\"\",\"hidden\":false,\"advancedConfig\":\"{flex:1}\",\"width\":\"\"},{\"header\":\"\\u5546\\u5e97\\u7c7b\\u578b\",\"dataIndex\":\"SHOP_TYPE_NAME\",\"xtype\":null,\"format\":\"\",\"hidden\":\"\",\"advancedConfig\":\"{flex:1}\",\"width\":\"\"},{\"header\":\"\\u5546\\u5e97\\u5730\\u5740\",\"dataIndex\":\"ADDRESS\",\"xtype\":null,\"format\":null,\"hidden\":\"\",\"advancedConfig\":\"{flex:1}\",\"width\":\"\"},{\"header\":\"\\u8054\\u7cfb\\u7535\\u8bdd\",\"dataIndex\":\"CONTACT_NUMBER\",\"xtype\":null,\"format\":null,\"hidden\":\"\",\"advancedConfig\":\"{flex:1}\",\"width\":\"\"}],\"toolbarItems\":[],\"operationItems\":[]},\"searchPanelConfig\":{\"columnCount\":5,\"advancedConfig\":\"\",\"items\":[{\"fieldLabel\":\"\\u5546\\u5e97\\u540d\\u79f0\",\"propertyName\":\"\",\"op\":\"like\",\"xtype\":\"textfield\",\"type\":\"string\",\"format\":null,\"colspan\":1,\"name\":\"NAME\",\"advancedConfig\":\"\"}]}}','',NULL,NULL),(10006,'商品管理列表配置','goods_grid_config','bear.module.datasource.InfoManagerGridPanel','{\"dataSetConfig\":{\"dataSetContextLocation\":\"tableContext\",\"dataSetCode\":\"ZRH_GOODS\",\"nodeTypeCode\":\"ZRH_GOODS\",\"pageSize\":\"20\",\"advancedConfig\":\"\",\"requireControls\":\"\",\"dataSetContext\":{\"code\":\"ZRH_GOODS\"}},\"gridPanelConfig\":{\"advancedConfig\":\"\",\"formClass\":null,\"addable\":true,\"deletable\":true,\"deletselectedable\":true,\"updatable\":true,\"checkboxable\":true,\"columns\":[{\"header\":\"ID\",\"dataIndex\":\"ID\",\"xtype\":null,\"format\":null,\"hidden\":true,\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u540d\\u79f0\",\"dataIndex\":\"NAME\",\"xtype\":null,\"format\":null,\"hidden\":false,\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u7f16\\u7801\",\"dataIndex\":\"CODE\",\"xtype\":null,\"format\":null,\"hidden\":\"\",\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u4f9b\\u5e94\\u4ef7\\u683c\",\"dataIndex\":\"SUPPLY_PRICE\",\"xtype\":null,\"format\":null,\"hidden\":\"\",\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u552e\\u4ef7\",\"dataIndex\":\"SELLING_PRICE\",\"xtype\":null,\"format\":null,\"hidden\":\"\",\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u8ba1\\u91cf\\u5355\\u4f4d\",\"dataIndex\":\"UNIT\",\"xtype\":null,\"format\":null,\"hidden\":\"\",\"advancedConfig\":\"\",\"width\":\"\"}],\"toolbarItems\":[],\"operationItems\":[]},\"searchPanelConfig\":{\"columnCount\":5,\"advancedConfig\":\"\",\"items\":[{\"fieldLabel\":\"\\u540d\\u79f0\",\"propertyName\":\"\",\"op\":\"like\",\"xtype\":\"textfield\",\"type\":\"string\",\"format\":null,\"colspan\":1,\"name\":\"NAME\",\"advancedConfig\":\"\"},{\"fieldLabel\":\"\\u7f16\\u7801\",\"propertyName\":\"\",\"op\":\"like\",\"xtype\":\"textfield\",\"type\":\"string\",\"format\":null,\"colspan\":1,\"name\":\"CODE\",\"advancedConfig\":\"\"}]}}','',NULL,NULL),(10007,'商品分类列表配置','goods_category_grid_config','bear.module.datasource.InfoManagerGridPanel','{\"dataSetConfig\":{\"dataSetContextLocation\":\"tableContext\",\"dataSetCode\":\"ZRH_GOODS_CATEGORY\",\"nodeTypeCode\":\"ZRH_GOODS_CATEGORY\",\"pageSize\":\"20\",\"advancedConfig\":\"\",\"requireControls\":\"\",\"dataSetContext\":{\"code\":\"ZRH_GOODS_CATEGORY\"}},\"gridPanelConfig\":{\"advancedConfig\":\"\",\"formClass\":null,\"addable\":true,\"deletable\":true,\"deletselectedable\":true,\"updatable\":true,\"checkboxable\":true,\"columns\":[{\"header\":\"ID\",\"dataIndex\":\"ID\",\"xtype\":null,\"format\":null,\"hidden\":true,\"advancedConfig\":\"\",\"width\":\"\"},{\"header\":\"\\u540d\\u79f0\",\"dataIndex\":\"NAME\",\"xtype\":null,\"format\":null,\"hidden\":false,\"advancedConfig\":\"\",\"width\":\"\"}],\"toolbarItems\":[],\"operationItems\":[]},\"searchPanelConfig\":{\"columnCount\":5,\"advancedConfig\":\"\",\"items\":[]}}','',NULL,NULL);

/*Table structure for table `data_set` */

DROP TABLE IF EXISTS `data_set`;

CREATE TABLE `data_set` (
  `ID` bigint(20) NOT NULL auto_increment,
  `CODE` varchar(50) NOT NULL,
  `NAME` varchar(50) default NULL,
  `TYPE` varchar(255) default NULL,
  `_SQL` text,
  `PINYIN` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `data_set` */

insert  into `data_set`(`ID`,`CODE`,`NAME`,`TYPE`,`_SQL`,`PINYIN`) values (1,'SHOP_TYPE_VIEW','商店类型视图','com.fpi.bear.dataset.spi.basic.SQLDataSet','SELECT ID, NAME FROM ZRH_SHOP_TYPE','sdlxst'),(2,'SHOP_VIEW','商店视图','com.fpi.bear.dataset.spi.basic.SQLDataSet','SELECT S.ID, S.NAME, T.NAME SHOP_TYPE_NAME, S.ADDRESS, S.CONTACT_NUMBER, S.AGENT_ID FROM ZRH_SHOP S LEFT JOIN ZRH_SHOP_TYPE T ON S.SHOP_TYPE_ID = T.ID','sdst');

/*Table structure for table `database_alias` */

DROP TABLE IF EXISTS `database_alias`;

CREATE TABLE `database_alias` (
  `TABLE_NAME` varchar(50) default NULL,
  `COLUMN_NAME` varchar(50) default NULL,
  `ALIAS` varchar(50) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `database_alias` */

insert  into `database_alias`(`TABLE_NAME`,`COLUMN_NAME`,`ALIAS`) values ('ORGANIZATION',NULL,'组织'),('ORGANIZATION','ID','编码'),('ORGANIZATION','NAME','名称'),('ORGANIZATION','PARENTID','父组织'),('ORGANIZATION','DESCRIPTION','描述'),('ORGANIZATION','POSITIONCODE','组织位置编码'),('ORGANIZATION','PINYIN','拼音'),('POSITION',NULL,'岗位'),('POSITION','ID','编码'),('POSITION','NAME','名称'),('POSITION','DESCRIPTION','描述'),('POSITION','PINYIN','拼音'),('STAFF',NULL,'员工'),('STAFF','ID','编码'),('STAFF','NAME','姓名'),('STAFF','USERID','账号'),('STAFF','POSITIONID','岗位'),('STAFF','ORGANIZATIONID','组织'),('STAFF','TELEPHONE','电话号码'),('STAFF','MOBILEPHONE','手机'),('STAFF','EMAIL','电子邮箱'),('STAFF','ADDRESS','联系地址'),('STAFF','PINYIN','拼音'),('ZRH_AGENT',NULL,'代理商'),('ZRH_AGENT','ID','主键'),('ZRH_AGENT','NAME','名称'),('ZRH_AGENT','LEGAL_REPRESENTATIVE','法人代表'),('ZRH_AGENT','LINKMAN','联系人'),('ZRH_AGENT','CONTACT_NUMBER','联系电话'),('ZRH_AGENT','CONTACT_ADDRESS','联系地址'),('ZRH_AGENT','BANK_NAME','开户行'),('ZRH_AGENT','BANK_ACCOUNT','银行账号'),('ZRH_AGENT','ORGANIZATION_CODE','组织机构代码'),('ZRH_AGENT','BUSINESS_LICENSE','营业执照'),('ZRH_AGENT','ADMIN_ACCOUNT','管理员账号'),('ZRH_AGENT','PINYIN','拼音'),('ZRH_GOODS',NULL,'商品'),('ZRH_GOODS','ID','主键'),('ZRH_GOODS','NAME','名称'),('ZRH_GOODS','CODE','商品编码'),('ZRH_GOODS','ALIAS','别名'),('ZRH_GOODS','SUPPLY_PRICE','商店供货价'),('ZRH_GOODS','SELLING_PRICE','售价'),('ZRH_GOODS','UNIT','计量单位'),('ZRH_GOODS','COMMENT','商品备注'),('ZRH_GOODS','LARGE_IMAGE','大图'),('ZRH_GOODS','THUMBNAIL','缩略图'),('ZRH_GOODS','CATEGORY_ID','所属商品分类'),('ZRH_GOODS','SHOP_ID','所属商店'),('ZRH_GOODS','AGENT_ID','所属代理商'),('ZRH_GOODS','PINYIN','拼音'),('ZRH_ORDER',NULL,'订单'),('ZRH_ORDER','ID','主键'),('ZRH_ORDER','SERIAL_NUMBER','流水号'),('ZRH_ORDER','BUYER_PHONE','买家电话'),('ZRH_ORDER','BUYER_NAME','买家名称'),('ZRH_ORDER','AGENT_ID','所属代理商'),('ZRH_ORDER','SHOP_ID','所属商店'),('ZRH_ORDER','DELIVERY_WORKER_ID','配送人员'),('ZRH_ORDER','STATUS','状态'),('ZRH_ORDER','SUBMIT_TIME','提交时间'),('ZRH_ORDER','COMPLETE_TIME','完成时间'),('ZRH_ORDER','PREPARE_TIME','备货时间'),('ZRH_ORDER','DELIVERY_ADDRESS','配送地址'),('ZRH_ORDER','DELIVERY_LONGITUDE','配送地址经度'),('ZRH_ORDER','DELIVERY_LATITUDE','配送地址纬度'),('ZRH_ORDER','COMMENT','备注'),('ZRH_ORDER_ITEM',NULL,'订单项'),('ZRH_ORDER_ITEM','ID','主键'),('ZRH_ORDER_ITEM','SERIAL_NUMBER','流水号'),('ZRH_ORDER_ITEM','ORDER_SERIAL_NUMBER','所属订单流水号'),('ZRH_ORDER_ITEM','GOODS_ID','商品ID'),('ZRH_ORDER_ITEM','GOODS_CODE','商品编码'),('ZRH_ORDER_ITEM','AGENT_ID','所属代理商'),('ZRH_ORDER_ITEM','SHOP_ID','所属商店'),('ZRH_ORDER_ITEM','SELLING_PRICE','售价'),('ZRH_ORDER_ITEM','SUPPLY_PRICE','供货价'),('ZRH_ORDER_ITEM','QUANTITY','数量'),('ZRH_ORDER_ITEM','FLAG','标志'),('ZRH_ORDER_LOG',NULL,'订单操作日志'),('ZRH_ORDER_LOG','ID','主键'),('ZRH_ORDER_LOG','ORDER_SERIAL_NUMBER','订单流水号'),('ZRH_ORDER_LOG','OPERATION','操作'),('ZRH_ORDER_LOG','REASON','原因'),('ZRH_ORDER_LOG','OPERATOR_TYPE','操作者类型'),('ZRH_ORDER_LOG','OPERATOR','操作者'),('ZRH_ORDER_LOG','OPERATE_TIME','操作时间'),('ZRH_ORDER_LOG','COMMENT','备注'),('ZRH_SHOP_TYPE',NULL,'商店类型'),('ZRH_SHOP_TYPE','ID','主键'),('ZRH_SHOP_TYPE','NAME','名称'),('ZRH_SHOP_TYPE','IMAGE_URL','图片'),('ZRH_SHOP_TYPE','COMMENT','备注'),('ZRH_SHOP_TYPE','PINYIN','拼音'),('USERS',NULL,'账户'),('USERS','NAME','用户名'),('USERS','PASSWORD','密码'),('USERS','CREATETIME','创建时间'),('USERS','USER_TYPE','用户类型'),('USERS','PINYIN','拼音'),('ZRH_AGENT_DELIVERY_WORKER',NULL,'代理商配送人员'),('ZRH_AGENT_DELIVERY_WORKER','ID','主键'),('ZRH_AGENT_DELIVERY_WORKER','NAME','姓名'),('ZRH_AGENT_DELIVERY_WORKER','PHONE','电话'),('ZRH_AGENT_DELIVERY_WORKER','PASSWORD','密码'),('ZRH_AGENT_DELIVERY_WORKER','AGENT_ID','所属代理商'),('ZRH_AGENT_DELIVERY_WORKER','FLAG','状态标记'),('ZRH_AGENT_DELIVERY_WORKER','ID_NUMBER','身份证号'),('ZRH_AGENT_DELIVERY_WORKER','PINYIN','拼音'),('ZRH_SHOP',NULL,'商店'),('ZRH_SHOP','ID','主键'),('ZRH_SHOP','NAME','名称'),('ZRH_SHOP','SHOP_TYPE_ID','商店类型'),('ZRH_SHOP','AGENT_ID','所属代理商'),('ZRH_SHOP','ADDRESS','商店地址'),('ZRH_SHOP','CONTACT_NUMBER','联系电话'),('ZRH_SHOP','LONGITUDE','经度'),('ZRH_SHOP','LATITUDE','纬度'),('ZRH_SHOP','SERVICE_RADIUS','服务半径'),('ZRH_SHOP','IMAGE_URL','图片'),('ZRH_SHOP','BANK_NAME','开户行'),('ZRH_SHOP','BANK_ACCOUNT','银行账号'),('ZRH_SHOP','ORGANIZATION_CODE','组织机构代码'),('ZRH_SHOP','BUSINESS_LICENSE','营业执照'),('ZRH_SHOP','LINKMAN','联系人'),('ZRH_SHOP','COMMENT','备注'),('ZRH_SHOP','PINYIN','拼音'),('ZRH_GOODS_CATEGORY',NULL,'商品分类'),('ZRH_GOODS_CATEGORY','ID','主键'),('ZRH_GOODS_CATEGORY','NAME','名称'),('ZRH_GOODS_CATEGORY','ALIAS','别名'),('ZRH_GOODS_CATEGORY','SHOP_ID','所属商店'),('ZRH_GOODS_CATEGORY','AGENT_ID','所属代理商'),('ZRH_GOODS_CATEGORY','PINYIN','拼音');

/*Table structure for table `file_info` */

DROP TABLE IF EXISTS `file_info`;

CREATE TABLE `file_info` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `URL` varchar(255) default NULL,
  `UUID` varchar(255) default NULL,
  `UPLOADUSER` varchar(255) default NULL,
  `UPLOADDATE` datetime default NULL,
  `PINYIN` varchar(255) default NULL,
  `TYPE` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `file_info` */

insert  into `file_info`(`ID`,`NAME`,`URL`,`UUID`,`UPLOADUSER`,`UPLOADDATE`,`PINYIN`,`TYPE`) values (1,'alarm.png','/d255fa4783014485b5897f2ceb1fdc9f.png','d255fa4783014485b5897f2ceb1fdc9f',NULL,'2013-06-08 21:23:45',NULL,'SystemIcon');

/*Table structure for table `group_` */

DROP TABLE IF EXISTS `group_`;

CREATE TABLE `group_` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `POSITIONCODE` varchar(255) default NULL,
  `CHILDCOUNT` int(11) default NULL,
  `CATEGORYCODE` varchar(255) default NULL,
  `PARENTID` bigint(20) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `group_` */

insert  into `group_`(`ID`,`NAME`,`POSITIONCODE`,`CHILDCOUNT`,`CATEGORYCODE`,`PARENTID`) values (10000,'组织和权限','00000001',0,'NodeTypeGroup',-1),(10001,'宅人汇','00000001',0,'NodeTypeGroup',-1);

/*Table structure for table `groupcategory` */

DROP TABLE IF EXISTS `groupcategory`;

CREATE TABLE `groupcategory` (
  `ID` bigint(20) NOT NULL auto_increment,
  `CODE` varchar(255) default NULL,
  `NAME` varchar(255) default NULL,
  `DEPTH` int(11) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `PINYIN` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `groupcategory` */

insert  into `groupcategory`(`ID`,`CODE`,`NAME`,`DEPTH`,`DESCRIPTION`,`PINYIN`) values (10000,'NodeTypeGroup','节点类型分组',0,'节点类型分组类别',NULL);

/*Table structure for table `menu` */

DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL default '',
  `CODE` varchar(255) default '',
  `ICON` varchar(255) default '',
  `URL` varchar(255) default '',
  `FUNCCLASS` varchar(255) default '',
  `FUNCDEFINE` bigint(20) default '-1',
  `PARENTID` bigint(20) default '-1',
  `SYSTEMID` bigint(20) default '-1',
  `ORDERINDEX` int(11) default '0',
  `HIDDEN` tinyint(1) default '0',
  `DESCRIPTION` varchar(255) default '',
  `PINYIN` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `menu` */

insert  into `menu`(`ID`,`NAME`,`CODE`,`ICON`,`URL`,`FUNCCLASS`,`FUNCDEFINE`,`PARENTID`,`SYSTEMID`,`ORDERINDEX`,`HIDDEN`,`DESCRIPTION`,`PINYIN`) values (2,'节点类型管理','nodeTypeDefine','','','infodefine.nodetype.NodeTypeManager',-1,-1,10000,1,0,'',NULL),(3,'数据集管理','dataSetManager','','','bear.module.datasource.InfoManagerGridPanel',10000,-1,10000,2,0,'',NULL),(4,'设计工具','designers','','','',-1,-1,10000,2,0,'',NULL),(5,'表单设计器','formDesigner','','','bear.designer.form.view.VisualFormDesigner',-1,4,10000,0,0,'',NULL),(6,'界面设计器','webDesigner','','','bear.designer.web.module.WebDesignerPanel',-1,4,10000,1,0,'',NULL),(10000,'系统构建','systemBuilder','systemIcons/7e6d9e5ec6074a3c83a2c95189f273a2.gif','','bear.module.builder.SystemBuild',0,-1,10000,0,0,'',NULL),(10001,'商店类型管理','shop_type_manager','','','bear.module.datasource.InfoManagerGridPanel',10001,-1,10001,0,0,'',NULL),(10002,'代理商管理','agent_manager','','','bear.module.datasource.InfoManagerGridPanel',10002,-1,10001,1,0,'',NULL),(10003,'商家管理','shop_manager','','','grid.herod.order.ShopInfoManagerGridPanel',10005,-1,10002,0,0,'',NULL),(10004,'账户管理','user_manager','','','bear.module.permissioncontrol.permission.control.user.UserGrid',-1,-1,10003,0,0,'',NULL),(10005,'角色管理','role_manager','','','bear.module.permissioncontrol.permission.control.role.RoleGrid',-1,-1,10003,1,0,'',NULL),(10006,'权限管理','permission_manager','','','bear.module.permissioncontrol.permission.control.permission.PermissionGrid',-1,-1,10003,2,0,'',NULL),(10007,'动作管理','action_manager','','','bear.module.permissioncontrol.permission.control.action.ActionGrid',-1,-1,10003,3,0,'',NULL),(10008,'配送员管理','delivery_worker_manager','','','bear.module.datasource.InfoManagerGridPanel',10004,-1,10002,1,0,'',NULL);

/*Table structure for table `nodetype` */

DROP TABLE IF EXISTS `nodetype`;

CREATE TABLE `nodetype` (
  `ID` bigint(20) NOT NULL auto_increment,
  `CODE` varchar(255) default NULL,
  `NAME` varchar(255) default NULL,
  `PINYIN` varchar(255) default NULL,
  `GROUPID` bigint(20) default NULL,
  `CREATETIME` datetime default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `TREESTRUCTURE` tinyint(1) default '0',
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `nodetype` */

insert  into `nodetype`(`ID`,`CODE`,`NAME`,`PINYIN`,`GROUPID`,`CREATETIME`,`DESCRIPTION`,`TREESTRUCTURE`) values (1,'ORGANIZATION','组织','zz',10000,NULL,'',0),(2,'POSITION','岗位','gw',10000,NULL,'',0),(3,'USERS','账户','zh',10000,NULL,'',0),(4,'SERVICE_USER_MAPPING','应用用户映射','yyyhys',10000,NULL,NULL,0),(10000,'STAFF','员工','yg',10000,NULL,'',0),(10001,'ZRH_SHOP_TYPE','商店类型','sdlx',10001,NULL,'',0),(10002,'ZRH_AGENT','代理商','dls',10001,'2013-06-08 16:06:03','',0),(10003,'ZRH_AGENT_DELIVERY_WORKER','代理商配送人员','dlspsry',10001,NULL,'',0),(10004,'ZRH_SHOP','商店','sd',10001,NULL,'',0),(10005,'ZRH_GOODS_CATEGORY','商品分类','spfl',10001,NULL,'',0),(10006,'ZRH_GOODS','商品','sp',10001,'2013-06-08 16:20:33','',0),(10007,'ZRH_ORDER','订单','dd',10001,'2013-06-08 17:15:29','',0),(10008,'ZRH_ORDER_ITEM','订单项','ddx',10001,'2013-06-08 17:20:34','',0),(10009,'ZRH_ORDER_LOG','订单操作日志','ddczrz',10001,'2013-06-08 20:46:43','',0);

/*Table structure for table `nodetypestrategy` */

DROP TABLE IF EXISTS `nodetypestrategy`;

CREATE TABLE `nodetypestrategy` (
  `ID` bigint(20) NOT NULL,
  `NODETYPECODE` varchar(255) default NULL,
  `STRATEGYCODE` varchar(255) default NULL,
  `OPTIONVALUE` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `nodetypestrategy` */

/*Table structure for table `organization` */

DROP TABLE IF EXISTS `organization`;

CREATE TABLE `organization` (
  `ID` bigint(19) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `PARENTID` bigint(19) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `POSITIONCODE` varchar(255) default NULL,
  `PINYIN` varchar(255) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK_PARENTID_ORGANIZATION_ID` (`PARENTID`),
  CONSTRAINT `FK_PARENTID_ORGANIZATION_ID` FOREIGN KEY (`PARENTID`) REFERENCES `organization` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `organization` */

/*Table structure for table `organization_position` */

DROP TABLE IF EXISTS `organization_position`;

CREATE TABLE `organization_position` (
  `ORGANIZATIONID` bigint(20) NOT NULL,
  `POSITIONID` bigint(20) NOT NULL,
  PRIMARY KEY  (`ORGANIZATIONID`,`POSITIONID`),
  KEY `FK_ORGANIZATION_POSITION` (`POSITIONID`),
  CONSTRAINT `FK_ORGANIZATION_POSITION` FOREIGN KEY (`POSITIONID`) REFERENCES `position` (`ID`),
  CONSTRAINT `FK_ORGANIZATION_POSITION_1` FOREIGN KEY (`ORGANIZATIONID`) REFERENCES `organization` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `organization_position` */

/*Table structure for table `permissions` */

DROP TABLE IF EXISTS `permissions`;

CREATE TABLE `permissions` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `TYPE` varchar(255) default NULL,
  `CODE` longtext,
  `DESCRIPTION` varchar(255) default NULL,
  `PINYIN` varchar(255) default NULL,
  `LOCATION` varchar(255) default NULL,
  `CONDITION_DEFINE` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `permissions` */

insert  into `permissions`(`ID`,`NAME`,`TYPE`,`CODE`,`DESCRIPTION`,`PINYIN`,`LOCATION`,`CONDITION_DEFINE`) values (10000,'all','simpleDefine','*','超级管理员权限',NULL,NULL,NULL),(10001,'SystemPermission10001','SystemPermission','SystemPermission:VIEW:10002',NULL,NULL,NULL,NULL),(10002,'MenuPermission10001','MenuPermission','MenuPermission:VIEW:10003,10008',NULL,NULL,NULL,NULL),(10003,'商店视图查看权限','simpleDefine','SHOP_VIEW:VIEW',NULL,NULL,NULL,NULL),(10004,'配送人员查看权限','simpleDefine','ZRH_AGENT_DELIVERY_WORKER:VIEW',NULL,NULL,NULL,NULL),(10005,'店铺类型查看权限','simpleDefine','SHOP_TYPE_VIEW:VIEW',NULL,NULL,NULL,NULL),(10006,'商品分类查看权限','simpleDefine','ZRH_GOODS_CATEGORY:VIEW',NULL,NULL,NULL,NULL),(10007,'商品查看权限','simpleDefine','ZRH_GOODS:VIEW',NULL,NULL,NULL,NULL);

/*Table structure for table `position` */

DROP TABLE IF EXISTS `position`;

CREATE TABLE `position` (
  `ID` bigint(19) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `PINYIN` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `position` */

/*Table structure for table `roles` */

DROP TABLE IF EXISTS `roles`;

CREATE TABLE `roles` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `ISDEFAULTROLE` varchar(255) NOT NULL,
  `PINYIN` varchar(255) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `roles` */

insert  into `roles`(`ID`,`NAME`,`ISDEFAULTROLE`,`PINYIN`) values (10000,'超级管理员','no',NULL),(10001,'代理商','',NULL);

/*Table structure for table `roles_permissions` */

DROP TABLE IF EXISTS `roles_permissions`;

CREATE TABLE `roles_permissions` (
  `PERMISSIONID` bigint(20) NOT NULL,
  `ROLEID` bigint(20) NOT NULL,
  PRIMARY KEY  (`ROLEID`,`PERMISSIONID`),
  KEY `FK250AE02A182099A` (`PERMISSIONID`),
  KEY `FK250AE02DBECBDE8` (`ROLEID`),
  CONSTRAINT `ROLES_PERMISSIONS_IBFK_1` FOREIGN KEY (`ROLEID`) REFERENCES `roles` (`ID`),
  CONSTRAINT `ROLES_PERMISSIONS_IBFK_2` FOREIGN KEY (`PERMISSIONID`) REFERENCES `permissions` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `roles_permissions` */

insert  into `roles_permissions`(`PERMISSIONID`,`ROLEID`) values (10000,10000),(10001,10001),(10002,10001),(10003,10001),(10004,10001),(10005,10001),(10006,10001),(10007,10001);

/*Table structure for table `service_user_mapping` */

DROP TABLE IF EXISTS `service_user_mapping`;

CREATE TABLE `service_user_mapping` (
  `ID` bigint(19) NOT NULL auto_increment,
  `SERVICE_USER_NAME` varchar(255) default NULL,
  `LOCAL_USER_ID` bigint(19) default NULL,
  `SERVICE_NAME` varchar(255) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK_SERVICE_USER_MAPPING_LOCAL_USER_ID` (`LOCAL_USER_ID`),
  CONSTRAINT `FK_SERVICE_USER_MAPPING_LOCAL_USER_ID` FOREIGN KEY (`LOCAL_USER_ID`) REFERENCES `users` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `service_user_mapping` */

/*Table structure for table `staff` */

DROP TABLE IF EXISTS `staff`;

CREATE TABLE `staff` (
  `ID` bigint(19) NOT NULL auto_increment,
  `NAME` varchar(255) default NULL,
  `USERID` bigint(19) default NULL,
  `POSITIONID` bigint(19) default NULL,
  `ORGANIZATIONID` bigint(19) default NULL,
  `TELEPHONE` varchar(255) default NULL,
  `MOBILEPHONE` varchar(255) default NULL,
  `EMAIL` varchar(255) default NULL,
  `ADDRESS` varchar(255) default NULL,
  `PINYIN` varchar(255) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `UNIQUE_USERID` (`USERID`),
  KEY `FK_ORG` (`ORGANIZATIONID`),
  KEY `FK_POS` (`POSITIONID`),
  CONSTRAINT `FK_ORG` FOREIGN KEY (`ORGANIZATIONID`) REFERENCES `organization` (`ID`),
  CONSTRAINT `FK_POS` FOREIGN KEY (`POSITIONID`) REFERENCES `position` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `staff` */

insert  into `staff`(`ID`,`NAME`,`USERID`,`POSITIONID`,`ORGANIZATIONID`,`TELEPHONE`,`MOBILEPHONE`,`EMAIL`,`ADDRESS`,`PINYIN`) values (10000,'超级管理员',10000,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `strategydefinition` */

DROP TABLE IF EXISTS `strategydefinition`;

CREATE TABLE `strategydefinition` (
  `ID` bigint(20) NOT NULL auto_increment,
  `CODE` varchar(255) default NULL,
  `NAME` varchar(255) default NULL,
  `STRATEGYOPTION` varchar(255) default NULL,
  `CREATETIME` datetime default NULL,
  `PINYIN` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `strategydefinition` */

/*Table structure for table `system` */

DROP TABLE IF EXISTS `system`;

CREATE TABLE `system` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL default '',
  `CODE` varchar(255) default '',
  `ICON` varchar(255) default '',
  `LOGO` varchar(255) default '',
  `TITLEIMG` varchar(255) default '',
  `LAYOUT` varchar(255) default '',
  `URL` varchar(255) default '',
  `DESCRIPTION` varchar(255) default '',
  `PINYIN` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `system` */

insert  into `system`(`ID`,`NAME`,`CODE`,`ICON`,`LOGO`,`TITLEIMG`,`LAYOUT`,`URL`,`DESCRIPTION`,`PINYIN`) values (10000,'构建系统','builder','/systemIcons/900263cd28bc4980981822563324bbfd.jpg','./bear/ued/pmf/standard/images/logo.jpg','bear/ued/pmf/standard/images/logo.jpg','standard','','',NULL),(10001,'管理系统','manager_system','','','','standard','','',NULL),(10002,'代理商管理系统','agent_manager_system','','','','standard','','',NULL),(10003,'权限系统','permission_system','','','','standard','','',NULL);

/*Table structure for table `system_info` */

DROP TABLE IF EXISTS `system_info`;

CREATE TABLE `system_info` (
  `NAME` varchar(255) default NULL,
  `VALUE` varchar(255) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `system_info` */

insert  into `system_info`(`NAME`,`VALUE`) values ('PLATFORM_DB_VERSION','5');

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `ID` bigint(19) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `PASSWORD` varchar(255) default NULL,
  `CREATETIME` datetime default NULL,
  `PINYIN` varchar(255) default NULL,
  `USER_TYPE` varchar(255) NOT NULL default 'Admin',
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `users` */

insert  into `users`(`ID`,`NAME`,`PASSWORD`,`CREATETIME`,`PINYIN`,`USER_TYPE`) values (10000,'root','E10ADC3949BA59ABBE56E057F20F883E','2011-11-11 11:11:11',NULL,'Admin'),(10001,'bj_agent','E10ADC3949BA59ABBE56E057F20F883E','2013-06-13 14:49:21',NULL,'AgentAdmin');

/*Table structure for table `users_roles` */

DROP TABLE IF EXISTS `users_roles`;

CREATE TABLE `users_roles` (
  `ROLEID` bigint(20) NOT NULL,
  `USERID` bigint(20) NOT NULL,
  PRIMARY KEY  (`USERID`,`ROLEID`),
  KEY `FKF6CCD9C6DBECBDE8` (`ROLEID`),
  KEY `FKF6CCD9C6E1421352` (`USERID`),
  CONSTRAINT `FK_USERS_ROLES_USERID` FOREIGN KEY (`USERID`) REFERENCES `users` (`ID`),
  CONSTRAINT `USERS_ROLES_IBFK_1` FOREIGN KEY (`ROLEID`) REFERENCES `roles` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `users_roles` */

insert  into `users_roles`(`ROLEID`,`USERID`) values (10000,10000),(10001,10001);

/*Table structure for table `zrh_agent` */

DROP TABLE IF EXISTS `zrh_agent`;

CREATE TABLE `zrh_agent` (
  `ID` bigint(19) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `LEGAL_REPRESENTATIVE` varchar(255) default NULL,
  `LINKMAN` varchar(255) default NULL,
  `CONTACT_NUMBER` varchar(255) default NULL,
  `CONTACT_ADDRESS` varchar(255) default NULL,
  `BANK_NAME` varchar(255) default NULL,
  `BANK_ACCOUNT` varchar(255) default NULL,
  `ORGANIZATION_CODE` varchar(255) default NULL,
  `BUSINESS_LICENSE` varchar(255) default NULL,
  `ADMIN_ACCOUNT` varchar(255) default NULL,
  `PINYIN` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `zrh_agent` */

insert  into `zrh_agent`(`ID`,`NAME`,`LEGAL_REPRESENTATIVE`,`LINKMAN`,`CONTACT_NUMBER`,`CONTACT_ADDRESS`,`BANK_NAME`,`BANK_ACCOUNT`,`ORGANIZATION_CODE`,`BUSINESS_LICENSE`,`ADMIN_ACCOUNT`,`PINYIN`) values (1,'超级代理商','熊志军','熊志军','110','中南海','中国人民银行','001','','/OrderImages/51a4e54164e0411883dd365671a974cb.png','root','cjdls'),(2,'滨江代理商','张三','张三','120','精工科技','','','','','bj_agent','bjdls');

/*Table structure for table `zrh_agent_delivery_worker` */

DROP TABLE IF EXISTS `zrh_agent_delivery_worker`;

CREATE TABLE `zrh_agent_delivery_worker` (
  `ID` bigint(19) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `PHONE` varchar(255) NOT NULL,
  `PASSWORD` varchar(255) NOT NULL,
  `AGENT_ID` bigint(19) NOT NULL,
  `FLAG` int(11) NOT NULL default '1',
  `PINYIN` varchar(255) default NULL,
  `ID_NUMBER` varchar(255) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK_ZRH_AGENT_DELIVERY_WORKER_AGENT_ID` (`AGENT_ID`),
  CONSTRAINT `FK_ZRH_AGENT_DELIVERY_WORKER_AGENT_ID` FOREIGN KEY (`AGENT_ID`) REFERENCES `zrh_agent` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `zrh_agent_delivery_worker` */

/*Table structure for table `zrh_goods` */

DROP TABLE IF EXISTS `zrh_goods`;

CREATE TABLE `zrh_goods` (
  `ID` bigint(19) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `CODE` varchar(255) NOT NULL,
  `ALIAS` varchar(255) default NULL,
  `SUPPLY_PRICE` double NOT NULL default '0',
  `SELLING_PRICE` double NOT NULL default '0',
  `UNIT` varchar(255) default NULL,
  `COMMENT` varchar(2048) default NULL,
  `LARGE_IMAGE` varchar(255) default NULL,
  `THUMBNAIL` varchar(255) default NULL,
  `CATEGORY_ID` bigint(19) default NULL,
  `SHOP_ID` bigint(19) default NULL,
  `AGENT_ID` bigint(19) default NULL,
  `PINYIN` varchar(255) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK_ZRH_GOODS_CATEGORY_ID` (`CATEGORY_ID`),
  KEY `FK_ZRH_GOODS_SHOP_ID` (`SHOP_ID`),
  KEY `FK_ZRH_GOODS_AGENT_ID` (`AGENT_ID`),
  CONSTRAINT `FK_ZRH_GOODS_AGENT_ID` FOREIGN KEY (`AGENT_ID`) REFERENCES `zrh_agent` (`ID`),
  CONSTRAINT `FK_ZRH_GOODS_CATEGORY_ID` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `zrh_goods_category` (`ID`),
  CONSTRAINT `FK_ZRH_GOODS_SHOP_ID` FOREIGN KEY (`SHOP_ID`) REFERENCES `zrh_shop` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `zrh_goods` */

insert  into `zrh_goods`(`ID`,`NAME`,`CODE`,`ALIAS`,`SUPPLY_PRICE`,`SELLING_PRICE`,`UNIT`,`COMMENT`,`LARGE_IMAGE`,`THUMBNAIL`,`CATEGORY_ID`,`SHOP_ID`,`AGENT_ID`,`PINYIN`) values (3,'sfsdf','dfg','dfg',4,5,'fdg','','/OrderImages/3bbbaaaee5ae4cae8f4480acb68c6026.png','/OrderImages/edee7c70785942c7a894130d3afd05fe.png',3,7,1,'');

/*Table structure for table `zrh_goods_category` */

DROP TABLE IF EXISTS `zrh_goods_category`;

CREATE TABLE `zrh_goods_category` (
  `ID` bigint(19) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `ALIAS` varchar(255) default NULL,
  `SHOP_ID` bigint(19) NOT NULL,
  `PINYIN` varchar(255) default NULL,
  `AGENT_ID` bigint(19) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK_ZRH_GOODS_CATEGORY_SHOP_ID` (`SHOP_ID`),
  KEY `FK_ZRH_GOODS_CATEGORY_AGENT_ID` (`AGENT_ID`),
  CONSTRAINT `FK_ZRH_GOODS_CATEGORY_AGENT_ID` FOREIGN KEY (`AGENT_ID`) REFERENCES `zrh_agent` (`ID`),
  CONSTRAINT `FK_ZRH_GOODS_CATEGORY_SHOP_ID` FOREIGN KEY (`SHOP_ID`) REFERENCES `zrh_shop` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `zrh_goods_category` */

insert  into `zrh_goods_category`(`ID`,`NAME`,`ALIAS`,`SHOP_ID`,`PINYIN`,`AGENT_ID`) values (3,'sdfsdf','',7,'',1);

/*Table structure for table `zrh_order` */

DROP TABLE IF EXISTS `zrh_order`;

CREATE TABLE `zrh_order` (
  `ID` bigint(19) NOT NULL auto_increment,
  `SERIAL_NUMBER` varchar(255) NOT NULL,
  `BUYER_PHONE` varchar(255) NOT NULL,
  `BUYER_NAME` varchar(255) default NULL,
  `AGENT_ID` bigint(19) NOT NULL,
  `SHOP_ID` bigint(19) NOT NULL,
  `DELIVERY_WORKER_ID` bigint(19) default NULL,
  `STATUS` varchar(255) NOT NULL,
  `SUBMIT_TIME` datetime default NULL,
  `COMPLETE_TIME` datetime default NULL,
  `PREPARE_TIME` int(11) default '0',
  `DELIVERY_ADDRESS` varchar(255) NOT NULL,
  `DELIVERY_LONGITUDE` double default '0',
  `DELIVERY_LATITUDE` double default '0',
  `COMMENT` varchar(2048) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK_ZRH_ORDER_AGENT_ID` (`AGENT_ID`),
  KEY `FK_ZRH_ORDER_SHOP_ID` (`SHOP_ID`),
  KEY `FK_ZRH_ORDER_DELIVERY_WORKER_ID` (`DELIVERY_WORKER_ID`),
  CONSTRAINT `FK_ZRH_ORDER_AGENT_ID` FOREIGN KEY (`AGENT_ID`) REFERENCES `zrh_agent` (`ID`),
  CONSTRAINT `FK_ZRH_ORDER_DELIVERY_WORKER_ID` FOREIGN KEY (`DELIVERY_WORKER_ID`) REFERENCES `zrh_agent_delivery_worker` (`ID`),
  CONSTRAINT `FK_ZRH_ORDER_SHOP_ID` FOREIGN KEY (`SHOP_ID`) REFERENCES `zrh_shop` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `zrh_order` */

/*Table structure for table `zrh_order_item` */

DROP TABLE IF EXISTS `zrh_order_item`;

CREATE TABLE `zrh_order_item` (
  `ID` bigint(19) NOT NULL auto_increment,
  `SERIAL_NUMBER` varchar(255) NOT NULL,
  `ORDER_SERIAL_NUMBER` varchar(255) NOT NULL,
  `GOODS_ID` bigint(19) NOT NULL,
  `GOODS_CODE` varchar(255) NOT NULL,
  `AGENT_ID` bigint(19) NOT NULL,
  `SHOP_ID` bigint(19) NOT NULL,
  `SELLING_PRICE` double NOT NULL default '0',
  `SUPPLY_PRICE` double NOT NULL default '0',
  `QUANTITY` int(11) NOT NULL default '0',
  `FLAG` varchar(255) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK_ZRH_ORDER_ITEM_GOODS_ID` (`GOODS_ID`),
  KEY `FK_ZRH_ORDER_ITEM_AGENT_ID` (`AGENT_ID`),
  KEY `FK_ZRH_ORDER_ITEM_SHOP_ID` (`SHOP_ID`),
  CONSTRAINT `FK_ZRH_ORDER_ITEM_AGENT_ID` FOREIGN KEY (`AGENT_ID`) REFERENCES `zrh_agent` (`ID`),
  CONSTRAINT `FK_ZRH_ORDER_ITEM_GOODS_ID` FOREIGN KEY (`GOODS_ID`) REFERENCES `zrh_goods` (`ID`),
  CONSTRAINT `FK_ZRH_ORDER_ITEM_SHOP_ID` FOREIGN KEY (`SHOP_ID`) REFERENCES `zrh_shop` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `zrh_order_item` */

/*Table structure for table `zrh_order_log` */

DROP TABLE IF EXISTS `zrh_order_log`;

CREATE TABLE `zrh_order_log` (
  `ID` bigint(19) NOT NULL auto_increment,
  `ORDER_SERIAL_NUMBER` varchar(255) NOT NULL,
  `OPERATION` varchar(255) default NULL,
  `REASON` varchar(255) default NULL,
  `OPERATOR_TYPE` varchar(255) default NULL,
  `OPERATOR` varchar(255) default NULL,
  `OPERATE_TIME` datetime default NULL,
  `COMMENT` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `zrh_order_log` */

/*Table structure for table `zrh_shop` */

DROP TABLE IF EXISTS `zrh_shop`;

CREATE TABLE `zrh_shop` (
  `ID` bigint(19) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `SHOP_TYPE_ID` bigint(19) default NULL,
  `AGENT_ID` bigint(19) default NULL,
  `ADDRESS` varchar(255) default NULL,
  `CONTACT_NUMBER` varchar(255) default NULL,
  `LONGITUDE` double default NULL,
  `LATITUDE` double default NULL,
  `SERVICE_RADIUS` int(11) default NULL,
  `IMAGE_URL` varchar(255) default NULL,
  `BANK_NAME` varchar(255) default NULL,
  `BANK_ACCOUNT` varchar(255) default NULL,
  `ORGANIZATION_CODE` varchar(255) default NULL,
  `BUSINESS_LICENSE` varchar(255) default NULL,
  `PINYIN` varchar(255) default NULL,
  `LINKMAN` varchar(255) default NULL,
  `COMMENT` varchar(255) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK_ZRH_SHOP_SHOP_TYPE_ID` (`SHOP_TYPE_ID`),
  KEY `FK_ZRH_SHOP_AGENT_ID` (`AGENT_ID`),
  CONSTRAINT `FK_ZRH_SHOP_AGENT_ID` FOREIGN KEY (`AGENT_ID`) REFERENCES `zrh_agent` (`ID`),
  CONSTRAINT `FK_ZRH_SHOP_SHOP_TYPE_ID` FOREIGN KEY (`SHOP_TYPE_ID`) REFERENCES `zrh_shop_type` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `zrh_shop` */

insert  into `zrh_shop`(`ID`,`NAME`,`SHOP_TYPE_ID`,`AGENT_ID`,`ADDRESS`,`CONTACT_NUMBER`,`LONGITUDE`,`LATITUDE`,`SERVICE_RADIUS`,`IMAGE_URL`,`BANK_NAME`,`BANK_ACCOUNT`,`ORGANIZATION_CODE`,`BUSINESS_LICENSE`,`PINYIN`,`LINKMAN`,`COMMENT`) values (7,'外婆家',1,1,'滨安路','123',120.236387,30.212938,1,'','123','123','001','','wpj','123','');

/*Table structure for table `zrh_shop_type` */

DROP TABLE IF EXISTS `zrh_shop_type`;

CREATE TABLE `zrh_shop_type` (
  `ID` bigint(19) NOT NULL auto_increment,
  `NAME` varchar(255) NOT NULL,
  `IMAGE_URL` varchar(255) default NULL,
  `PINYIN` varchar(255) default NULL,
  `COMMENT` varchar(2048) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `zrh_shop_type` */

insert  into `zrh_shop_type`(`ID`,`NAME`,`IMAGE_URL`,`PINYIN`,`COMMENT`) values (1,'餐饮外卖','/OrderImages/2d7f10aa6870449a87bc9b5dffbf7aab.png','cywm','');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
