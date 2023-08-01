package client;

/**
 * This class manage invalid cash amount exception when the cashier 
 * enter payment amount less than RM 1.
 * 
 * @author Akmal Zahin bin Zulkepli
 *
 */

public class InvalidCashAmountException extends Exception {

	String message;
	
	public InvalidCashAmountException() {
		
		super();
		
	}
	
	//method to display message if the amount less than RM1
	public String getMessage() {
		
		message = "Invalid payment amount. The payment amount "
				+ "cannot be less than RM 1.";
		
		return message;
		
	}
	
}
