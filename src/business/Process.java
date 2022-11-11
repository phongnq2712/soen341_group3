package business;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import model.JavaSQLite;
import model.Quotations;
import model.Requests;
import model.User;
import model.UserAcctCreation;

public class Process extends JFrame {
	private static final long serialVersionUID = 1L;
	JButton button = new JButton();
	DefaultTableModel model2;
	int seqItemRequest = 0;
	User user = new User();
	Requests requests = new Requests();
	Quotations quotations = new Quotations();
	DefaultTableModel modelRq = new DefaultTableModel();
	JTable tableRequests = new JTable();
	JTable tableLowestQuo = new JTable();
	int userId = 0;
	int userRole = 0;
	/**
	 * Build all requests table
	 * @return
	 */
	public JScrollPane buildGridRequest() {
		String[] columnNames = {"Request Id", "Total", "Status", "Description", "Action"};
		modelRq.setColumnIdentifiers(columnNames);
        tableRequests.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableRequests.setFillsViewportHeight(true);
        
        JScrollPane scroll = new JScrollPane(tableRequests);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        requests.getAllRequests(modelRq, tableRequests, userId, userRole);
        tableRequests.setModel(modelRq);
        
        return scroll;
	}
	
	public Process() {
		// TODO Auto-generated constructor stub
		String[] columnNames = {"No.", "Name", "Unit", "Qty"};
		JFrame frame1 = new JFrame("Procurement System");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
//        frame1.setLayout(new SpringLayout());
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        JTable table = new JTable();
        table.setModel(model);
//        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
//        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));
        		        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        JPanel outPanel = new JPanel();
        outPanel.setLayout(new BoxLayout(outPanel, BoxLayout.PAGE_AXIS));
        JPanel logoutPanel = new JPanel();
        JButton btnLogout = new JButton("Logout");
        logoutPanel.add(btnLogout);
        logoutPanel.setVisible(false);
        
        JPanel makeRequestPanel = new JPanel();
        makeRequestPanel.setLayout(new BoxLayout(makeRequestPanel, BoxLayout.PAGE_AXIS));

        String itemName = "";
        int itemQty = 0;
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = JavaSQLite.connectDB();
        try {
//        	PreparedStatement pst = con.prepareStatement("select * from items");
//            ResultSet rs = pst.executeQuery();
        	stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT distinct name FROM quotations");
            int seq = 0;
            while (rs.next()) {
            	seq ++;
                itemName = rs.getString("name");
                model.addRow(new Object[]{seq, itemName, "Kg", itemQty});
            }
            if (seq < 1) {
                JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (seq == 1) {
                System.out.println(seq + " Record Found");
            } else {
                System.out.println(seq + " Records Found");
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
        // Submit button
        JButton btnCalculate = new JButton("Submit");
        
        // table 2
        String[] columnNames2 = {"No.", "Name", "Unit", "Price", "Qty", "Price * Qty"};
        
        tableLowestQuo.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableLowestQuo.setFillsViewportHeight(true);
        
        // result table
        JScrollPane scroll2 = new JScrollPane(tableLowestQuo);
        scroll2.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll2.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
//        model2.addRow(new Object[]{seq, itemName, itemPrice, itemQty});
        
        JPanel topPanel = new JPanel(new SpringLayout());
        JLabel label = new JLabel("The lowest quotation:");
        topPanel.add(label, BorderLayout.BEFORE_LINE_BEGINS);
        
        JLabel lblFirst = new JLabel("Enter quantity of items you need:");
        
        makeRequestPanel.add(lblFirst);
        makeRequestPanel.add(scroll);
        makeRequestPanel.add(btnCalculate);
        JButton btnViewRequests = new JButton("View all requests");
        makeRequestPanel.add(btnViewRequests);
        makeRequestPanel.add(label);
        
        btnCalculate.addActionListener(
	      new ActionListener()
	      {
	        public void actionPerformed(ActionEvent event)
	        {
	        	model2 = new DefaultTableModel();
	        	model2.setColumnIdentifiers(columnNames2);
	        	tableLowestQuo.setModel(model2);
	        	Object[] columnData = new Object[table.getRowCount()];  // One entry for each row
//	            Object[] rowData = new Object [table.getRowCount()];
	            for (int i = 0; i < table.getRowCount(); i++) {  // Loop through the rows
	                // 
	            	if(Integer.parseInt(table.getValueAt(i, 3).toString()) > 0) {
	            		columnData[i] = table.getValueAt(i, 1) + "-" + table.getValueAt(i, 3);
	            	}
	             }
	            Quotations[] lowestQuotationObj = quotations.getTheLowestQuotation(columnData);
	            int seqNo = 0;
	            String supplierName = "";
	            int total = 0;
	            for(Quotations q: lowestQuotationObj) {
	            	if(q != null) {
        				seqNo ++;
        				total += Integer.parseInt(q.getPrice()) * Integer.parseInt(q.getQty());
        				model2.addRow(new Object[] {seqNo, q.getName(), q.getUnit(), q.getPrice(), q.getQty(), 
        						Integer.parseInt(q.getPrice()) * Integer.parseInt(q.getQty())});
        				if("".equals(supplierName)) {
        					supplierName = q.getSupplier();
        				}
	            	}
	            }
	            model2.addRow(new Object[] {"", "", "", "", supplierName + " - Total:", total});
	        	if(total > 0 && total < 5000) {
	        		JOptionPane.showMessageDialog(null, "The lowest quotation from "+ supplierName + ": $" + total + " \nYour request is approved!");
	        		// save to DB - table Requests
	        		requests.saveRequest(userId, total, 1, "This request is approved!");
	        	} else if(total > 5000) {
	        		JOptionPane.showMessageDialog(null, "The lowest quotation from "+ supplierName + ": $" + total + " \nYour request is pending as it is greater than $5000");
	        		requests.saveRequest(userId, total, 2, "This request is pending for approval");
	        	} else {
	        		JOptionPane.showMessageDialog(null, "Please enter the quantity for items");
	        	}
	        }
	      }
	    );       
        
        makeRequestPanel.add(scroll2);
        makeRequestPanel.add(Box.createRigidArea(new Dimension(0,5)));
        outPanel.add(logoutPanel);
        outPanel.add(makeRequestPanel);
        
        // View all requests panel
        JPanel viewRequestsPanel = new JPanel();
        viewRequestsPanel.setLayout(new BoxLayout(viewRequestsPanel, BoxLayout.PAGE_AXIS));
        JLabel lblViewRQ = new JLabel("All requests:");
        viewRequestsPanel.add(lblViewRQ);
        viewRequestsPanel.add(buildGridRequest());
        JButton btnBackMakeRequest = new JButton("Back to make new request");
        viewRequestsPanel.add(btnBackMakeRequest);
        viewRequestsPanel.add(Box.createRigidArea(new Dimension(10,0)));
        viewRequestsPanel.setVisible(false);
        makeRequestPanel.setVisible(false);
       // Login panel
        GridLayout experimentLayout = new GridLayout(0,1);
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setLayout(experimentLayout);
        loginPanel.setMaximumSize(new Dimension(200,150));
        //loginPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        //oginPanel.setLayout(null);
//        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        //JPanel userNamePanel = new JPanel();
        JLabel lblUserName = new JLabel("User Name:");
        loginPanel.add(lblUserName);
        
        //lblUserName.setBounds(36, 44, 112, 16);
        //loginPanel.add(lblUserName);
        
        JTextField txtUserName = new JTextField();
        txtUserName.setColumns(10);
        //txtUserName.setBounds(171, 39, 123, 26);
//        userNamePanel.add(txtUserName);
        
        loginPanel.add(txtUserName);
        JLabel spacePwd = new JLabel("");
        loginPanel.add(spacePwd);
        
//        JPanel pwdPanel = new JPanel();
        JLabel lblPwd = new JLabel("Password:");
        //lblPwd.setBounds(36, 73, 112, 16);
        loginPanel.add(lblPwd);
        JPasswordField txtPwd = new JPasswordField();
        //txtPwd.setBounds(171, 73, 123, 26);
        txtPwd.setColumns(10);
        loginPanel.add(txtPwd);
        
//        JPanel btnLoginPanel = new JPanel();
//        btnLoginPanel.setPreferredSize(new Dimension(300, 50));
        JLabel space = new JLabel("");
        loginPanel.add(space);
       // loginPanel.add(space);
        JButton btnLogin = new JButton("Login");
        //btnLogin.setBounds(128, 118, 117, 29);
//        btnLogin.setLocation(2,2);
//        btnLogin.setSize(1,1);
        loginPanel.add(btnLogin);
        //loginPanel.add(space);
//        loginPanel.add(userNamePanel);
//        loginPanel.add(pwdPanel);
//        loginPanel.add(btnLoginPanel);
        
        outPanel.add(loginPanel);
        outPanel.add(viewRequestsPanel);
        
        frame1.add(outPanel);
        frame1.setVisible(true);
        frame1.setSize(600, 400);

        // account creation 
        JLabel space1 = new JLabel("");
        loginPanel.add(space1);
        JLabel lblNewAccount = new JLabel("Don't have an account?");
        //lblNewAccount.setBounds(48, 158, 197, 29);
        loginPanel.add(lblNewAccount);
	
        JButton btnRegister = new JButton("Create account");
       // btnRegister.setBounds(198, 158, 132, 29);
        loginPanel.add(btnRegister);
        
    	btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		   UserAcctCreation.main(null);
 		 			}; 
 		});
		frame1.setEnabled(true);  	 		
        // account creation 
		
		
        
        btnViewRequests.addActionListener(
	      new ActionListener()
	      {
	        public void actionPerformed(ActionEvent event)
	        {
	        	makeRequestPanel.setVisible(false);
	        	viewRequestsPanel.setVisible(true);
	        	viewRequestsPanel.add(btnBackMakeRequest);
	        	((DefaultTableModel)tableRequests.getModel()).setRowCount(0);
	        	tableRequests.setModel(requests.getAllRequests(modelRq, tableRequests, userId, userRole));
	        }
	      }
	    );
        btnBackMakeRequest.addActionListener(
  	      new ActionListener()
  	      {
  	        public void actionPerformed(ActionEvent event)
  	        {
  	        	makeRequestPanel.setVisible(true);
  	        	viewRequestsPanel.setVisible(false);
  	        }
  	      }
  	    );
        btnLogin.addActionListener(
  	      new ActionListener()
  	      {
  	        public void actionPerformed(ActionEvent event)
  	        {
  	        	int[] userArr = user.checkLogin(txtUserName.getText().trim(), txtPwd.getText().trim());
  	        	if(userArr.length > 0 && userArr[0] > 0 && userArr[1] > 0) {
  	        		loginPanel.setVisible(false);
  	        		logoutPanel.setVisible(true);
  	        		userId = userArr[0];
  	        		userRole = userArr[1];
  	        		if(userArr[1] == 1) {
  	        			// user role
  	        			makeRequestPanel.setVisible(true);
  	        			((DefaultTableModel)tableLowestQuo.getModel()).setRowCount(0);
  	        			tableRequests.setModel(requests.getAllRequests(modelRq, tableRequests, userId, userRole));
  	  	  	        	viewRequestsPanel.setVisible(false);
  	  	  	        	
  	        		} else if(userArr[1] == 2) {
  	        			// vendors role
  	        		} else {
  	        			// admin role
  	        			makeRequestPanel.setVisible(false);
  	  	  	        	viewRequestsPanel.setVisible(true);
  	  	  	        	viewRequestsPanel.remove(btnBackMakeRequest);
  	  	  	        	((DefaultTableModel)tableRequests.getModel()).setRowCount(0);
  	  	  	        	tableRequests.setModel(requests.getAllRequests(modelRq, tableRequests, userId, userRole));
  	        		}
  	        	} else {
  	        		JOptionPane.showMessageDialog(null, "User Name or Password is not correct!\nPlease try again!");
  	        	}
  	        	
  	        }
  	      }
  	    );
        btnLogout.addActionListener(
	      new ActionListener()
	      {
	        public void actionPerformed(ActionEvent event)
	        {
	        	loginPanel.setVisible(true);
	        	viewRequestsPanel.setVisible(false);
	        	makeRequestPanel.setVisible(false);
	        	logoutPanel.setVisible(false);
	        }
	      }
	    );
	}
	
	class ButtonRenderer extends JButton implements TableCellRenderer {
	    public ButtonRenderer() {
	      setOpaque(true);
	    }
	  public Component getTableCellRendererComponent(JTable table, Object value,
	    boolean isSelected, boolean hasFocus, int row, int column) {
	      setText((value == null) ? "Select" : value.toString());
	      return this;
	    }
  }
  class ButtonEditor extends DefaultCellEditor 
  {
    private String label;
    
    public ButtonEditor(JCheckBox checkBox)
    {
      super(checkBox);
    }
    public Component getTableCellEditorComponent(JTable table, Object value,
    boolean isSelected, int row, int column) 
    {
    	seqItemRequest ++;
    	model2.addRow(new Object[]{seqItemRequest, table.getValueAt(row, 1), table.getValueAt(row, 2)});
    	label = (value == null) ? "Select" : value.toString();
    	button.setText(label);
    	button.setEnabled(false);
    	button.addActionListener(
  	      new ActionListener()
  	      {
  	        public void actionPerformed(ActionEvent event)
  	        {
  	        	System.out.println("button clicked!");
  	        	JOptionPane.showMessageDialog(null,"");
  	        }
  	      }
  	    );
    	return button;
    }
    public Object getCellEditorValue() 
    {
    	System.out.println("getCellEditorValue");
    	button.setEnabled(false);
    	return new String(label);
    }
  }
	
	public static void main(String[] args) {
		new Process();
	}

}
