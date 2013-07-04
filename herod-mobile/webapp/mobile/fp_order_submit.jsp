<%@ page language="Java" contentType="text/html;charset=UTF-8"%>
<%
	System.out.println("Submitted!");
%>
<!DOCTYPE html>
<html>
<head>
<title>废品回收中心</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://code.jquery.com/mobile/1.3.1/jquery.mobile-1.3.1.min.css" />
<link rel="stylesheet" href="css/fp.css" />
<script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
<script
	src="http://code.jquery.com/mobile/1.3.1/jquery.mobile-1.3.1.min.js"></script>
</head>

<body>

	<div data-role="dialog" data-theme="b" id="fp" data-title="废品回收站">

		<div data-role="header" data-theme="b">
			<h3>废品回收订单</h3>
		</div>
		<!-- /header -->

		<div data-role="content" data-theme="b">
			<h1>提交成功！</h1>
		</div>
		<!-- /content -->

	</div>
	<!-- /page -->

</body>
</html>