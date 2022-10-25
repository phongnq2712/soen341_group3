package business;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.JavaSQLite;

public class Process {
	
	public static void main(String[] args) {
		String[] columnNames = {"No.", "Name", "Price", "Qty"};
		System.out.println("abc");
		JFrame frame1 = new JFrame("List of Items");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLayout(new BorderLayout());
        //TableModel tm = new TableModel();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        //DefaultTableModel model = new DefaultTableModel(tm.getData1(), tm.getColumnNames());
        //table = new JTable(model);
        JTable table = new JTable();
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        String itemName = "";
        String itemPrice = "";
        String itemQty = "";
        try {
        	Connection con = JavaSQLite.connectDB();
//        	PreparedStatement pst = con.prepareStatement("select * from items");
//            ResultSet rs = pst.executeQuery();
        	Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM items");
            int seq = 0;
            while (rs.next()) {
            	seq ++;
                itemName = rs.getString("name");
                itemPrice = rs.getString("price");
                itemQty = rs.getString("qty");
                model.addRow(new Object[]{seq, itemName, itemPrice, itemQty});
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
        frame1.add(scroll);
        frame1.setVisible(true);
        frame1.setSize(400, 300);
	}

}
