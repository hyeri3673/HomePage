package utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

public class Utility {
	// 문자체크
	public static String checkNull(String str) {
		if (str == null) {
			str = "";
		}
		return str;
	}

	/**
	 * 
	 * @param totalRecord
	 *            전체 레코드수
	 * @param nowPage
	 *            현재 페이지
	 * @param recordPerPage
	 *            페이지당 레코드 수
	 * @param col
	 *            검색 컬럼
	 * @param word
	 *            검색어
	 * @return 페이징 생성 문자열
	 */

	public static String paging(int totalRecord, int nowPage, int recordPerPage, String col, String word) {

		int pagePerBlock = 5; // 블럭당 페이지 수 , rownum을 한번에 몇개까지 보여줄건지(1~5가 나오고 다음을 눌러야 6~10이 나옴)
		int totalPage = (int) (Math.ceil((double) totalRecord / recordPerPage)); // 전체 페이지 = 전체레코드/페이지 당 보여줄 레코드,
																					// 안나누어떨어지면 올림으로 함(ceil)
		int totalGrp = (int) (Math.ceil((double) totalPage / pagePerBlock));// 전체 그룹 전체페이지/한페이지당 보여줄 rownum ,안나누어 떨어져도
																			// 올림 123 456 78 (3블럭)
		int nowGrp = (int) (Math.ceil((double) nowPage / pagePerBlock)); // 현재 그룹 현재페이지/한페이당보여줄 rounum, 2페이지/3블럭 = 1번째
																			// 블럭에 있음(올림) 6페이지/2블럭 = 3
		int startPage = ((nowGrp - 1) * pagePerBlock) + 1; // 특정 그룹의 페이지 목록 시작
		int endPage = (nowGrp * pagePerBlock); // 특정 그룹의 페이지 목록 종료

		StringBuffer str = new StringBuffer();
		str.append("<div style='text-align:center'>");
		str.append("<ul class='pagination'> ");// 페이지하단 번호의 모양을 사각형으로 지정

		int _nowPage = (nowGrp - 1) * pagePerBlock; // 10개 이전 페이지로 이동 pagePerBlock=5일때 1그룹은 1~5의 rownum을 가짐,
		// 2그룹(6~10)-1 * 5 = 5 = > 2그룹에서 이전을 눌렀을때 이동하는곳(현재그룹에서 이전그룹의 맨 마지막 페이지를 가리킴)
		if (nowGrp >= 2) { // 내가 보고 있는 그룹이 2그룹이면(6~10)
			str.append(
					"<li><a href='./list.jsp?col=" + col + "&word=" + word + "&nowPage=" + _nowPage + "'>이전</A></li>");
		}

		for (int i = startPage; i <= endPage; i++) {
			if (i > totalPage) { // endPage가 전체페이지보다 커지면 반복을 끝냄
				break;
			}
			if (nowPage == i) {
				str.append("<li class='active'><a href=#>" + i + "</a></li>");
			} else {
				// 현재페이지가 아닌곳에서 col과 word를 가지고 감
				str.append("<li><a href='./list.jsp?col=" + col + "&word=" + word + "&nowPage=" + i + "'>" + i
						+ "</A></li>");
			}
		}
		// 2그룹 (6~10)* 5 = 10+1 =>11페이지로 이동(2그룹에서 다음을 눌렀을때 이동하는 곳, 현재그룹에서 다음그룹의 첫번쨰 페이지를
		// 가리킴)
		_nowPage = (nowGrp * pagePerBlock) + 1; // 10개 다음 페이지로 이동
		if (nowGrp < totalGrp) {
			str.append(
					"<li><A href='./list.jsp?col=" + col + "&word=" + word + "&nowPage=" + _nowPage + "'>다음</A></li>");
		}
		str.append("</ul>");
		str.append("</div>");
		return str.toString();
	}

	/**
	 * 등록날짜와 오늘,어제,그제날짜와 비교
	 * 
	 * @param wdate
	 *            - 등록날짜
	 * @return - true:오늘,어제,그제중 등록날짜와 같음 false:오늘,어제,그제 날짜가 등록날짜와 다 다름
	 */
	public static boolean compareDay(String wdate) {
		boolean flag = false;
		List<String> list = getDay();
		// 오늘 0 ,어제 1, 그제 2
		if (wdate.equals(list.get(0)) || wdate.equals(list.get(1)) || wdate.endsWith(list.get(2))) {
			flag = true;
		}
		return flag;
	}

	private static List<String> getDay() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		// getInstance() : 현재위치에 맞는 날짜정보를 제공
		Calendar cal = Calendar.getInstance();
		// 반목문을 돌면서 오늘 - 어제 - 그제 순으로 저장이 됨
		for (int j = 0; j < 3; j++) {
			list.add(sd.format(cal.getTime()));
			cal.add(Calendar.DATE, -1);// Calendar.DATE 어떤형식을 변경할지를 알려주는것
		}
		return list;
	}

	public static String getCodeValue(String code) {
		String value = null;

		Hashtable codes = new Hashtable();

		codes.put("A01", "회사원");
		codes.put("A02", "전산관련직");
		codes.put("A03", "연구전문직");
		codes.put("A04", "각종학교학생");
		codes.put("A05", "일반자영업");
		codes.put("A06", "공무원");
		codes.put("A07", "의료인");
		codes.put("A08", "법조인");
		codes.put("A09", "종교/언론/예술인");
		codes.put("A10", "기타");

		value = (String) codes.get(code);
		return value;

	}
}
