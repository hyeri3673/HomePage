<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/ssi/ssi_bbs.jsp"%>
<jsp:useBean id="dao" class="bbs.BbsDAO"></jsp:useBean>

<%
	int bbsno = Integer.parseInt(request.getParameter("bbsno"));
	String passwd = request.getParameter("passwd");
	Map map = new HashMap();
	map.put("bbsno", bbsno);
	map.put("passwd", passwd);
	boolean flag = false;
	boolean pflag = dao.passCheck(map);
	if (pflag) {
		flag = dao.delete(bbsno);
	}
%>
<!DOCTYPE html>
<html>
<head>
<title>bbs삭제</title>
<meta charset="utf-8">
<script>
function listM(){
	var url = "list.jsp";
	url +="?col=<%=request.getParameter("col")%>";
	url +="&word=<%=request.getParameter("word")%>";
	url +="&nowPage=<%=request.getParameter("nowPage")%>";
	
	location.href=url;
}
</script>
</head>
<body>
	<jsp:include page="/menu/top.jsp"></jsp:include>
	<div class="container">
		<div class="well well-lg">
			<%
				if (!pflag) {
					out.print("잘못된 비밀번호입니다.");
				} else if (flag) {
					out.print("글 삭제 성공입니다.");
				} else {
					out.print("글 삭제 실패입니다.");
				}
			%>

		</div>
		<!--  잘못된 비밀번호를 쳤을때만 다시시도의 버튼이 나옴 -->
		<%
			if (!pflag) {
		%>
		<button class="btn" onclick="history.back()">다시시도</button>
		<%
			}
		%>
		<button class="btn" onclick="location.href='./createForm.jsp'">다시등록</button>
		<button type="button" class="btn" onclick="listM()">목록</button>
	</div>
</body>
</html>