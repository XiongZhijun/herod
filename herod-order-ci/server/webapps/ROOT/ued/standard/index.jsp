<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>${system.name}</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/ued/standard/css/main.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/ued/standard/css/ext.css" />
		<%@ include file="/bear/common/init.jsp" %>
		<script type="text/javascript" src="<%=request.getContextPath()%>/ued/standard/js/StandardFramework.js"></script>
		<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.5&ak=CF294a75f5162261d014a114104e5de0"></script>
		<script type="text/javascript">
			var framework = new Framework('${system.code}');
		</script>
	</head>

	<body>
		<div div="header" class="header">
			<div class="logo">
				<img src="${system.titleImg}" />
			</div>
			<div class="menus_wrap">
				<div class="menus">
					<ul>
						<c:forEach var="menu" items="${requestScope['menus']}">
							<c:if test="${!menu.hidden}">
								<li>
									<a code="${menu.code}"<c:if test="${menu.children == null || menu.children.size() == 0}"> href="#menu=${menu.code}"</c:if>>
										<span><p style="background: url('${menu.icon}') no-repeat center 3px;">${menu.name}</p></span>
									</a>
									<c:if test="${menu.children != null && menu.children.size() != 0}">
										<div class="second_menus">
											<c:forEach var="childMenu" items="${menu.children}">
												<c:if test="${!childMenu.hidden}">
													<a code="${childMenu.code}" href="#menu=${childMenu.code}">${childMenu.name}</a>
												</c:if>
											</c:forEach>
										</div>
									</c:if>
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</div>
			</div>
			<div class="headt">
				<div id="sword_secondmenus"></div>
			</div>
		</div>
		<div id="funcContainer"></div>
		<div id="footer" class="footer">
			<div class="footer_left">
				<ul>
					<li><a id="footer_username" href="javascript:void(0)"
						onclick="MainUI.editUserInfo();return false;">${sessionScope["pms_user"].STAFF_NAME}，您好！</a></li>
					<li><a href="javascript:void(0)" onclick="framework.editPassword('${sessionScope["pms_user"].USER_NAME}')">修改密码</a></li>
					<li><a id="footer_username" href="javascript:void(0)"
						onclick="framework.logout();"> 注销 </a></li>
					<c:if test="${requestScope['systems'].size() > 1}">
						<li>
							<div id="entranceMoreWrap" class="entrance">
								<a class="entrance-more-old" title="访问其他更多系统">更多系统</a>
								<div class="entrance-list" style="z-index: 10001;">
									<ul>
										<c:forEach var="system" items="${requestScope['systems']}">
											<c:if test="${system.code != requestScope['system'].code}">
												<li>
													<a href="<%=request.getContextPath()%>/system?code=${system.code}" target="_blank">${system.name}</a>
												</li>
											</c:if>
										</c:forEach>
									</ul>
								</div>
							</div>
						</li>
					</c:if>
				</ul>
			</div>
			<div class="footer_right">宅人汇 版权所有</div>
		</div>
	</body>
</html>