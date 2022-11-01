package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Quotations {

	String name;
	String unit;
	String price;
	String qty;
	String supplier;
		
	public Quotations() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Quotations(String name, String unit, String price, String qty, String supplier) {
		super();
		this.name = name;
		this.unit = unit;
		this.price = price;
		this.qty = qty;
		this.supplier = supplier;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	
	/**
	 * Get the lowest quotations from suppliers
	 * @param data
	 * @return
	 */
	public Quotations[] getTheLowestQuotation(Object[] data) {
		Quotations[] objWM = new Quotations[50];
		Quotations[] objCostCo = new Quotations[50];
		Quotations[] objSuperC = new Quotations[50];
		String[] suppliersName = {"Walmart", "Costco", "SuperC"};
		int sumWalmart = 0;
		int sumCostco = 0;
		int sumSuperC = 0;
		Connection con = JavaSQLite.connectDB();
		Statement stmt;
		for (int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				String[] parts = ((String) data[i]).split("-");
	            try {
	            	stmt = con.createStatement();
	            	for(int j = 0; j < suppliersName.length; j++) {
	            		ResultSet rs = stmt.executeQuery("SELECT price FROM quotations WHERE supplier_name LIKE '%"+suppliersName[j]+"%' "
	            				+ " AND name LIKE'%" + parts[0] + "%'");
						if (rs.next()) {
							String price = rs.getString("price");
							int sumItem = Integer.parseInt(price) * Integer.parseInt(parts[1]);
							if(suppliersName[j].equals("Walmart")) {
								sumWalmart += sumItem;
								objWM[i] = new Quotations(parts[0], "Kg", price, parts[1], suppliersName[j]); //[] {suppliersName[j], price, parts[1]};
							} else if (suppliersName[j].equals("Costco")) {
								sumCostco += sumItem;
								objCostCo[i] = new Quotations(parts[0], "Kg", price, parts[1], suppliersName[j]);
							} else {
								sumSuperC += sumItem;
								objSuperC[i] = new Quotations(parts[0], "Kg", price, parts[1], suppliersName[j]);
							}
						}
	            	}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println(sumWalmart);
		System.out.println(sumCostco);
		System.out.println(sumSuperC);
		System.out.println(Math.min(Math.min(sumWalmart, sumCostco), sumSuperC));
		int theLowestQuotationValue = Math.min(Math.min(sumWalmart, sumCostco), sumSuperC);
		if(theLowestQuotationValue == sumWalmart) {
			return objWM;
		} else if(theLowestQuotationValue == sumCostco) {
			return objCostCo;
		}
		
		return objSuperC;
	}
	
}
