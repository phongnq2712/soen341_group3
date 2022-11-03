package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Requests {
	
	
	public Requests() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Save requests to DB
	 * @param data
	 * @return
	 */
	public void saveRequest(int total, int stt, String des) {
		PreparedStatement stmt = null;
    	Connection con = JavaSQLite.connectDB();
		try {
			
			String sql = "INSERT INTO requests(userId, total, status, description) "
					+ "VALUES(" + "1," + total + "," + stt + "," + "'" + des + "')";
			stmt = con.prepareStatement(sql);
			stmt.execute();
			System.out.println("Inserted a new request");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null) {
					stmt.close();
				}
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
