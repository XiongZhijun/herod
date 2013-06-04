<%@ page language="Java" contentType="text/html;charset=UTF-8"%>
<%@ page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="com.fpi.bear.web.framework.service.AppSystemService"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="com.fpi.bear.web.framework.service.AppSystem"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录-聚光科技污染源在线监控系统</title>
<link type="text/css" href="style.css" rel="stylesheet" />
</head>

<body>
	<div class="wraps">
		<div class="login_dis">
			<h2>
				<img src="images/logo.jpg" width="445" height="123"
					alt="聚光科技污染源在线监控系统" />
			</h2>
			<ul class="ul2">
				<%
        	WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
			AppSystemService systemService = (AppSystemService) context.getBean("webFrameSystemService");
        	List<AppSystem> systems = systemService.getAllSystems();
        	int i = 0;
        	for (AppSystem system : systems) {
        	%>
				<li class="li<%=i%4 + 1%>"><a
					href="/system?code=<%=system.getCode() %>" class="a_img"><img
						src="<%=system.getLogo() %>" />
				</a> <a href="/system?code=<%=system.getCode() %>"><%=system.getName() %></a>
				</li>
				<% i++;
        	} 
        	%>
			</ul>
		</div>
	</div>
</body>
</html>
