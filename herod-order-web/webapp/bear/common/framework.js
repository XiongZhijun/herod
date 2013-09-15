(function() {
	//获取url参数jQuery插件
	(function($){
		$.getUrlParam = function(name){
			var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if (r!=null) return unescape(r[2]); return null;
		}
	})(jQuery);
	$.extend({
		getHashParam: function(name) {
			var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
			var r = window.location.hash.substr(1).match(reg);
			if (r!=null) return unescape(r[2]); return null;
		}
	});
	
	var defaultMenuCode = $.getHashParam('menu'),
		Framework = function(systemCode) {
			var me = this;
			me.systemCode = systemCode;
			dwr.engine.setHeaders({systemCode: systemCode});
			
			//登录超时重定向
			dwr.engine.setErrorHandler(function(msg, ex) {
				if(ex.name == 'com.fpi.bear.permission.exception.BearAuthenticationException') {
					Ext.Msg.alert('提示', '您可能登录超时，请重新登录！', me.logout);
				}
			});
			
			//初始化操作
			me.beforeReady();
			
			$(function() {
				//控件区尺寸控制事件
				$(window).resize($.proxy(me.resizeFuncContainer, me));
				//初始化功能容器大小
				me.resizeFuncContainer();
				//加载所有菜单
				WebFrameMenuService.getSystemMenus(systemCode, {
					callback: function(menus) {
						me.menus = menus || [];
						me.startUp();
					}
				});
			});
		}
	
	window.Framework = Framework;
	
	Framework.prototype = {
		/**
		 * 页面加载之前的初始化操作
		 */
		beforeReady: $.noop,
		/**
		 * 页面加载完成，相关参数都已准备好（系统、菜单变量等都已设置好）
		 */
		ready: $.noop,
		/**
		 * 加载功能控件
		 */
		loadFunc: $.noop,
		/**
		 * 功能项容器尺寸改变时触发
		 */
		onFuncContainerResize: $.noop,
		funcContainerId: 'funcContainer',
		startUp: function() {
			var me = this;
			//获取默认菜单，url指定菜单编码否则获取第一个叶子菜单
			var defaultMenu = null;
			if(defaultMenuCode) {
				defaultMenu = me.getMenuByCode(defaultMenuCode);
			}
			if(!defaultMenu) {
				defaultMenu = me.getFirstLeafMenu();
			}
			
			//加载默认菜单功能
			me.onMenuClicked(defaultMenu);
			me.ready(defaultMenu);
		},
		onMenuClicked: function(menu) {
			var me = this;
			if(menu == me.currentMenu) {
				return;
			}
			me.currentMenu = menu;
			
			var funcTarget = document.getElementById(me.funcContainerId);
			
			//绑定新功能
			if(menu.funcClass) {
				Ext.require(menu.funcClass, Ext.bind(me.loadFunc, me, [funcTarget, menu]));
			} else {
				me.loadFunc(funcTarget, menu);
			}
		},
		resizeFuncContainer: function() {
			var minHeight = 300,
				minWidth = 400,
//				doc = $(document),
//				docHeight = doc.height(),
//				docWidth = doc.width(),
				docHeight = window.innerHeight || document.documentElement.clientHeight,
				docWidth = window.innerWidth || document.documentElement.clientWidth,
				funcHeight = docHeight - 128;
			
			funcHeight = funcHeight > minHeight ? funcHeight : minHeight;
			docWidth = docWidth > minWidth ? docWidth : minWidth;
			
			$('#' + this.funcContainerId).height(funcHeight);
			$('#' + this.funcContainerId).width(docWidth);
			
			this.onFuncContainerResize(docWidth, funcHeight);
		},
		getMenus: function() {
			return this.menus;
		},
		getMenuByCode: function(code) {
			code = code.toLowerCase();
			var findM = null;
			$.each(this.menus, function(idx, menu) {
				findM = searchMenu(this, code);
				if(findM) {
					return false;
				}
			});
			
			return findM;
			
			function searchMenu(m, c) {
				if(m.code.toLowerCase() == c) {
					return m;
				}
				
				var children = m.children;
				if(children && children.length > 0) {
					for(var i = 0, len = children.length; i < len; i++) {
						var fm = arguments.callee(children[i], c);
						if(fm) {
							return fm;
						}
					}
				}
				
				return null;
			}
		},
		getFirstLeafMenu: function() {
			var findMenu = null,
				menus = this.menus;
			while(menus && menus.length > 0){
	    		menu = menus[0];
	    		menus = menu.children;
	    	}
	    	return menu;
		},
		logout: function() {
			$.post('logout', null, function() {
				if (sessionStorage && sessionStorage.clear) {
					sessionStorage.clear();
				}
				window.location.href = 'login.jsp';
			});
		}
	}
	
	/**
	 * 默认框架支持Extjs的功能控件加载
	 */
	$.extend(Framework.prototype, {
		/**
		 * 页面加载之前的初始化操作
		 */
		beforeReady: function() {
			Ext.QuickTips.init();
			Ext.Loader.setConfig({
				enabled:true,
				disableCaching:false,
			    paths: {
			        "Ext": "resources/ext-4.0.0",
			        "bear":"bear",
			        "form" : "form",
			        "designer" : "designer",
			        "grid" : 'grid'
			    }
			});
		},
		/**
		 * 加载功能控件（当前默认为Extjs的控件，以后应将控件接口上提一层，支持更通用的控件，extjs控件只作为一种实现方式）
		 */
		loadFunc: function(funcTarget, menu) {
			var me = this,
				cls = menu.funcClass,
				url = menu.url,
				func = menu['function'],
				cfg = {},
				isNavMenu = !cls && !url && (menu.children && menu.children.length > 0);
			
			if(isNavMenu) {
				return;
			}
			
			if(menu == me.currentRenderMenu) {
				return;
			}
			me.currentRenderMenu = menu;
			if(me.currentFunc) {
				me.currentFunc.destroy();
				delete me.currentFunc;
				me.currentFunc = null;
			}
			
			if(func) {
				try {
					cfg = eval('(' + func.params + ')');
				}catch(e) {
					Util.error('加载参数失败，MenuId:' + menu.id + ',params:' + func.params);
				}
			} else if(!cls) {
				if(url) {
					cls = 'Ext.panel.Panel';
					cfg = {
						html:'<iframe frameborder="0" style="height:100%;width:100%;" src="' + url + '"/>'
					};
				} else {
					cls = 'bear.control.ux.TipPanel';
					cfg = {
						title:'警告',
						text:'功能[' + menu.name + ']正在开发中。。。'
					};
				}
			}
			cfg.height = funcTarget.clientHeight;
			//cfg.layout = 'fit';
			setTimeout(function() {
				me.currentFunc = Ext.create(cls, cfg);
				me.currentFunc.render(funcTarget);
			}, 1);
		},
		/**
		 * 功能项容器尺寸改变时触发
		 * @param {} width
		 * @param {} height
		 */
		onFuncContainerResize: function(width, height) {
			this.currentFunc && this.currentFunc.setSize(width, height);
		},
		editPassword: function(userName) {
			Ext.require("bear.module.permissioncontrol.permission.control.user.ResetPasswordWindow", function() {
	    	var resetPasswordWindow = new bear.module.permissioncontrol.permission.control.user.ResetPasswordWindow();
	    	resetPasswordWindow.show(userName);
	    	});
		}	
	});
})();