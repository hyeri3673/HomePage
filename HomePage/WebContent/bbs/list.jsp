<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/ssi/ssi_bbs.jsp"%>
<jsp:useBean id="dao" class="bbs.BbsDAO"></jsp:useBean>
<%
	//검색관련
	String col = Utility.checkNull(request.getParameter("col"));
	String word = Utility.checkNull(request.getParameter("word"));

	if (col.equals("total"))
		word = "";
	
	//페이지관련
	int nowPage = 1;//현재보고있는페이지
	if(request.getParameter("nowPage")!=null){//다른페이지를 눌렀을때 그 페이지번호를 넣어주려고
		nowPage = Integer.parseInt(request.getParameter("nowPage"));
	}
	int recordPerPage = 3;//한페이지당 보여줄 레코드 갯수
	
	//DB에서 가져올 순번 ,1페이지라면 레코드를 1,2,3을 가지고 옴  2페이지라면 sno=4부터 eno =6   4,5,6의 레코드를 가지고 옴
	int sno = ((nowPage-1)*recordPerPage)+1;
	int eno = nowPage*recordPerPage;
	
	Map map = new HashMap();
	map.put("col", col);
	map.put("word", word);
	map.put("sno", sno);
	map.put("eno", eno);
	
	//전체레코드의 수
	int total = dao.total(col,word);
	
	List<BbsDTO> list = dao.list(map);
%>
<!DOCTYPE html>
<html>
<head>
<title>homepage</title>
<meta charset="utf-8">
<script type="text/javascript">
	function read(bbsno) {
		var url = "read.jsp";
		//bbsno,url : java script 변수
		url += "?bbsno=" + bbsno;
		//jsp변수 : col, word, now인데 자바스크립트안에서 사용할때는 스크립트릿으로 감싸야 사용할 수 있음
		url += "&col=<%=col%>";
		url += "&word=<%=word%>";
		url += "&nowPage=<%=nowPage%>";
		location.href = url;
	}
</script>
</head>
<body>
	<jsp:include page="/menu/top.jsp"></jsp:include>
	<div class="container">
		<h2>게시판 목록</h2>
		<br>
		<!-- 검색 -->
		<form class="form-inline" action="./list.jsp">
			<div class="form-group">
				<select class="form-control" name="col">
					<option value="wname"
					<%if(col.equals("wname")) out.print("selected"); %>
					>성명</option>
					<option value="title"
					<%if(col.equals("title")) out.print("selected"); %>
					>제목</option>
					<option value="content"
					<%if(col.equals("content")) out.print("selected"); %>
					>내용</option>
					<option value="title_content"
					<%if(col.equals("title_content")) out.print("selected"); %>
					>제목+내용</option>
					<option value="total"
					<%if(col.equals("total")) out.print("selected"); %>
					>전체출력</option>
				</select>
			</div>
			<div class="form-group">
				<input type="text" class="form-control" placeholder="Enter 검색어"
					value="<%=word%>" name="word">
			</div>
			
			
			
			<button type="submit" class="btn btn-default">검색</button>
			<button type="button" class="btn btn-default">등록</button>
		</form>


		<table class="table table-striped">
			<thead>
				<tr>
					<th>번호</th>
					<th>제목</th>
					<th>작성자</th>
					<th>grpno</th>
					<th>indent</th>
					<th>ansnum</th>
				</tr>
			</thead>
			<tbody>
				<%
					if (list.size() == 0) {
				%>
				<tr>
					<td colspan="4">등록된 게시판이 없습니다.</td>
					<%
						} else {//list에 있는 게시판의 글들을 다 가져오기 위해서 list의 크기만큼 반복문을 사용
							for (int i = 0; i < list.size(); i++) {
								//dto의 값들에 정보를 넣어줌
								BbsDTO dto = list.get(i);
					%>
				
				<tr>
					<td><%=dto.getBbsno()%></td>
					<!-- 제목을 눌렀을때 해당제목이 가지고 있는 read의 내용으로 이동,답변을 달았을경우 들여쓰기를함 링크가 생성되기전에 들여쓰기가 되어야함 -->
					<td>
						<%
							for (int r = 0; r < dto.getIndent(); r++) {
										//두칸이상 띄어쓰기를 해도 한칸만 되기 때문에 한칸을 나타내는 태그를 사용하여 들여쓰기를 함
										out.print("&nbsp;&nbsp;");
									}
									if (dto.getIndent() > 0)//최초의 부모의 indent의 값은 0이기때문에 0보다 크면 무조건 자식임을 알 수 있음
										out.print("<img src='../images/re.jpg'>");
						%> <a href="javascript:read('<%=dto.getBbsno()%>')"><%=dto.getTitle()%></a>
						<% if(Utility.compareDay(dto.getWdate().substring(0,10))) {%>
							<img src="../images/new.gif">  <!-- 스크립트릿안에 안쓰였기때문에 out.print가 필요없음 -->
						<%} %>
					</td>
					<td><%=dto.getWname()%></td>
					<td><%=dto.getGrpno()%></td>
					<td><%=dto.getIndent()%></td>
					<td><%=dto.getAnsnum()%></td>
				</tr>
				<%
					}
					}
				%>

			</tbody>
		</table>
		<div>
			<!-- 전체페이지, 현재페이지, 한페이지당 보여줄 레코드갯수, 검색했던것을 유지하기 위해서 열과 단어를 가지고 옴 -->
			<%=Utility.paging(total, nowPage, recordPerPage, col, word) %>
		</div>
	</div>

</body>
</html>