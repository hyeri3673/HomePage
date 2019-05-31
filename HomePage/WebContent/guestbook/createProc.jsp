<%@ page contentType="text/html; charset=UTF-8"%>

<%@ include file="/ssi/ssi_guestbook.jsp"%>

<jsp:useBean id="dao" class="guestbook.GuestBookDAO" />

<jsp:useBean id="dto" class="guestbook.GuestBookDTO" />

<jsp:setProperty name="dto" property="*" />

<!DOCTYPE html>

<%
	boolean flag = dao.create(dto);
%>

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

</head>

<body>

	<div class="container">

		<fieldset>

			<legend>방명록 확인</legend>

			<%
				if (flag)

					out.print("방명록을 생성했습니다. ");

				else

					out.print("방명록 생성을 실패했습니다. ");
			%>

		</fieldset>

		<button class="btn" onclick="location.href='createForm.jsp'">다시등록</button>

		<button class="btn" onclick="location.href='list.jsp'">목록</button>

	</div>

</body>

</html>