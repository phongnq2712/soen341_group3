package business;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import model.JavaSQLite;
import model.Quotations;

public class Process extends JFrame {
	private static final long serialVersionUID = 1L;
	JButton button = new JButton();
	DefaultTableModel model2;
	int seqItemRequest = 0;
	Business bussiness = new Business();
	
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
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.PAGE_AXIS));

        String itemName = "";
        int itemQty = 0;
        try {
        	Connection con = JavaSQLite.connectDB();
//        	PreparedStatement pst = con.prepareStatement("select * from items");
//            ResultSet rs = pst.executeQuery();
        	Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT distinct name FROM quotations");
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
        }
        // Submit button
        JButton btnCalculate = new JButton("Submit");
        
        // table 2
        String[] columnNames2 = {"No.", "Name", "Unit", "Price", "Qty", "Price * Qty"};
        
        JTable table2 = new JTable();
        
        table2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table2.setFillsViewportHeight(true);
        
        // result table
        JScrollPane scroll2 = new JScrollPane(table2);
        scroll2.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll2.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
//        model2.addRow(new Object[]{seq, itemName, itemPrice, itemQty});
        
        JPanel topPanel = new JPanel(new SpringLayout());
        JLabel label = new JLabel("The lowest quotation:");
        topPanel.add(label, BorderLayout.BEFORE_LINE_BEGINS);
        
        JLabel lblFirst = new JLabel("Enter quantity of items you need:");
        
        outerPanel.add(lblFirst);
        outerPanel.add(scroll);
        outerPanel.add(btnCalculate);
        outerPanel.add(label);
        
        btnCalculate.addActionListener(
	      new ActionListener()
	      {
	        public void actionPerformed(ActionEvent event)
	        {
	        	model2 = new DefaultTableModel();
	        	model2.setColumnIdentifiers(columnNames2);
	        	table2.setModel(model2);
	        	Object[] columnData = new Object[table.getRowCount()];  // One entry for each row
//	            Object[] rowData = new Object [table.getRowCount()];
	            for (int i = 0; i < table.getRowCount(); i++) {  // Loop through the rows
	                // 
	            	if(Integer.parseInt(table.getValueAt(i, 3).toString()) > 0) {
	            		columnData[i] = table.getValueAt(i, 1) + "-" + table.getValueAt(i, 3);
	            	}
	             }
	            Quotations[] lowestQuotationObj = bussiness.getTheLowestQuotation(columnData);
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
	        	} else if(total > 5000) {
	        		JOptionPane.showMessageDialog(null, "The lowest quotation from "+ supplierName + ": $" + total + " \nYour request is pending as it is greater than $5000");
	        	} else {
	        		JOptionPane.showMessageDialog(null, "Please enter the quantity for items");
	        	}
	        }
	      }
	    );       
        
        outerPanel.add(scroll2);
        // Total label
//        JLabel totalLabel = new JLabel("Total:");
//        outerPanel.add(totalLabel);
        outerPanel.add(Box.createRigidArea(new Dimension(0,5)));
//        frame1.add(scroll);
        frame1.add(outerPanel);
        frame1.setVisible(true);
        frame1.setSize(400, 300);
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
