package model;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class VendorUpdate extends JFrame {

	private static final long serialVersionUID = 1L;

	public static void main(JFrame loginframe, String userName) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame vendorframe = new JFrame("Vendor Update");
					vendorframe.setVisible(true);
					
					vendorframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					vendorframe.setBounds(100, 100, 505, 286);
					JPanel vendorPanel = new JPanel();
					vendorPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

					vendorframe.setContentPane(vendorPanel);
					vendorPanel.setLayout(null);
					
					JScrollPane scrollPane = new JScrollPane();
					scrollPane.setBounds(171, 30, 308, 183);
					vendorPanel.add(scrollPane);
					
					JTable table = new JTable();
					scrollPane.setViewportView(table);
			        DefaultTableModel model =  (DefaultTableModel) table.getModel();
			        
					JButton Button_Update = new JButton("Update Data");
					Button_Update.setBounds(30, 40, 117, 29);
					vendorPanel.add(Button_Update);

					JButton Button_Delete = new JButton("Delete Data");
					Button_Delete.setBounds(30, 113, 117, 29);
					vendorPanel.add(Button_Delete);

					JButton btnReturn = new JButton("Logout");
					btnReturn.setBounds(30, 184, 117, 29);
					vendorPanel.add(btnReturn);
					vendorframe.setVisible(false);
					
					
			        Statement stmt = null;
			        ResultSet rs = null;
			        Connection con = JavaSQLite.connectDB();
			        try {
			        	stmt = con.createStatement();
				            rs = stmt.executeQuery("SELECT COUNT(*) FROM quotations WHERE supplier_name = '" + userName + "'");
				             if (rs.getInt(1) >0) 
				            {
				            vendorframe.setVisible(true);
				            ResultSet rs2 = stmt.executeQuery("SELECT * FROM quotations WHERE supplier_name = '" + userName + "'");
				            ResultSetMetaData rsmd = rs2.getMetaData();
						
				            int cols = rsmd.getColumnCount();
				            String[] colNames = new String[cols-1];
				            for (int i=0; i<cols-1; i++)
				            	colNames[i] = rsmd.getColumnName(i+1);
				            model.setColumnIdentifiers(colNames);
				            String id, name, price, qty;
			            
				            while (rs2.next()) {
				            	id = rs2.getString(1);
				            	name = rs2.getString(2);
				            	price = rs2.getString(3);
				            	qty = rs2.getString(4);
				            	String[] row = { id, name, price, qty};
				            	model.addRow(row);
				            	
				            }
					        stmt.close();
				            con.close();
				            }
			             else 
			             { 
			             JOptionPane.showMessageDialog(null, "No quotation data from this vendor, please try again!", "Error", JOptionPane.ERROR_MESSAGE);
			             vendorframe.setVisible(false);
						 loginframe.setVisible(true);
			             }
				         } 
			        catch (Exception ex) 
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
					
					
					btnReturn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						loginframe.setVisible(true);
						}
					});
					
					
					Button_Update.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
				     if (table.getSelectedRowCount() ==0)
			 	            	JOptionPane.showMessageDialog(null, "You must select a record to update!", "Warning", JOptionPane.OK_OPTION) ;
				 	            else
			          if (table.getSelectedRowCount() > 1)
			 	            	JOptionPane.showMessageDialog(null, "You can only update one record at a time! ", "Warning", JOptionPane.OK_OPTION) ;
				            else
			              if (JOptionPane.showConfirmDialog(null, "Are you sure to update the selected row in database?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				 	       {Statement stmt = null;
					        Connection con = JavaSQLite.connectDB();
					        try {
					        	stmt = con.createStatement();
					        	stmt.executeUpdate("Update  quotations SET  NAME = " + "'"+model.getValueAt(table.getSelectedRow(),1)+"' WHERE ID = "+model.getValueAt(table.getSelectedRow(),0));
					        	stmt.executeUpdate("Update  quotations SET  PRICE = " + "'"+model.getValueAt(table.getSelectedRow(),2)+"' WHERE ID = "+model.getValueAt(table.getSelectedRow(),0));
					        	stmt.executeUpdate("Update  quotations SET  QTY = " + "'"+model.getValueAt(table.getSelectedRow(),3)+"' WHERE ID = "+model.getValueAt(table.getSelectedRow(),0));
					 	        JOptionPane.showMessageDialog(null, "Record UPDATED", "Message", JOptionPane.INFORMATION_MESSAGE);
			 		        } catch (Exception ex) 
					        	{
					            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					        	} 
					        finally {
					        	try {
									stmt.close();
									con.close();
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
					        }		
			               };
			 			}
					});
					
					Button_Delete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
				     if (table.getSelectedRowCount() ==0)   	JOptionPane.showMessageDialog(null, "You must select a record to delete!", "Warning", JOptionPane.OK_OPTION) ;
				        else 
				        	if (table.getSelectedRowCount() > 1)
			 	            	JOptionPane.showMessageDialog(null, "You can only delete one record at a time!", "Warning", JOptionPane.OK_OPTION) ;
						    else
					 	       if (JOptionPane.showConfirmDialog(null, "Are you sure to delete the selected row from database?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				 	            {
							            Statement stmt = null;
								        Connection con = JavaSQLite.connectDB();
								        try {
								        	stmt = con.createStatement();
						 		            stmt.executeUpdate("DELETE FROM quotations WHERE ID = "+model.getValueAt(table.getSelectedRow(),0));
						 		           JOptionPane.showMessageDialog(null, "Record deleted", "Message", JOptionPane.INFORMATION_MESSAGE);
						 		        } catch (Exception ex) 
								        	{
								            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
								        	} 
								        finally {
								        	try {
												stmt.close();
												con.close();
											} catch (SQLException e1) {
												e1.printStackTrace();
											}
								        }		
							model.removeRow(table.getSelectedRow());
						}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
