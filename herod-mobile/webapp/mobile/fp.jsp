<%@ page language="Java" contentType="text/html;charset=UTF-8"%>
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
<script type="text/javascript"
	src="http://jzaefferer.github.com/jquery-validation/jquery.validate.js"></script>
<script
	src="http://code.jquery.com/mobile/1.3.1/jquery.mobile-1.3.1.min.js"></script>
<script src="js/jquery.cookie.js"></script>

</head>
<body>

	<div data-role="page" data-theme="b" id="fp" data-title="废品回收站">

		<div data-role="header" data-theme="b">
			<h1>废品回收站</h1>
			<a href="index.html" data-icon="home" data-iconpos="notext"
				data-direction="reverse">首页</a> <a href="tel:10000"
				data-icon="dialing" data-iconpos="notext" data-direction="reverse">联系我们</a>
		</div>
		<!-- /header -->

		<div data-role="content" data-theme="b">
			<form id="fpOrderForm" action="fp_order_submit.jsp" method="post">
				<label for="basic"><em>* </em>联系电话：</label> <input type="tel"
					name="phone" id="phone" data-mini="true" required /> <label
					for="basic">联系地址：</label> <input name="address" id="address"
					data-mini="true" /> <label for="select-choice-0" class="select">废品类型</label>
				<select name="type" id="type">
					<option value="standard">家用电器</option>
					<option value="rush">日用百货</option>
					<option value="express">家具</option>
				</select> <label for="datetime-l">希望上门日期时间：</label> <input
					type="datetime-local" name="time" id="time" value="" /> <label
					for="textarea-a">备注说明：</label>
				<textarea name="comment" id="comment"></textarea>
				<button type="submit" data-theme="b" name="submit"
					value="submit-value" data-rel="dialog" data-transition="pop">提交</button>
				<a href="index.html" data-role="button" data-icon="back"
					data-theme="a">取消</a>
			</form>
		</div>
		<!-- /content -->
		<script type="text/javascript">
			$('#fp').bind('pageinit', function() {
				$('#phone').val($.cookie('phone'));
				$('#address').val($.cookie('address'));
				$("#fpOrderForm").validate({
					rules : {
						phone : "required",
					},
					messages : {
						phone : "请输入联系电话！"

					},
					submitHandler : function(form) {
						$.cookie('phone', $('#phone').val());
						$.cookie('address', $('#address').val());
						form.submit();
					}

				});
			});
			$('#fp').bind('pageshow', function() {
			});
		</script>
	</div>
	<!-- /page -->

</body>

</html>