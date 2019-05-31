<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ include file="/ssi/ssi_bbs.jsp" %>
<jsp:useBean id="dao" class="bbs.BbsDAO"></jsp:useBean>
<%
	int bbsno = Integer.parseInt(request.getParameter("bbsno"));
	BbsDTO dto = dao.read(bbsno);//수정할 bbsno의 하나의 레코드를 가져옴
%>
<!DOCTYPE html>
<html>
<head>
  <title>bbs 수정</title>
  <meta charset="utf-8">

</head>
<body>
 <jsp:include page="/menu/top.jsp"></jsp:include>
	<div class="container">
		<h1 class="col-sm-offset-2 col-sm-10">게시판 수정</h1>
		<!-- form에 대한 내용을 다 들고  proc로 넘어감 -->
		<form class="form-horizontal" method="post" action="updateProc.jsp">
		  <input type="hidden" name="bbsno" value ="<%=dto.getBbsno() %>">
		  <input type="hidden" name="col" value ="<%=request.getParameter("col") %>">
		  <input type="hidden" name="word" value ="<%=request.getParameter("word") %>">
		  <input type="hidden" name="nowPage" value ="<%=request.getParameter("nowPage") %>">
		  <!-- 그전의 값들을 가져오기 위해서 value를 사용하여 값을 들고옴 -->
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="wname">작성자</label>
		    <div class="col-sm-8">
		      <input type="text" name = "wname" id="wname" class="form-control" value ="<%=dto.getWname()%>">
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="title">제목</label>
		    <div class="col-sm-8">
		      <input type="text" name = "title" id="title" class="form-control" value ="<%=dto.getTitle()%>">
		    </div>
		  </div>
		  <!-- textarea는 value로 값을 받지않고 textarea사이에서 값을 들고와야함 -->
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="content">내용</label>
		    <div class="col-sm-8">
		      <textarea rows="12" cols="7" id="content" name="content" class="form-control"><%=dto.getContent() %></textarea>
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="passwd">비밀번호</label>
		    <div class="col-sm-2">
		      <input type="password" name = "passwd" id="passwd" class="form-control">
		    </div>
		  </div>
		  
		  <div class = "form-group">
       <div class="col-sm-offset-2 col-sm-5">
         <button class="btn">수정</button>
         <button type="reset" class="btn">취소</button>
       </div>
      </div>
      
      
		</form>
	</div>

</body>
</html>