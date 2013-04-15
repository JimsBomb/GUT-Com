<!-- 左边栏菜单页 -->
<html>
<script type="text/javascript">
	$(document).ready(function() {
		/* 子菜单收缩/展开 */
		$(".menu").click(function() {
			var id = this.id;
			id = "#" + id + " ul";
			$(id).toggle(500); // 延时动画
		});
	});
</script>
</head>
<body>
<ul>
	<li id="la" class="menu">a
		<ul>
			<li id="p1.jsp" class="item">a1</li>
			<li id="p2.jsp" class="item">a2</li>
		</ul>
	</li>
	<li id="lb" class="menu">b
		<ul>
			<li>b1</li>
			<li>b2</li>
		</ul>
	</li>
</ul>
</body>
</html>