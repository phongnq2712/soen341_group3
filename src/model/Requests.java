package model;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class Requests {
	Boolean flag = true;
	class CbbEditor extends DefaultCellEditor 
	  {
	    private JComboBox<String> aBox = new JComboBox<String>();
	    
	    public CbbEditor(JComboBox<String> checkBox)
	    {
	      super(checkBox);
	    }
	    public Component getTableCellEditorComponent(JTable table, Object value,
	    boolean isSelected, int row, int column) 
	    {
	    	System.out.println(value.toString());
	    	
	    	return aBox;
	    	
	    }
	  }
	
	public Requests() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public DefaultTableModel getAllRequests(DefaultTableModel model, JTable tableRequests, int userId, int userRole) {
		Statement stmt = null;
        ResultSet rs = null;
        Connection con = JavaSQLite.connectDB();
        int total = 0;
        int reqId = 0;
        int stt = 0;
        String status = "";
        String description = "";
        
        JComboBox<String> cbbStatus = new JComboBox<>();
    	cbbStatus.addItem("Rejected");
    	cbbStatus.addItem("Approved");
    	cbbStatus.addItem("Pending");
    	
        try {
        	stmt = con.createStatement();
            String sql = "";
            if(userRole == 3) {
            	sql = "SELECT requestId, total, status, description FROM requests ORDER BY status DESC";
            } else {
            	sql = "SELECT requestId, total, status, description FROM requests WHERE userId = " + userId;
            }
        	rs = stmt.executeQuery(sql);
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
            if(userRole == 3) {
            	
            	TableColumn column = tableRequests.getColumnModel().getColumn(2);
            	column.setCellEditor(new DefaultCellEditor(cbbStatus));
            	model.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    int type = e.getType();
                    	switch (type) {
                        case TableModelEvent.UPDATE:
                            if (e.getFirstRow() - e.getLastRow() == 0) {
                            	DefaultTableModel model = (DefaultTableModel) e.getSource();
                                int row = e.getFirstRow();
                                int col = e.getColumn();
                                int input = 0;
                                if(flag) {
                                	input = JOptionPane.showConfirmDialog(null, "Are you sure to update this record?");
                                }
                                
                                String status = model.getValueAt(row, col).toString();
                                // 0=yes, 1=no, 2=cancel
                                System.out.println("input:" + input + " - value:"+ model.getValueAt(row, col));
                                if(input == 0 && !"".equals(status)) {
                                	// update status of the request chosen
                                	int requestId = Integer.parseInt(model.getValueAt(row, 0).toString());
                                	int stt = 0;
                                	if("Approved".equals(status)) {
                                		stt = 1;
                                	} else if("Pending".equals(status)) {
                                		stt = 2;
                                	}
                                	updateRequest(requestId, stt);
                                	flag = true;
                                } else {
                                	flag = false;
                                	model.setValueAt("Pending", row, col);
                                }
                            }
                            break;
                    	}
                    }
                
                });
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
	 * Save requests to DB
	 * @param data
	 * @return
	 */
	public void saveRequest(int userId, int total, int stt, String des) {
		PreparedStatement stmt = null;
    	Connection con = JavaSQLite.connectDB();
		try {
			
			String sql = "INSERT INTO requests(userId, total, status, description) "
					+ "VALUES(" + userId +"," + total + "," + stt + "," + "'" + des + "')";
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
	
	/**
	 * Update request to DB
	 * @param data
	 * @return
	 */
	public void updateRequest(int requestId, int stt) {
		PreparedStatement stmt = null;
    	Connection con = JavaSQLite.connectDB();
		try {
			String description = "";
			String status = "";
			switch (stt) {
			case 0: {
				description = "This request is closed!";
				status = "The status is changed to closed.";
				break;
			}
			case 1: {
				description = "This request is approved!";
				status = "The status is changed to approved.";
				break;
			}
			default:
				description = "This request is pending for approval";
				status = "The status is not changed.";
				break;
			}
			
			String sql = "UPDATE requests SET status = " + stt + ", description= '"+ description +"'"+ " WHERE requestId = " + requestId;
			stmt = con.prepareStatement(sql);
			stmt.execute();
			JOptionPane.showMessageDialog(null, status);
			System.out.println("Updated the request!");
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


