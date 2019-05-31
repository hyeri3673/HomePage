package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBOpen {
	public static Connection open() {//리턴타입 = Connection타입 
		Connection con = null;
		try {
			Class.forName(Constant.DRIVER);
			
			
			con = DriverManager.getConnection(Constant.URL, Constant.USER, Constant.PASSWORD);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return con;
	}
}