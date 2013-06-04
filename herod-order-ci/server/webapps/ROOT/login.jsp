<%@ page language="Java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.fpi.bear.permission.BearSecurityUtils" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录-聚光科技污染源在线监控系统</title>
<link type="text/css" href="style.css" rel="stylesheet" />
</head>
<%
if (BearSecurityUtils.getSecurityUser().isAuthenticated()) {
	response.sendRedirect("/select.jsp");
}
%>
<body>
	<div class="wraps">
		<div class="login_box">
			<form method="post" action="/login.jsp">
				<div class="login_box_con">
					<ul class="from_box">
						<li><input type="text" class="inp_01" id="username"
							name="username" />
						</li>
						<li><input type="password" class="inp_01" id="password"
							name="password" />
						</li>
						<li><input type="submit" class="inp_02" value="登录" />
						</li>
					</ul>
					<%
					Object shiroLoginFailure = request.getAttribute("shiroLoginFailure");
					if (shiroLoginFailure != null) {
					%>
					<p>错误提示：用户不存在或密码错误！</p>
					<%
					}
					%>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
