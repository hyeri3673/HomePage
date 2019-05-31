<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/ssi/ssi_guestbook.jsp"%>
<jsp:useBean id="dao" class="guestbook.GuestBookDAO"></jsp:useBean>
<%
	//col,word에 값이 있는지 먼저 확인함
	String col = Utility.checkNull(request.getParameter("col"));
	String word = Utility.checkNull(request.getParameter("word"));
	//전체출력을 선택했다면 검색할 필요가 없으므로 검색창을 빈문자열로 변경
	if (col.equals("total")) {
		word = "";
	}
	int nowPage = 1;
	if(request.getParameter("nowPage")!=null){
		nowPage = Integer.parseInt(request.getParameter("nowPage"));	
	}
	int recordPerPage = 3;//페이지에 나타난 전체 레코드수
	
	int sno = (nowPage-1)*recordPerPage +1;
	int eno = nowPage * recordPerPage;
	//map에 key를 col로 한것의 값을 div의 col의 내용으로 함
	
	Map map = new HashMap();
	map.put("col", col);
	map.put("word", word);
	map.put("sno", sno);
	map.put("eno", eno);
	
	int total = dao.total(map);
	List<GuestBookDTO> list = dao.list(map);
%>
<!DOCTYPE html>
<html>
<head>
<title>방명록</title>
<meta charset="utf-8">

<script type="text/javascript">
	function read(no) {
		var url = "read.jsp";
		url += "?no=" + no;
		location.href = url;
	}
	function update(no) {
		var url = "updateForm.jsp";
		url += "?no=" + no;
		location.href = url;
	}
	function del(no) {
		if (confirm("정말 삭제 하시겠습니까?")) {
			var url = "deleteProc.jsp";
			url += "?no=" + no;
			location.href = url;
		}
	}
	function listM(){
		var url ="list.jsp";
		url += "?col=<%=col%>";
		url += "&word=<%=word%>";
		url += "&nowPage=<%=nowPage%>";
	}
</script>
<style type="text/css">
</style>
</head>
<body>
<jsp:include page="/menu/top.jsp"></jsp:include>
	<div class="container">
		<h2>방명록</h2>
		<form class="form-inline" method="post" action="list.jsp">
			<!-- 선택 -->
			<div class="form-group">
				<select class="form-control" name="col">
					<option value="title"
					<%//겁색버튼을 눌러도 선택한것이 계속 유지되도록 하기위해서
					if(col.equals("title"))
						out.print("selected");
					%>
					>제목</option>
					<option value="name"
					<%if(col.equals("name"))
						out.print("selected");
					%>
					>이름</option>
					<option value="part"
					<%if(col.equals("part"))
						out.print("selected");
					%>
					>관계</option>
					<option value="title_name"
					<%if(col.equals("title_name"))
						out.print("selected");
					%>
					>제목+이름</option>
					<option value="total"
					<%if(col.equals("total"))
						out.print("selected");
					%>
					>전체출력</option>
				
				</select>
			
			</div>
			<!-- 검색 -->
			<div class="form-group">
				<input type="text" class = "form-control" name="word" value="<%=word %>">
			</div>
			<!-- 검색을 눌러도 list.jsp에서 결과를 봄 -->
			<button class="btn btn-default">검색</button>
			<button class="btn btn-default" type="button" onclick="location.href='createForm.jsp'">등록</button>
		</form>
		<table class="table table-striped">
			<thead align="center">
				<tr>
					<th>번호</th>
					<th>제목</th>
					<th>이름</th>
					<th>관계</th>
					<th>날짜</th>
					<th>grpno</th>
					<th>indent</th>
					<th>ansnum</th>
					<th>수정/삭제</th>
				</tr>
			</thead>
			<tbody>
				<%
					if (list.size() == 0) {
				%>
				<tr>
					<td colspan="5">등록된 방명록이 없습니다.</td>
				</tr>
				<%
					} else {
						for (int i = 0; i < list.size(); i++) {
							GuestBookDTO dto = list.get(i);
				%>
				<tr>
					<td><%=dto.getNo()%></td>
					<!-- 이름은 누르면 read로 이동하여 이름에 해당하는 내용이 보여지도록 -->
					<td>
					<% for(int r=0;r<dto.getIndent();r++){
						out.print("&nbsp;&nbsp;");
					}
					if(dto.getIndent()>0){
						out.print("<img src='../images/re.jpg'>");
					}
					%>
					
					<a href="javascript:read('<%=dto.getNo()%>')"><%=dto.getTitle()%></a></td>
					<td><%=dto.getName()%></td>
					<td><%=dto.getPartstr()%></td>
					<td><%=dto.getRegdate()%></td>
					<td><%=dto.getGrpno() %></td>
					<td><%=dto.getIndent() %></td>
					<td><%=dto.getAnsnum() %></td>
					<td><a href="javascript:update('<%=dto.getNo()%>')">수정/</a> <a
						href="javascript:del('<%=dto.getNo()%>')">삭제</a></td>
				</tr>
				<%
					}
					}
				%>
			</tbody>
		</table>
		<%=Utility.paging(total, nowPage, recordPerPage, col, word) %>
	</div>

</body>
</html>