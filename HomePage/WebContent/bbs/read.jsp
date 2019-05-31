<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/ssi/ssi_bbs.jsp" %>
<jsp:useBean id="dao" class="bbs.BbsDAO"/>
<%
	int bbsno = Integer.parseInt(request.getParameter("bbsno"));//list에서 선택시 bbsno가 넘어옴
	
	dao.upViewcnt(bbsno);//조회수 증가
	BbsDTO dto = dao.read(bbsno);//getParameter로 받은 bbsno가 들어감, 한건의 데이터를 가지고옴
	String content = dto.getContent().replaceAll("\r\n", "<br>");
	
%>

<!DOCTYPE html>
<html>
<head>
<title>Bootstrap Example</title>
<meta charset="utf-8">

<script type="text/javascript">
function updateM(){
	var url = "updateForm.jsp";
	//파라메터가 있을때 ?뒤에 써줌
	//rs.getInt("bbsno") =>rs는 사용할 수 없음 , 다 dto로 가져옴
	url += "?bbsno=<%=dto.getBbsno()%>";
	url +="&col=<%=request.getParameter("col")%>"; 	
	url +="&word=<%=request.getParameter("word")%>";  	
	url +="&nowPage=<%=request.getParameter("nowPage")%>";
	
	location.href=url;
}
function deleteM(){
	var url = "deleteForm.jsp";
	//파라메터가 있을때 ?뒤에 써줌
	url += "?bbsno=<%=dto.getBbsno()%>";
	url +="&col=<%=request.getParameter("col")%>"; 	
	url +="&word=<%=request.getParameter("word")%>";  	
	url +="&nowPage=<%=request.getParameter("nowPage")%>";
	location.href=url;
}
function replyM(){
	var url = "replyForm.jsp";
	//파라메터가 있을때 ?뒤에 써줌
	url += "?bbsno=<%=dto.getBbsno()%>";
	
	location.href=url;
}
function listM(){
	var url = "list.jsp";
	////?는 파라메터의 시작임을 알리는것으로 한번만 쓰면 됨
	url +="?col=<%=request.getParameter("col")%>"; 
	//&계속 이어진다는것을 알려줌
	url +="&word=<%=request.getParameter("word")%>";  
	//&계속 이어진다는것을 알려줌
	url +="&nowPage=<%=request.getParameter("nowPage")%>";
	
	location.href=url;
}
</script>
</head>
<body>
<jsp:include page="/menu/top.jsp"></jsp:include>
	<div class="container">

		<h2>조회</h2>
		<div class="panel panel-default">
			<div class="panel-heading">번호</div>
			<div class="panel-body"><%=dto.getBbsno()%></div>

			<div class="panel-heading">작성자</div>
			<div class="panel-body"><%=dto.getWname()%></div>

			<div class="panel-heading">제목</div>
			<div class="panel-body"><%=dto.getTitle()%></div>

			<div class="panel-heading">내용</div>
			<div class="panel-body"><%=dto.getContent()%></div>
			
			<div class="panel-heading">조회수</div>
			<div class="panel-body"><%=dto.getViewcnt()%></div>
			
			<div class="panel-heading">날짜</div>
			<div class="panel-body"><%=dto.getWdate()%></div>
		</div>

		<div>
			<button type="button" class="btn"
				onclick="location.href='./createForm.jsp'">등록</button><!-- 파라메터가 필요없기때문에 바로 링크로 걸어줌 -->
			<!-- 버튼을 누르면 자바스크립트함수를호출(파라메터필요) -->
			<button type="button" class="btn" onclick="updateM();">수정</button>
			<button type="button" class="btn" onclick="deleteM();">삭제</button>
			<button type="button" class="btn" onclick="replyM();">답변</button>
			<button type="button" class="btn" onclick="listM();">목록</button>
		</div>


	</div>



</body>
</html>
<!-- dao가 처리하기 때문에 ssi_close도 필요없음 -->
<!--include file="/ssi/ssi_close.jsp"  -->
