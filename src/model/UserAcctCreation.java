package model;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.awt.event.ActionEvent;


public class UserAcctCreation extends JFrame {
	
	
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void run() {
				try {
					//UserAcctCreation frame = new UserAcctCreation();
					//frame.setVisible(true);
					/**
					 * Create the frame.
					 */
					
					JFrame acctframe = new JFrame("Account Creation");
			        acctframe.setDefaultCloseOperation(HIDE_ON_CLOSE);
			       
					//acctframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					acctframe.setBounds(100, 100, 450, 300);
					acctframe.setVisible(true);
									
					JPanel AcctCreationPane = new JPanel();
					AcctCreationPane.setBorder(new EmptyBorder(5, 5, 5, 5));
					AcctCreationPane.setLayout(null);
					acctframe.setContentPane(AcctCreationPane);
					
				
					JLabel lblNewLabel = new JLabel("Please input your information as below");
					lblNewLabel.setBounds(53, 10, 261, 22);
					AcctCreationPane.add(lblNewLabel);
					
					JLabel lblNewLabel_1 = new JLabel("User Name:");
					lblNewLabel_1.setBounds(36, 44, 112, 16);
					AcctCreationPane.add(lblNewLabel_1);
					
					JLabel lblNewLabel_2 = new JLabel("Password:");
					lblNewLabel_2.setBounds(36, 73, 112, 16);
					AcctCreationPane.add(lblNewLabel_2);
					
					JLabel lblNewLabel_3 = new JLabel("(Input twice)");
					lblNewLabel_3.setBounds(36, 92, 99, 16);
					AcctCreationPane.add(lblNewLabel_3);
					
					JTextField UserName  = new JTextField();
					UserName.setBounds(191, 39, 123, 26);
					AcctCreationPane.add(UserName);
					UserName.setColumns(10);
					
					JLabel lblNewLabel_4 = new JLabel("Your Role:");
					lblNewLabel_4.setBounds(36, 154, 92, 16);
					AcctCreationPane.add(lblNewLabel_4);
					
					
					JComboBox comboBoxRole = new JComboBox();
					comboBoxRole.setModel(new DefaultComboBoxModel(new String[] {"User", "Supervisor", "Provider"}));
					comboBoxRole.setBounds(191, 150, 123, 27);
					AcctCreationPane.add(comboBoxRole);

					JPasswordField passwordField = new JPasswordField();
					passwordField.setBounds(191, 73, 123, 26);
					AcctCreationPane.add(passwordField);
					
					JPasswordField passwordField2 = new JPasswordField();
					passwordField2.setBounds(191, 106, 123, 26);
					AcctCreationPane.add(passwordField2);
					
					
					JButton btnNewButton = new JButton("Create");
					btnNewButton.setBounds(68, 218, 117, 29);
					AcctCreationPane.add(btnNewButton);
					
					JButton btnNewButton_1 = new JButton("Cancel");
		
					btnNewButton_1.setBounds(204, 218, 117, 29);
					AcctCreationPane.add(btnNewButton_1);
					
					
					btnNewButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
				
		
				 
					 boolean toInsert = false;
					 int newuserId = 0;
					 
					 if (UserName.getText().equals(""))
			             JOptionPane.showMessageDialog(null, "User Name cannot be empty, please try again!", "Error", JOptionPane.ERROR_MESSAGE);
					 else
					 if ((passwordField.getPassword().length==0) || (passwordField2.getPassword().length==0))
			              JOptionPane.showMessageDialog(null, "Your passwords cannot be empty, please try again!", "Error", JOptionPane.ERROR_MESSAGE);
			      	 else      		    
			          if  (Arrays.equals(passwordField.getPassword(), passwordField2.getPassword()) == false)
			             JOptionPane.showMessageDialog(null, "Your passwords don't match, please try again!", "Error", JOptionPane.ERROR_MESSAGE);
			          else
			          {
				            Statement stmt = null;
					        ResultSet rs = null;
					        Connection con = JavaSQLite.connectDB();
					        try {
					        	stmt = con.createStatement();
			 		            rs = stmt.executeQuery("SELECT COUNT(*) FROM user WHERE userName = " + "'" + UserName.getText() +"'");
			 		             if (rs.getInt(1) >0) 
					                JOptionPane.showMessageDialog(null, "This user name exists, please try again!", "Error", JOptionPane.ERROR_MESSAGE);
			 		             else 
			 		                {ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) FROM user");
			 		                newuserId = rs2.getInt(1)+1;
			 		                toInsert = true;
			 		                }
					        
					        } catch (Exception ex) 
					        	{
					            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					        	} 
					        finally {
					        	try {
									stmt.close();
									rs.close();
									con.close();
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
					        }
					    if (toInsert) {
			 			insertUserAcct(newuserId, UserName.getText(),String.valueOf(passwordField.getPassword()),comboBoxRole.getSelectedIndex()+1);
			 		       JOptionPane.showMessageDialog(null, "User added!", "Message", JOptionPane.INFORMATION_MESSAGE);
							acctframe.setVisible(false);
						 		     }        
			          }	
						}
					});
		
					btnNewButton_1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
						acctframe.setVisible(false);
												
						}
					});
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public static  void insertUserAcct(int userId, String userName, String password, int role) {
		PreparedStatement stmt = null;
    	Connection con = JavaSQLite.connectDB();
		try {
			String sql = "INSERT INTO user (userId, userName, password, role)"
						+ "VALUES(" + userId +",\"" + userName + "\",\"" + password + "\"," + role + ")";
 			stmt = con.prepareStatement(sql);
			stmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	
	
}
