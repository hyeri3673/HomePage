package guestbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import db.DBClose;
import db.DBOpen;

public class GuestBookDAO {
	//CRUDL   CRU - boolean    R - dto  L - list
	
	//list
	public List<GuestBookDTO> list(Map map){
		List<GuestBookDTO> list = new ArrayList<GuestBookDTO>();
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String col = (String)map.get("col");
		String word = (String)map.get("word");
		int sno = (Integer)map.get("sno");
		int eno = (Integer)map.get("eno");
		
		StringBuffer sql = new StringBuffer();
		//세번중첩 이유 : r의 조건에서 r이 1부터 시작을 하지 않으면 제대로된 결과값을 가질 수 없음(버그)
		sql.append(" select no, name, title, part, regdate, ");
		sql.append(" grpno, indent, ansnum,  r ");
		sql.append(" from( ");		
		sql.append(" 	select no, name, title, part, regdate, ");
		sql.append(" 	grpno, indent, ansnum, rownum r ");
		sql.append(" 	from ( ");
		sql.append(" 			select no, name, title, part, to_char(regdate,'yyyy-mm-dd')regdate,  ");
		sql.append(" 			grpno, indent, ansnum ");
		sql.append(" 			from guestbook ");
		
		if(word.trim().length()>0&& col.equals("title_name")) {
			sql.append(" where title like '%'||?||'%' ");
			sql.append(" or name like '%'||?||'%' ");
		}else if(word.trim().length()>0) {
			sql.append(" where "+col+" like '%'||?||'%' ");
		}
		sql.append(" order by grpno desc, ansnum ");
		sql.append(" ) ");
		sql.append(" ) where r>=? and r<=? ");
		try {
			pstmt = con.prepareStatement(sql.toString());
			int i=0;
			if(word.trim().length()>0&& col.equals("title_name")) {
				pstmt.setString(++i, word);
				pstmt.setString(++i, word);
			}
			else if(word.trim().length()>0) {
				pstmt.setString(++i, word);
			}
			pstmt.setInt(++i, sno);
			pstmt.setInt(++i, eno);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				GuestBookDTO dto = new GuestBookDTO();
				dto.setNo(rs.getInt("no"));
				dto.setName(rs.getString("name"));
				dto.setTitle(rs.getString("title"));
				dto.setPartstr(rs.getString("part"));
				dto.setRegdate(rs.getString("regdate"));
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
	
	//read
	public GuestBookDTO read(int no) {
		GuestBookDTO dto = null;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select no,name,gender,title,content, part, to_char(regdate,'yyyy-mm-dd') regdate ");
		sql.append(" from guestbook ");
		sql.append(" where no = ? ");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, no);
			rs  = pstmt.executeQuery();
			
			if(rs.next()) {
				dto =new GuestBookDTO();
				dto.setNo(rs.getInt("no"));
				dto.setName(rs.getString("name"));
				dto.setGender(rs.getString("gender"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setPartstr(rs.getString("part"));
				dto.setRegdate(rs.getString("regdate"));
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBClose.close(rs, pstmt, con);
		}
		
		return dto;
		
	}
	
	//create
	public boolean create(GuestBookDTO dto) {
		boolean flag = false;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into guestbook(no, name, gender, ");
		sql.append(" title, content, part, passwd, regdate, grpno) ");
		sql.append(" values((select nvl(max(no),0)+1 from guestbook), ");
		sql.append(" ?, ?, ?, ?, ?, ?, sysdate,  ");
		sql.append(" (select nvl(max(grpno),0)+1 from guestbook)) ");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getGender());
			pstmt.setString(3, dto.getTitle());
			pstmt.setString(4, dto.getContent());
			pstmt.setString(5, dto.getPartstr());
			pstmt.setString(6, dto.getPasswd());
			int cnt = pstmt.executeUpdate();
			
			if(cnt>0)flag=true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	//update
	public boolean update(GuestBookDTO dto) {
		boolean flag = false;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" update guestbook ");
		sql.append(" set title = ?, ");
		sql.append(" 	 part = ?, ");
		sql.append("     content=? ");
		sql.append("where no = ? ");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getPartstr());
			pstmt.setString(3, dto.getContent());
			pstmt.setInt(4, dto.getNo());
			
			int cnt = pstmt.executeUpdate();
			if(cnt>0)flag = true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBClose.close(pstmt, con);
		}
		return flag;
	}
	//delete
	public boolean delete(int no) {
		boolean flag= false;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from guestbook ");
		sql.append(" where no = ? ");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, no);
			int cnt = pstmt.executeUpdate();
			if(cnt>0)flag = true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBClose.close(pstmt, con);
		}
		return flag;
	}
	//passwd check 
	public boolean passwdCheck(Map map) {
		boolean flag = false;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;	
		ResultSet rs = null;
		
		int no = (Integer)map.get("no");
		String passwd = (String)map.get("passwd");
		
		StringBuffer sql = new StringBuffer(); 
		sql.append(" select count(*) from guestbook ");
		sql.append(" where no = ? ");
		sql.append(" and passwd= ? ");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, no);
			pstmt.setString(2, passwd);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int cnt = rs.getInt(1);
				if(cnt>0)flag=true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBClose.close(rs, pstmt, con);
		}
		
		return  flag;
	}
	//readReply 나의 부모의 정보를 가지고 옴
	public GuestBookDTO readReply(int no) {
		GuestBookDTO dto = null;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		StringBuffer sql = new StringBuffer();
		//읽어지는 열은 답변을 달때 부모의 내용을 가지고 올 수 있음
		sql.append(" select no, grpno, indent, ansnum, title   ");
		sql.append(" from guestbook ");
		sql.append(" where no = ? ");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, no);
			rs  = pstmt.executeQuery();
			
			if(rs.next()) {
				dto =new GuestBookDTO();
				dto.setNo(rs.getInt("no"));
				dto.setTitle(rs.getString("title"));				
				dto.setGrpno(rs.getInt("grpno"));
				dto.setIndent(rs.getInt("indent"));
				dto.setAnsnum(rs.getInt("ansnum"));
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBClose.close(rs, pstmt, con);
		}
		
		return dto;
		
	}

	
	//upAnsnum
	public void upAnsnum(Map map) {
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" update guestbook ");
		sql.append(" set ansnum  = ansnum+1 ");
		sql.append(" where grpno = ? ");
		sql.append(" and ansnum > ? ");
		
		int grpno=(Integer)map.get("grpno");
		int ansnum=(Integer)map.get("ansnum");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, grpno);
			pstmt.setInt(2, ansnum);
			
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBClose.close(pstmt, con);
		}
		
	}
	//createReply
	public boolean createReply(GuestBookDTO dto) {
		boolean flag = false;
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into guestbook(no, name, gender, ");
		sql.append(" title, content, part, passwd, regdate, grpno, indent, ansnum ) ");
		sql.append(" values((select nvl(max(no),0)+1 from guestbook), ");
		sql.append(" ?, ?, ?, ?, ?, ?, sysdate, ?, ?, ?) ");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getGender());
			pstmt.setString(3, dto.getTitle());
			pstmt.setString(4, dto.getContent());
			pstmt.setString(5, dto.getPartstr());
			pstmt.setString(6, dto.getPasswd());
			pstmt.setInt(7, dto.getGrpno());
			pstmt.setInt(8, dto.getIndent()+1);
			pstmt.setInt(9, dto.getAnsnum()+1);
			int cnt = pstmt.executeUpdate();
			
			if(cnt>0)flag=true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	public int total(Map map) {
		int total = 0;
		
		Connection con = DBOpen.open();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String col = (String)map.get("col");
		String word = (String)map.get("word");
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select  count(*) ");
		sql.append(" from guestbook ");
		
		if(word.trim().length()>0&& col.equals("title_name")) {
			sql.append(" where title like '%'||?||'%' ");
			sql.append(" or name like '%'||?||'%' ");
		}else if(word.trim().length()>0) {
			sql.append(" where "+col+" like '%'||?||'%' ");
		}
		try {
			pstmt = con.prepareStatement(sql.toString());
			
			if(word.trim().length()>0&& col.equals("title_name")) {
				pstmt.setString(1, word);
				pstmt.setString(2, word);
			}
			else if(word.trim().length()>0) {
				pstmt.setString(1, word);
			}
			rs = pstmt.executeQuery();
			if(rs.next()) {
				total = rs.getInt(1);//?
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBClose.close(rs, pstmt, con);
		}
		return total;
	}
}
