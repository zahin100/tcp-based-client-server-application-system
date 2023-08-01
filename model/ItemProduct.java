package model;

import java.io.Serializable;


/**
 * This class is the model class for ItemProduct.
 * 
 * @author Siti Hajar Binti Azi Shaufi
 *
 */

public class ItemProduct implements Serializable {
	
	private int itemProductId;
	private String name;
	private String labelName;
	private double price;
	
	public int getItemProductId() {
		
		return itemProductId;
		
	}
	
	public void setItemProductId(int itemProductId) {
		
		this.itemProductId = itemProductId;
		
	}
	
	public String getName() {
		
		return name;
		
	}
	
	public void setName(String name) {
		
		this.name = name;
		
	}
	
	public String getLabelName() {
		
		return labelName;
		
	}
	
	public void setLabelName(String labelName) {
		
		this.labelName = labelName;
		
	}
	
	public double getPrice() {
		
		return price;
		
	}
	
	public void setPrice(double price) {
		
		this.price = price;
		
	}

}