package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import client.InvalidCashAmountException;


/**
 * This class is the model class for Order.
 * 
 * @author Siti Hajar Binti Azi Shaufi
 *
 */

public class Order implements Serializable {
	
	private int orderId;
	private int orderNumber;
	private String transactionDate;
	private List<OrderItem> orderItems;
	private int totalOrderItem; // Total quantity in one order.
	private double subTotal; // Sub total for one order.
	private double serviceTax;
	private double rounding;
	private double grandTotal;
	private double tenderedCash;
	private double change;
	
	public int getOrderId() {
		
		return orderId;
		
	}
	
	public void setOrderId(int orderId) {
		
		this.orderId = orderId;
		
	}
	
	public int getOrderNumber() {
		
		return orderNumber;
		
	}
	
	public void setOrderNumber(int orderNumber) {
		
		this.orderNumber = orderNumber;
		
	}
	
	public String getTransactionDate() {
		
		return transactionDate;
		
	}
	
	public void setTransactionDate(String transactionDate) {
		
		this.transactionDate = transactionDate;
		
	}
	
	public List<OrderItem> getOrderItems() {
		
		return orderItems;
		
	}
	
	public void setOrderItems(List<OrderItem> orderItems) {
		
		this.orderItems = orderItems;
		
	}
	
	public int getTotalOrderItem() {
		
		return totalOrderItem;
		
	}
	
	public void setTotalOrderItem(int totalOrderItem) {
		
		this.totalOrderItem = totalOrderItem;
		
	}
	
	public double getSubTotal() {
		
		return subTotal;
		
	}
	
	public void setSubTotal(double subTotal) {
		
		this.subTotal = subTotal;
		
	}
	
	public double getServiceTax() {
		
		return serviceTax;
		
	}
	
	public void setServiceTax(double serviceTax) {
		
		this.serviceTax = serviceTax;
		
	}
	
	public double getRounding() {
		
		return rounding;
		
	}
	
	public void setRounding(double rounding) {
		
		this.rounding = rounding;
		
	}
	
	public double getGrandTotal() {
		
		return grandTotal;
		
	}
	
	public void setGrandTotal(double grandTotal) {
		
		this.grandTotal = grandTotal;
		
	}
	
	public double getTenderedCash() {
		
		return tenderedCash;
		
	}
	
	public void setTenderedCash(double tenderedCash) throws 
					InvalidCashAmountException {
		
		if (tenderedCash < 1) {
			
			throw new InvalidCashAmountException();
			
		}
		
		else {
			
			this.tenderedCash = tenderedCash;
			
		}

	}
	
	public double getChange() {
		
		return change;
		
	}
	
	public void setChange(double change) {
		
		this.change = change;
		
	}
}