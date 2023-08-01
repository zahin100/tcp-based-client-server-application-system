package server;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import database.HTDatabase;
import model.Order;

/**
 * This class is the controller that manage orders.
 * 
 * @author Akmal Zahin bin Zulkepli
 *
 */


public class OrderManager {

	// Method to calculate total.
	public Order calculateTotal(Order order) throws ClassNotFoundException, 
		SQLException {
		
		// Declare variable for totalQuantity, subTotal, serviceTax, rounding 
		// and total.
		int totalQuantity = 0;
		double subTotal = 0;
		double serviceTax = 0;
		double rounding = 0;
		double total = 0;
		
		// Get the price for each product.
		for (int i = 0; i < order.getOrderItems().size(); i++) {
			
			// Get the product id.
			int productId = 
			order.getOrderItems().get(i).getItemproduct().getItemProductId();
			
			// Get the quantity of each product.
			int quantity = order.getOrderItems().get(i).getQuantity();
			
			// Set the sequence number.
			order.getOrderItems().get(i).setSequenceNumber(quantity);
			
			// Calculate the total quantity.
			totalQuantity += quantity;
			
			// Get the product price from the database.
			String getProductPrice = "select price from itemproduct where "
					+ "itemproductid = ?";
			
			Connection conn = (Connection) HTDatabase.doConnection();
			PreparedStatement preparedStatement = 
					(PreparedStatement) conn.prepareStatement(getProductPrice);
			preparedStatement.setInt(1, productId);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
				order.getOrderItems().get(i).getItemproduct().setPrice(
						resultSet.getDouble(1));

			subTotal += (resultSet.getDouble(1) * quantity);
			
			// Set the sub total amount for one type of drink.
			double oneDrinkSubTotal = quantity * (resultSet.getDouble(1));
			order.getOrderItems().get(i).setSubTotalAmount(oneDrinkSubTotal);
			
			conn.close();
		}
		
		order.setTotalOrderItem(totalQuantity);
		
		// Set the subTotal.
		DecimalFormat dfSubTotal = new DecimalFormat("####.##");
		String SUBTOTAL = dfSubTotal.format(subTotal);
		subTotal = Double.parseDouble(SUBTOTAL);
		order.setSubTotal(subTotal);
		
		// Calculate the service tax.
		DecimalFormat df1 = new DecimalFormat("####.##");
		String tax = df1.format((0.06 * subTotal));
		serviceTax = Double.parseDouble(tax);
		order.setServiceTax(serviceTax);
		
		// Calculate the rounding.
		DecimalFormat dfRounding = new DecimalFormat("####.00");
		String number = String.valueOf((dfRounding.format((subTotal 
				+ serviceTax))));
		number = number.substring(number.length() - 1);
		
		
		if (number.equals("0")) {
			rounding = 0.00;
		}
		
		else if (number.equals("1")) {
			rounding = -0.01;
		}
			
		else if (number.equals("2")) {
			rounding = -0.02;
		}
			
		else if (number.equals("3")) {
			rounding = 0.02;
		}
		
		else if (number.equals("4")) {
			rounding = 0.01;
		}
		
		else if (number.equals("5")) {
			rounding = 0.00;
		}
		
		else if (number.equals("6")) {
			rounding = -0.01;
		}
		
		else if (number.equals("7")) {
			rounding = -0.02;
		}
		
		else if (number.equals("8")) {
			rounding = 0.02;
		}
		
		else if (number.equals("9")) {
			rounding = 0.01;
		}

		order.setRounding(rounding);
		
		// Calculate the grand total.
		DecimalFormat df2 = new DecimalFormat("####.00");
		String TOTAL = df2.format((subTotal + serviceTax + rounding));
		total = Double.parseDouble(TOTAL);
		order.setGrandTotal(total);
		
		return order;
	}
	
	
	// Method to calculate balance.
	public Order calculateBalance(Order order) throws ClassNotFoundException, 
				SQLException {
		
		// Declare cashReceived, totalPrice and balance.
		double cashReceived = order.getTenderedCash();
		double totalPrice = order.getGrandTotal();
		
		DecimalFormat dfBalance = new DecimalFormat("##0.00");
		double balance = cashReceived - totalPrice;
		String BALANCE = dfBalance.format(balance);
		balance = Double.parseDouble(BALANCE);
		order.setChange(balance);
		
		// Generate order number.
		order = generateOrderNumber(order);
		
		// Set the transaction date.
		String getCurrentDateTime = "select now()";
		
		Connection conn = (Connection) HTDatabase.doConnection();
		PreparedStatement preparedStatement = 
				(PreparedStatement) conn.prepareStatement(getCurrentDateTime);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next())
			order.setTransactionDate(resultSet.getString(1));
		
		conn.close();
		
		
		return order;
	}
	
	
	// Method to generate order number.
	public Order generateOrderNumber(Order order) throws ClassNotFoundException,
				SQLException {
		
		// Declared orderNumber variable.
		int orderNumber = 0;
		
		// Get the latest order number from the database.
		String getOrderNumber = "select ordernumber from ht_db.order order by"
				+ " ordernumber desc limit 1";
		
		Connection conn = (Connection) HTDatabase.doConnection();
		PreparedStatement preparedStatement = 
				(PreparedStatement) conn.prepareStatement(getOrderNumber);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		
		// If resultSet is empty, set orderNumber = 1000.
		if (resultSet.next() == false) {
			
			orderNumber = 1000;
			order.setOrderNumber(orderNumber);
		}
		
		// Set the new orderNumber by incrementing the previous orderNumber
		// by 1.
		else {
			
			orderNumber = resultSet.getInt(1);
			order.setOrderNumber(orderNumber + 1);
		}
		
		conn.close();
		
		return order;
	}
	
	
	// Method to insert data into the Order table.
	public Order insertDataIntoOrder(Order order) throws ClassNotFoundException,
				SQLException {
		
		// Insert data into the Order table.
		String insertData = "insert into ht_db.order (ordernumber, "
				+ "transactiondate, grandtotal, tenderedcash, "
				+ "ht_db.order.Change,totalorderitem, subtotal, rounding, "
				+ "servicetax) values (?,?,?,?,?,?,?,?,?);";
		
		Connection conn = (Connection) HTDatabase.doConnection();
		PreparedStatement preparedStatement = (PreparedStatement) 
				conn.prepareStatement(insertData);
		
		preparedStatement.setInt(1, order.getOrderNumber());
		preparedStatement.setString(2, order.getTransactionDate());
		preparedStatement.setDouble(3, order.getGrandTotal());
		preparedStatement.setDouble(4, order.getTenderedCash());
		preparedStatement.setDouble(5, order.getChange());		
		preparedStatement.setInt(6, order.getTotalOrderItem());
		preparedStatement.setDouble(7, order.getSubTotal());
		preparedStatement.setDouble(8, order.getRounding());		
		preparedStatement.setDouble(9, order.getServiceTax());
		preparedStatement.executeUpdate();
		
		// Set the order id.
		String getOrderId = "select orderid from ht_db.order order by "
				+ "orderid desc limit 1";
		
		PreparedStatement preparedStatementOrderId = 
				(PreparedStatement) conn.prepareStatement(getOrderId);
		
		ResultSet resultSet = preparedStatementOrderId.executeQuery();
		
		if (resultSet.next())
			order.setOrderId(resultSet.getInt(1));
		
		conn.close();
		
		return order;
	}
	
	
	
	// Method to insert data into the OrderItem table.
	public Order insertDataIntoOrderItem(Order order) throws 
			ClassNotFoundException, SQLException {
		
		
		// Insert data into the OrderItem table.
		for (int i = 0; i < order.getOrderItems().size(); i++) {
			
			String insertData = "insert into orderitem (itemproduct, "
					+ "ht_db.orderitem.order, " + "quantity, subtotalamount, "
				    + "orderstatus) " + "values (?,?,?,?,?)";
			
			// Get the product id.
			int productId = order.getOrderItems().get(i).getItemproduct(
					).getItemProductId();
			
			// Get the order id.
			int orderId = order.getOrderId();
			
			// Get the quantity.
			int quantity = order.getOrderItems().get(i).getQuantity();
			
			// Get the sub total amount.
			double subTotalAmount = 
					order.getOrderItems().get(i).getSubTotalAmount();
			
			// Get the order status.
			String orderStatus = order.getOrderItems().get(i).getOrderStatus();
			
			Connection conn = (Connection) HTDatabase.doConnection();
			PreparedStatement preparedStatement = (PreparedStatement) 
					conn.prepareStatement(insertData);
			
			preparedStatement.setInt(1, productId);
			preparedStatement.setInt(2, orderId);
			preparedStatement.setInt(3, quantity);		
			preparedStatement.setDouble(4, subTotalAmount);
			preparedStatement.setString(5, orderStatus);
			preparedStatement.executeUpdate();
			
			
			// Set the order item id.
			String getOrderItemId = "select orderitem from ht_db.orderitem "
					+ "where ht_db.orderitem.order = " + order.getOrderId();
			
			PreparedStatement preparedStatementOrderItemId = 
					(PreparedStatement) conn.prepareStatement(getOrderItemId);
			
			ResultSet resultSet = preparedStatementOrderItemId.executeQuery();
			
			int counter = 0;
			while (resultSet.next()) {
				
				order.getOrderItems().get(counter).setOrderItemId(
						resultSet.getInt(1));
				counter++;
			}
				
			conn.close();
		}
		
		// Set data in the ItemProduct model class.
		order = setItemProductData(order);
		
		return order;
		
	}
	
	
	// Method to set data in the ItemProduct model class.
	public Order setItemProductData (Order order) throws ClassNotFoundException,
				SQLException {
		
		// Get product data from the database.
		for (int i = 0; i < order.getOrderItems().size(); i++) {
			
			int productId = order.getOrderItems().get(i).getItemproduct(
					).getItemProductId();
			
			String getData = "select name, labelname from itemproduct where "
					+ "itemproductid = ?";
			
			Connection conn = (Connection) HTDatabase.doConnection();
			PreparedStatement preparedStatement = 
					(PreparedStatement) conn.prepareStatement(getData);
			preparedStatement.setInt(1, productId);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				
				order.getOrderItems().get(i).getItemproduct().setName(
						resultSet.getString(1));
				order.getOrderItems().get(i).getItemproduct().setLabelName(
						resultSet.getString(2));
			}
				
			
			conn.close();
			
		}
		
		return order;
		
	}
	
	
	// Method to write receipt to a file.
	public void generateReceipt (Order order) throws IOException, 
				ClassNotFoundException, SQLException {
		
		// Get all of the required information from Order class model.
		int orderNumber = order.getOrderNumber();
		int billNo = 81232879 + orderNumber;
		
		// Format the date.
		String transactionDate = order.getTransactionDate();
		String formatDate = "select date_format('" + transactionDate + 
				"', '%d/%m/%Y %T')";
		Connection conn = (Connection) HTDatabase.doConnection();
		PreparedStatement preparedStatement = 
				(PreparedStatement) conn.prepareStatement(formatDate);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		
		if (resultSet.next()) {
			transactionDate = resultSet.getString(1);
		}
		
		conn.close();
		
		int totalQuantity = order.getTotalOrderItem();
		
		// Display value in 2 decimal places.
		double SUBTOTAL = order.getSubTotal();
		DecimalFormat df1 = new DecimalFormat("###.00");
		String subTotal = df1.format(SUBTOTAL);

		double SERVICETAX = order.getServiceTax();
		DecimalFormat df2 = new DecimalFormat("##0.00");
		String serviceTax = df2.format(SERVICETAX);
		
		double ROUNDING = order.getRounding();
		DecimalFormat df3 = new DecimalFormat("##0.00");
		String rounding = df3.format(ROUNDING);
		
		double GRANDTOTAL = order.getGrandTotal();
		DecimalFormat df4 = new DecimalFormat("##0.00");
		String grandTotal = df4.format(GRANDTOTAL);
		
		double TENDEREDCASH = order.getTenderedCash();
		DecimalFormat df5 = new DecimalFormat("##0.00");
		String tenderedCash = df5.format(TENDEREDCASH);
		
		double BALANCE = order.getChange();
		DecimalFormat df6 = new DecimalFormat("##0.00");
		String balance = df6.format(BALANCE);
		
		
		// Get all of the required information from OrderItem class model.
		String labelName[] = new String[10];
		int quantity[] = new int[10];
		double SUBTOTALAMOUNT[] = new double[10];
		DecimalFormat df7 = new DecimalFormat("##0.00");
		String subTotalAmount[] = new String[10];
		
		for (int i = 0; i < order.getOrderItems().size(); i++) {
			
			labelName[i] = order.getOrderItems().get(i).getItemproduct(
					).getLabelName();
			
			quantity[i] = order.getOrderItems().get(i).getQuantity();
			
			SUBTOTALAMOUNT[i] = order.getOrderItems().get(i
					).getSubTotalAmount();
			subTotalAmount[i] = df7.format(SUBTOTALAMOUNT[i]);
			
		}
		
		
		// Open the FileWriter and BufferedWriter.
		String file = "Receipt" + orderNumber; 
		FileWriter fileWriter = new FileWriter(file + ".txt");
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		// Write the receipt to the file.
		bufferedWriter.newLine();
		bufferedWriter.write("    -----------------------------------------");
		bufferedWriter.newLine();
		bufferedWriter.write("    Your order number is: " + orderNumber);
		bufferedWriter.newLine();
		bufferedWriter.write("    -----------------------------------------");
		bufferedWriter.newLine();
		bufferedWriter.write("    HornettTea");
		bufferedWriter.newLine();
		bufferedWriter.write("    FICTS");
		bufferedWriter.newLine();
		bufferedWriter.write("    Fakulti Teknologi Maklumat dan Komunikasi");
		bufferedWriter.newLine();
		bufferedWriter.write("    Universiti Teknikal Malaysia Melaka");
		bufferedWriter.newLine();
		bufferedWriter.write("    Hang Tuah Jaya, 76100 Durian Tunggal ");
		bufferedWriter.newLine();
		bufferedWriter.write("    Melaka, Malaysia");
		bufferedWriter.newLine();
		bufferedWriter.write("    -----------------------------------------");
		bufferedWriter.newLine();
		bufferedWriter.write("    Invoice");
		bufferedWriter.newLine();
		bufferedWriter.newLine();
		bufferedWriter.write("    Bill No: " + billNo);
		bufferedWriter.newLine();
		bufferedWriter.write("    Date: " + transactionDate);
		bufferedWriter.newLine();
		bufferedWriter.newLine();
		bufferedWriter.write("    Details");
		bufferedWriter.newLine();
		bufferedWriter.write("    -----------------------------------------");
		bufferedWriter.newLine();
		bufferedWriter.write("    Item Name \t\t\t Qty \tPrice(RM)");
		bufferedWriter.newLine();
		bufferedWriter.write("    -----------------------------------------");
		bufferedWriter.newLine();
		for (int i = 0; i < order.getOrderItems().size(); i++) {
			
			if (labelName[i].length() == 20) {
				
				bufferedWriter.write("    " + labelName[i]);
				bufferedWriter.write("\t  " + quantity[i]);
				bufferedWriter.write("       " + subTotalAmount[i]);
				bufferedWriter.newLine();
				
			}
			
			else if (labelName[i].length() == 13) {
				
				bufferedWriter.write("    " + labelName[i]);
				bufferedWriter.write("\t  \t        " + quantity[i]);
				bufferedWriter.write("       " + subTotalAmount[i]);
				bufferedWriter.newLine();
			
			}
			
			else {
				
				bufferedWriter.write("    " + labelName[i]);
				bufferedWriter.write("\t  \t  " + quantity[i]);
				bufferedWriter.write("       " + subTotalAmount[i]);
				bufferedWriter.newLine();
				
			}
			
		}
		
		
		bufferedWriter.write("    -----------------------------------------");
		bufferedWriter.newLine();
		bufferedWriter.write("    Total item \t \t\t  " + totalQuantity);
		bufferedWriter.newLine();
		bufferedWriter.newLine();
		bufferedWriter.write("\t\t\t Sub Total \t \t    "
				+ subTotal);
		bufferedWriter.newLine();
		bufferedWriter.write("\t\tService Tax (6%)\t\t     "
				+ serviceTax);
		bufferedWriter.newLine();
		bufferedWriter.write("\t\t\t Rounding  \t \t     "
				+ rounding);
		bufferedWriter.newLine();
		bufferedWriter.write("    -----------------------------------------");
		bufferedWriter.newLine();
		bufferedWriter.write("\t\t\t Grand Total   \t    "
				+ grandTotal);
		bufferedWriter.newLine();
		bufferedWriter.write("\t\t\t Tendered Cash        "
				+ tenderedCash);
		bufferedWriter.newLine();
		bufferedWriter.write("\t\t\t Change        \t     "
				+ balance);
		bufferedWriter.newLine();
		bufferedWriter.write("    -----------------------------------------");
		bufferedWriter.newLine();
		bufferedWriter.write("\t Thank you and have a good day");
		
		bufferedWriter.close();
	}
	
	
	// Method to write order details to a file.
	public void displayOrderDetails (Order order2) throws IOException {
		
		// Get order number.
		int orderNumber = order2.getOrderNumber();
		
		// Get the products' name, quantity and totalQuantity.
		String productName[] = new String[10];
		int quantity[] = new int[10];
		
		for (int i = 0; i < order2.getOrderItems().size(); i++) {
			
			productName[i] = order2.getOrderItems().get(i).getItemproduct(
					).getName().trim();			
			
			quantity[i] = order2.getOrderItems().get(i).getQuantity();
			
		}
		
		// Get the order's total quantity.
		int totalQuantity = order2.getTotalOrderItem();
		
		
		// Open the FileWriter and BufferedWriter.
		String file = "OrderDetails" + orderNumber; 
		FileWriter fileWriter = new FileWriter(file + ".txt");
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		// Write the order details to a file.
		bufferedWriter.newLine();
		bufferedWriter.write("   Order Number: " + orderNumber);
		bufferedWriter.newLine();
		bufferedWriter.newLine();
		bufferedWriter.write("   -------------------------------------"
				+ "--------------------");
		bufferedWriter.newLine();
		bufferedWriter.write("   Item Name (Quantity) ");
		bufferedWriter.newLine();
		bufferedWriter.write("   -------------------------------------"
				+ "--------------------");
		bufferedWriter.newLine();
		
		for (int i = 0; i < order2.getOrderItems().size(); i++) {
			
			bufferedWriter.write("   " + productName[i] 
					+ " (" + quantity[i] + ")");
			bufferedWriter.newLine();
			
		}
		
		bufferedWriter.write("   ------------------------------------"
				+ "---------------------");
		bufferedWriter.newLine();
		bufferedWriter.write("   Total Item: " + totalQuantity);
		
		
		bufferedWriter.close();
		
	}
	
	// Method to update order status from "Preparing" to "Ready".
	public void updateOrderStatus (Order order) throws ClassNotFoundException, 
				SQLException {
		
		// Update the order status.
		for (int i = 0; i < order.getOrderItems().size(); i++) {
			
			String updateStatus = "update ht_db.orderitem set OrderStatus = ? "
					+ "where ht_db.orderitem.orderitem = ?";
			
			Connection conn = (Connection) HTDatabase.doConnection();
			PreparedStatement preparedStatement = (PreparedStatement) 
					conn.prepareStatement(updateStatus);
			
			preparedStatement.setString(1, order.getOrderItems(
					).get(i).getOrderStatus());
			preparedStatement.setInt(2, order.getOrderItems(
					).get(i).getOrderItemId());
			preparedStatement.executeUpdate();
			
			conn.close();
			
		}
		
	}
	
	// Method to write stickers to a file.
	public void generateStickers (Order order) throws ClassNotFoundException,
				SQLException, IOException {
		
		// Get the order number.
		int orderNumber = order.getOrderNumber();
		
		// Get the products' name and sequence number.
		String productName[] = new String[10];
		int sequenceNumber[] = new int[10];
		
		for (int i = 0; i < order.getOrderItems().size(); i++) {
			
			productName[i] = order.getOrderItems().get(i).getItemproduct(
					).getName();
			
			sequenceNumber[i] = order.getOrderItems().get(i
					).getSequenceNumber();
		}
		
		
		// Set the ready date and time.
		String getReadyDateTime = "select now()";
		Connection conn = (Connection) HTDatabase.doConnection();
		PreparedStatement preparedStatement = 
				(PreparedStatement) conn.prepareStatement(getReadyDateTime);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		
		if (resultSet.next()) {
			
			for (int i = 0; i < order.getOrderItems().size(); i++) {
					
					order.getOrderItems().get(i).setReadyTime(
							resultSet.getString(1));
					
			}
			
		}
		
		// Update the ready date and time in the database.
		String updateReadyDateTime = "update ht_db.orderitem set readyTime = ? "
				+ "where ht_db.orderitem.order = ?";
		
		PreparedStatement preparedStatement2 = (PreparedStatement) 
				conn.prepareStatement(updateReadyDateTime);
		
		preparedStatement2.setString(1, order.getOrderItems(
				).get(0).getReadyTime());
		preparedStatement2.setInt(2, order.getOrderId());
		preparedStatement2.executeUpdate();
		
		// Format the date.
		String readyDateTime = order.getOrderItems().get(0).getReadyTime();
		String formatDate = "select date_format('" + readyDateTime 
				+ "', '%d/%m/%Y %T')";
		
		PreparedStatement preparedStatement3 = 
				(PreparedStatement) conn.prepareStatement(formatDate);
		
		ResultSet resultSet2 = preparedStatement3.executeQuery();
		
		if (resultSet2.next()) {
			readyDateTime = resultSet2.getString(1);
		}
		
		conn.close();
		
		
		// Open the FileWriter and BufferedWriter.
		String file = "Stickers" + orderNumber; 
		FileWriter fileWriter = new FileWriter(file + ".txt");
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		
		// Write stickers to the file.
		for (int i = 0; i < order.getOrderItems().size(); i++) {
			
			int counter = 1;
			
			for (int j = 0; j < sequenceNumber[i]; j++) {
				
				bufferedWriter.newLine();
				bufferedWriter.write("   --------------------------"
						+ "--------------------");
				bufferedWriter.newLine();
				bufferedWriter.write("   HornettTea FTMK UTeM");
				bufferedWriter.newLine();
				bufferedWriter.write("   Order Number: " + orderNumber);
				bufferedWriter.newLine();
				bufferedWriter.write("   Date: " + readyDateTime);
				bufferedWriter.newLine();
				bufferedWriter.newLine();
				bufferedWriter.newLine();
				bufferedWriter.write("   Name: ");
				bufferedWriter.newLine();
				bufferedWriter.write("   " + productName[i]);
				bufferedWriter.newLine();
				bufferedWriter.newLine();
				bufferedWriter.write("   Sequence: " + counter 
						+ " / " + sequenceNumber[i]);
				bufferedWriter.newLine();
				bufferedWriter.write("   ----------------------------"
						+ "------------------");
				bufferedWriter.newLine();
				bufferedWriter.newLine();
				
				counter++;

			}
		
		}
		
		bufferedWriter.close();
		
	}
	
}
