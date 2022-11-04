package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class User {
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public DefaultTableModel getAllRequests(DefaultTableModel model) {
		Statement stmt = null;
        ResultSet rs = null;
        Connection con = JavaSQLite.connectDB();
        int total = 0;
        int reqId;
        int stt;
        String status = "";
        String description = "";
        try {
//        	PreparedStatement pst = con.prepareStatement("select * from items");
//            ResultSet rs = pst.executeQuery();
        	stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT requestId, total, status, description FROM requests");
            while (rs.next()) {
                reqId = rs.getInt("requestId");
                total = rs.getInt("total");
                stt = rs.getInt("status");
                description = rs.getString("description");
                if(stt == 0) {
                	status = "Rejected";
                } else if(stt == 1) {
                	status = "Approved";
                } else {
                	status = "Pending";
                }
                model.addRow(new Object[]{reqId, total, status, description});
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
        	try {
        		if(stmt != null) {
        			stmt.close();	
        		}
        		if(rs != null) {
        			rs.close();
        		}
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return model;
	}
	
	/**
	 * Check user login to the system
	 * @param data
	 * @return
	 */
	public int[] checkLogin(String userName, String pwd) {
		Statement stmt = null;
		ResultSet rs = null;
		int[] result = new int[2];
		int userId = 0;
		int role = 0;
    	Connection con = JavaSQLite.connectDB();
		try {
			stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT userId, role FROM user WHERE userName = " + "'" + userName +"' AND password=" + "'"+ pwd +"'");
            if (rs.next()) {
                userId = rs.getInt("userId");
                role = rs.getInt("role");
                if(userId > 0) {
                	result[0] = userId;
                	result[1] = role;
                	
                	return result;
                }
            }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null) {
					stmt.close();
				}
				if(rs != null) {
					rs.close();
				}
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
}
