<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ include file="/ssi/ssi_guestbook.jsp" %>
<jsp:useBean id="dao" class="guestbook.GuestBookDAO"></jsp:useBean>
<%
	int no = Integer.parseInt(request.getParameter("no"));
	GuestBookDTO dto = dao.read(no);
	
	String part = dto.getPartstr();
%>
<!DOCTYPE html>
<html>
<head>
  <title>방명록 수정</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
  <style type="text/css">#divbtn{text-align: center;}</style>
</head>
<body>
  
	<div class="container">
		<h1 class="col-sm-offset-2 col-sm-10">방명록 수정</h1>
		<form class="form-horizontal" method="post"action="updateProc.jsp" name = "frm">
			<input type="hidden" name="no" value="<%=dto.getNo() %>">
			<input type="hidden" name="col" value="<%=request.getParameter("col") %>">
			<input type="hidden" name="word" value="<%=request.getParameter("word") %>">
			<input type="hidden" name="nowPage" value="<%=request.getParameter("nowPage") %>">
			<!-- 제목 (수정)분야내용-->
			<div class="form-group">
				<label class="control-label col-sm-2" for="title">제목</label>
				<div class="col-sm-5">
					<input type="text" class="form-control" id="title" value="<%=dto.getTitle() %>" name="title">			
				</div>
			</div>
			<!-- 이름(수정안함) -->
			<div class="form-group">
				<label class="control-label col-sm-2" for="name">이름</label>
				<div class="col-sm-6">
					<%=dto.getName() %>
				</div>
			</div>
			<!-- 내용(수정) -->
			<div class="form-group">
				<label class="control-label col-sm-2" for = gender>내용</label>
				<div class="col-sm-6">
					<textarea rows="5" cols="5" id="content" class="form-control" name="content"><%=dto.getContent() %></textarea> 
				</div>
			</div>
			<!-- 관계(수정) -->
			<div class="form-group">
				<label class="control-label col-sm-2" for="part">관계</label>
				<div class="col-sm-10">
					<label class="checkbox-inline">
						<input type="checkbox" name="part" value="초등학교 친구"
						<%if (part.indexOf("초등학교 친구")!=-1) 
							out.print("checked");%>>초등학교친구
					</label>
					<label class="checkbox-inline">
						<input type="checkbox" name="part" value="중학교친구"
						<%if (part.indexOf("중학교친구")!=-1) 
							out.print("checked");%>>중학교친구
					</label>
					<label class="checkbox-inline">
						<input type="checkbox" name="part" value="고등학교친구"
						<%if (part.indexOf("고등학교친구")!=-1) 
							out.print("checked");%>>고등학교친구
					</label>
					<label class="checkbox-inline">
						<input type="checkbox" name="part" value="대학교친구"
						<%if (part.indexOf("대학교친구")!=-1) 
							out.print("checked");%>>대학교친구
					</label>
					<label class="checkbox-inline">
						<input type="checkbox" name="part" value="제3자"
						<%if (part.indexOf("제3자")!=-1) 
							out.print("checked");%>>제3자
					</label>

				</div>	
			</div>
			<div class="form-group" id="divbtn">
				<div class="col-sm-offsest-2 col-sm-5">
					<button class="btn">수정</button>
					<button type="reset" class="btn">취소</button>
				</div>
			</div>
		</form>
	</div>

</body>
</html>