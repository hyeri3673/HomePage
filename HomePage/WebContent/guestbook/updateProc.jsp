<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/ssi/ssi_guestbook.jsp"%>
<jsp:useBean id="dao" class="guestbook.GuestBookDAO"></jsp:useBean>
<jsp:useBean id="dto" class="guestbook.GuestBookDTO"></jsp:useBean>
<jsp:setProperty property="*" name="dto" />
<%
	boolean flag = dao.update(dto);
%>
<!DOCTYPE html>
<html>
<head>
<title>Bootstrap Example</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<script type="text/javascript">
  function listM(){
		var url = "list.jsp";
		url +="?col=<%=request.getParameter("col")%>";
		url +="?word=<%=request.getParameter("word")%>";
		url +="?nowPage=<%=request.getParameter("nowPage")%>
	";

		location.href = url;
	}
</script>
</head>
<body>

	<div class="container">
		<fieldset>
			<legend>방명록 수정 확인</legend>
			<%
				if (flag) {
					out.print("방명록 수정을 했습니다.");
				} else {
					out.print("방명록 수정을 실패했습니다.");
				}
			%>
		</fieldset>
		<button class="btn" type="button"
			onclick="location.href='createForm.jsp'">다시등록</button>
		<button class="btn" type="button" onclick="listM();">목록</button>
	</div>

</body>
</html>