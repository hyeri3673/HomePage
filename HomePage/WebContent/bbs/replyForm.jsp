<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ include file="/ssi/ssi_bbs.jsp" %>
<jsp:useBean id="dao" class="bbs.BbsDAO"></jsp:useBean>
<%
	int bbsno = Integer.parseInt(request.getParameter("bbsno"));
	BbsDTO dto = dao.readReply(bbsno);
%>
<!DOCTYPE html>
<html>
<head>
  <title>homepage</title>
  <meta charset="utf-8">

</head>
<body>
 <jsp:include page="/menu/top.jsp"></jsp:include>
	<div class="container">
		<h1 class="col-sm-offset-2 col-sm-10">게시판 답변</h1>
		<form class="form-horizontal" method="post" action="replyProc.jsp">
		<!-- 부모글인지 확인하기 위해서 가지고 감 -->
		  <input type="hidden" name ="bbsno" value="<%=dto.getBbsno()  %>">
		<!--  -->
		  <input type="hidden" name ="grpno" value="<%=dto.getGrpno()  %>">
		  <input type="hidden" name ="indent" value="<%=dto.getIndent()  %>">
		  <input type="hidden" name ="ansnum" value="<%=dto.getAnsnum()  %>">
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="wname">작성자</label>
		    <div class="col-sm-8">
		      <input type="text" name = "wname" id="wname" class="form-control">
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="title">제목</label>
		    <div class="col-sm-8">
		      <input type="text" name = "title" id="title" class="form-control" value="<%=dto.getTitle()%>">
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="content">내용</label>
		    <div class="col-sm-8">
		      <textarea rows="12" cols="7" id="content" name="content" class="form-control"></textarea>
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
         <button class="btn">등록</button>
         <button type="reset" class="btn">취소</button>
       </div>
      </div>
      
      
		</form>
	</div>

</body>
</html>