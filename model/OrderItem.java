package model;

import java.io.Serializable;
import java.sql.Date;


/**
 * This class is the model class for OrderItem.
 * 
 * @author Siti Hajar Binti Azi Shaufi
 *
 */

public class OrderItem implements Serializable {

	private int orderItemId;
	private ItemProduct itemproduct;
	private int orderId;
	private int quantity;
	private double subTotalAmount; // Sub total for one type of drink only.
	private int sequenceNumber;
	private String orderStatus;
	private String readyTime;
	
	public int getOrderItemId() {
		
		return orderItemId;
		
	}
	
	public void setOrderItemId(int orderItemId) {
		
		this.orderItemId = orderItemId;
		
	}
	
	public ItemProduct getItemproduct() {
		
		return itemproduct;
		
	}
	
	public void setItemproduct(ItemProduct itemproduct) {
		
		this.itemproduct = itemproduct;
		
	}
	
	public int getOrderId() {
		
		return orderId;
		
	}
	
	public void setOrderId(int orderId) {
		
		this.orderId = orderId;
		
	}
	
	public int getQuantity() {
		
		return quantity;
		
	}
	
	public void setQuantity(int quantity) {
		
		this.quantity = quantity;
		
	}
	
	public double getSubTotalAmount() {
		
		return subTotalAmount;
		
	}
	
	public void setSubTotalAmount(double subTotalAmount) {
		
		this.subTotalAmount = subTotalAmount;
		
	}
	
	public int getSequenceNumber() {
		
		return sequenceNumber;
		
	}
	
	public void setSequenceNumber(int sequenceNumber) {
		
		this.sequenceNumber = sequenceNumber;
		
	}
	
	public String getOrderStatus() {
		
		return orderStatus;
		
	}
	
	public void setOrderStatus(String orderStatus) {
		
		this.orderStatus = orderStatus;
		
	}
	
	public String getReadyTime() {
		
		return readyTime;
		
	}
	
	public void setReadyTime(String readyTime) {
		
		this.readyTime = readyTime;
		
	}
}
