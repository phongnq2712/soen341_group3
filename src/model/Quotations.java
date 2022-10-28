package model;

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
	
}
