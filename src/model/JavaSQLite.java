package model;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

public class JavaSQLite {
	
	public static Connection connectDB() {
		Connection cnn = null;
		try {
//			if(cnn == null) {
				Class.forName("org.sqlite.JDBC");
				cnn = DriverManager.getConnection("jdbc:sqlite:group3.db");	
//			}
		} catch(Exception ex) {
			JOptionPane.showMessageDialog(null, ex);
		}
		
		return cnn;
	}
	
}
