package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Requests {
	
	
	public Requests() {
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
				stmt.close();
				rs.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return model;
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
