package bbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

import db.DBClose;
import db.DBOpen;

public class BbsDAO {
	// 답변생성
	public boolean createReply(BbsDTO dto) {
		boolean flag = false;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		StringBuffer sql = new StringBuffer();

		sql.append(" INSERT INTO bbs(bbsno, wname, title, content, passwd, wdate, grpno, indent, ansnum ) ");
		sql.append(" VALUES((SELECT NVL(MAX(bbsno), 0) + 1 as bbsno FROM bbs), ");
		sql.append(" ?, ?, ?, ?, sysdate, ?, ? ,? )");

		try {
			// 여기서 값을 주지 않음
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, dto.getWname());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getPasswd());// 비번
			pstmt.setInt(5, dto.getGrpno());// 부모와 자식의 grpno는 동일해야함
			pstmt.setInt(6, dto.getIndent() + 1);// 부모보다 1증가해야하므로
			pstmt.setInt(7, dto.getAnsnum() + 1);// 부모보다 1증가해야하므로
			int cnt = pstmt.executeUpdate();
			if (cnt > 0)
				flag = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBClose.close(pstmt, con);
		}
		return flag;
	}

	// ansnum증가
	public void upAnsnum(Map map) {
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" update bbs ");
		sql.append(" set ansnum=ansnum+1 ");
		sql.append(" where grpno = ? ");
		sql.append(" and ansnum> ? ");

		int grpno = (Integer) map.get("grpno");
		int ansnum = (Integer) map.get("ansnum");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, grpno);
			pstmt.setInt(2, ansnum);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBClose.close(pstmt, con);
		}
	}

	// reply
	public BbsDTO readReply(int bbsno) {
		BbsDTO dto = null;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		sql.append(" select bbsno, grpno, title, indent, ansnum ");
		sql.append(" from bbs ");
		sql.append(" where bbsno=?  ");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, bbsno);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto = new BbsDTO();
				dto.setBbsno(rs.getInt("bbsno"));
				dto.setTitle(rs.getString("title"));
				dto.setGrpno(rs.getInt("grpno"));
				dto.setIndent(rs.getInt("indent"));
				dto.setAnsnum(rs.getInt("ansnum"));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBClose.close(rs, pstmt, con);
		}
		return dto;
	}

	//rownum
	public int total (String col, String word) {
		int total = 0;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("  select count(*) ");
		sql.append(" from bbs ");
		
		if(word.trim().length()>0 && col.equals("title_content")) {
			sql.append(" where title like '%'||?||'%' ");
			sql.append(" or content like '%'||?||'%' ");
		}else if(word.trim().length()>0) {
			sql.append(" where "+col+ " like '%'||?||'%' ");
		}
		
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			if(word.trim().length()>0 && col.equals("title_content")) {//sql문에 쓴 조건과 동일해야함
				pstmt.setString(1, word);
				pstmt.setString(2, word);
			}else if(word.trim().length()>0){
				pstmt.setString(1, word);
			}
					
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				total = rs.getInt(1);			
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBClose.close(rs, pstmt, con);
		}
		return total;
	}
	
	// list
	public List<BbsDTO> list(Map map) {
		List<BbsDTO> list = new ArrayList<BbsDTO>();
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String col = (String)map.get("col");
		String word = (String)map.get("word");
		
		int sno=(Integer)map.get("sno"); //rownum의시작번호
		int eno=(Integer)map.get("eno"); //rownum의끝번호
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select bbsno, wname, title, viewcnt, wdate, grpno, indent, ansnum , r  ");		
		sql.append(" from(  ");		
		sql.append("     select bbsno, wname, title, viewcnt, wdate, grpno, indent, ansnum , rownum r  ");		
		sql.append("        from(  ");						
		sql.append(" 			select bbsno, wname, title, viewcnt,to_char(wdate,'yyyy-mm-dd')wdate, grpno, indent, ansnum ");
		sql.append(" 			from bbs ");
		
		
		if(word.trim().length()>0 && col.equals("title_content")) {
			sql.append(" where title like '%'||?||'%' ");
			sql.append(" or content like '%'||?||'%' ");
		}else if(word.trim().length()>0) {
			sql.append(" where "+col+ " like '%'||?||'%' ");
		}
		sql.append(" order by grpno desc, ansnum asc ");
		sql.append(" )");
		sql.append("  )where r>=? and r<=? ");
		
		try {
			
			pstmt = con.prepareStatement(sql.toString());
			
			int i =0;
			if(word.trim().length()>0 && col.equals("title_content")) {//sql문에 쓴 조건과 동일해야함
				pstmt.setString(++i, word);
				pstmt.setString(++i, word);
			}else if(word.trim().length()>0){
				pstmt.setString(++i, word);
			}
			//if, else if의 조건에 따라 ?의 숫자가 정해지지 않기 때문에 변수로 지정해서 조건에 맞에 ?의 갯수가 생성되도록 함
			pstmt.setInt(++i, sno);
			pstmt.setInt(++i, eno);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BbsDTO dto = new BbsDTO();
				dto.setBbsno(rs.getInt("bbsno"));
				dto.setWname(rs.getString("wname"));
				dto.setWdate(rs.getString("wdate"));
				dto.setTitle(rs.getString("title"));
				dto.setGrpno(rs.getInt("grpno"));
				dto.setIndent(rs.getInt("indent"));
				dto.setAnsnum(rs.getInt("ansnum"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBClose.close(rs, pstmt, con);
		}
				
		return list;
	}

	// read
	public BbsDTO read(int bbsno) {
		BbsDTO dto = null;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		sql.append(" select bbsno, wname, title, content, viewcnt, ");
		sql.append(" to_char(wdate,'yyyy-mm-dd') wdate");
		sql.append(" from bbs ");
		sql.append(" where bbsno = ? ");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, bbsno);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto = new BbsDTO();
				dto.setBbsno(rs.getInt("bbsno"));
				dto.setWname(rs.getString("wname"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setViewcnt(rs.getInt("viewcnt"));
				dto.setWdate(rs.getString("wdate"));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBClose.close(rs, pstmt, con);
		}
		return dto;
	}

	// create
	public boolean create(BbsDTO dto) {
		boolean flag = false;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		StringBuffer sql = new StringBuffer();

		sql.append(" INSERT INTO bbs(bbsno, wname, title, content, passwd, wdate, grpno ) ");
		sql.append(" VALUES((SELECT NVL(MAX(bbsno), 0) + 1 as bbsno FROM bbs), ");
		sql.append(" ?, ?, ?, ?, sysdate, (SELECT NVL(MAX(grpno), 0) + 1 as bbsno FROM bbs)) ");

		try {
			// 여기서 값을 주지 않음
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, dto.getWname());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getPasswd());// 비번

			int cnt = pstmt.executeUpdate();
			if (cnt > 0)
				flag = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBClose.close(pstmt, con);
		}
		return flag;
	}

	// update
	public boolean update(BbsDTO dto) {
		boolean flag = false;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		sql.append(" update bbs ");
		sql.append(" set wname= ?, ");
		sql.append("     title= ?, ");
		sql.append("     content= ? ");
		sql.append(" where bbsno= ? ");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, dto.getWname());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setInt(4, dto.getBbsno());

			int cnt = pstmt.executeUpdate();
			if (cnt > 0)
				flag = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBClose.close(rs, pstmt, con);
		}
		return flag;
	}

	public boolean delete(int bbsno) {
		boolean flag = false;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		sql.append(" delete from bbs ");
		sql.append(" where bbsno = ?");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, bbsno);
			int cnt = pstmt.executeUpdate();
			if (cnt > 0)
				flag = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBClose.close(rs, pstmt, con);
		}
		return flag;
	}

	// passwd 체크
	public boolean passCheck(Map map) {
		boolean flag = false;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		sql.append(" select count(bbsno) as cnt ");
		sql.append(" from bbs");
		sql.append(" where bbsno=? ");
		sql.append(" and passwd=? ");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, (Integer) map.get("bbsno"));
			pstmt.setString(2, (String) map.get("passwd"));

			rs = pstmt.executeQuery();

			if (rs.next()) {
				int cnt = rs.getInt("cnt");
				if (cnt > 0)
					flag = true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBClose.close(rs, pstmt, con);
		}
		return flag;
	}

	public void upViewcnt(int bbsno) {
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" update bbs ");
		sql.append(" set viewcnt=viewcnt+1 ");
		sql.append(" where bbsno= ? ");

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, bbsno);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBClose.close(pstmt, con);
		}
	}
	
}
