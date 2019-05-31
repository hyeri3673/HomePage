<%@ page contentType="text/html; charset=UTF-8"%>
<%
	String root = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<title>homepage</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<style type="text/css">
ul{
text-align: center;
}
{
background-color: #E6E6E6;
}
.page-header {color: black;}
</style>
</head>
<body>

	<div class="container-fluid">
		<div class="page-header row">
			<div class="col-sm-4">
				<a class="navbar-brand" href="#"><img
					src="<%=root%>/images/house.jpg" width="50" height="50" alt=""></a>
			</div>
			<div class="col-sm-5">
				<h1 style="text-align: center;">MWP</h1>
			</div>
			<div style="float: right;">
				<a href="#"><span class="glyphicon glyphicon-user"></span> Sign
					Up</a> <a href="#"><span class="glyphicon glyphicon-log-in"></span>
					Login </a>
			</div>
		</div>
		<div>
		<ul class="nav nav-tabs">
			<li class="dropdown"><a class="dropdown-toggle"
				data-toggle="dropdown" href="#"
				style="color: #0489B1; font-size: 16pt; font-weight: bold;">게시판<span
					class="caret"></span></a>
				<ul class="dropdown-menu">
					<li><a href="<%=root%>/bbs/createForm.jsp">생성</a></li>
					<li><a href="<%=root%>/bbs/list.jsp">목록</a></li>
				</ul></li>


			<li class="dropdown"><a class="dropdown-toggle"
				data-toggle="dropdown" href="#"
				style="color: #848484; font-size: 16pt; font-weight: bold;">방명록<span
					class="caret"></span></a>
				<ul class="dropdown-menu">
					<li><a href="<%=root%>/guestbook/createForm.jsp">생성</a></li>
					<li><a href="<%=root%>/guestbook/list.jsp">목록</a></li>
				</ul></li>
		</ul>
		</div>
	</div>



</body>
</html>