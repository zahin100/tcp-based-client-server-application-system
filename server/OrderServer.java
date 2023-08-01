package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import model.Order;


/**
 * This class is the server side TCP order server.
 * 
 * @author Akmal Zahin bin Zulkepli
 *
 */

public class OrderServer {

	private static Order order2;
	
	public static void main(String[] args) {
		
		// Declare port number.
		int portNumber = 8088;
		
		// Create object for ProductManager.
		OrderManager orderManager = new OrderManager();
		
		try {
			
			// Bind to port number.
			ServerSocket serverSocket = new ServerSocket(portNumber);
			
			
			// The server need to continuously run to listen to requests.
			while (true) {
				
				// Accept request from client.
				Socket socket = serverSocket.accept();
				System.out.println("Client connected.");
				
				// Get the request number.
				InputStream inputStream = socket.getInputStream();
				DataInputStream dis = new DataInputStream(inputStream);
				int request = dis.readInt();
				System.out.println("Request number: " + request);
				
				
				// Respond to client based on the request number.
				// Request 1 = Calculate total.
				if (request == 1) {
					
					// Create stream to get object from client.
					ObjectInputStream ois = new ObjectInputStream(inputStream);
					Order order = (Order) ois.readObject();
					
					// Process the object.
					// Calculate the total.
					order = orderManager.calculateTotal(order);
					
					// Create stream to send object to client.
					OutputStream outputStream = socket.getOutputStream();
					ObjectOutputStream oos = new 
							ObjectOutputStream(outputStream);
					oos.writeObject(order);
					
					socket.close();
					
					System.out.println("Sending subtotal RM " + 
							order.getSubTotal() + ", service tax RM " + 
							order.getServiceTax() + ", rounding RM " + 
							order.getRounding() + ", and total RM " + 
							order.getGrandTotal() + " to client.");
				}
				
				
				// Request 2 = Calculate balance.
				else if (request == 2) {
					
					// Create stream to get object from client.
					ObjectInputStream ois2 = new ObjectInputStream(inputStream);
					Order order = (Order) ois2.readObject();
					
					// Process the object.
					// Calculate balance.
					order = orderManager.calculateBalance(order);
					
					// Insert data into the Order table.
					order = orderManager.insertDataIntoOrder(order);
					
					// Insert data into the OrderItem table.
					order = orderManager.insertDataIntoOrderItem(order);
					
					// Create stream to send object to client.
					OutputStream outputStream2 = socket.getOutputStream();
					ObjectOutputStream oos2 = new 
							ObjectOutputStream(outputStream2);
					oos2.writeObject(order);
					
					order2 = order;
					
					socket.close();
					
					System.out.println("Sending balance RM " + order.getChange()
								+ " to client.");
				}
				
				
				// Request 3 = Generate a receipt.
				else if (request == 3) {
					
					// Create stream to get object from client.
					ObjectInputStream ois3 = new ObjectInputStream(inputStream);
					Order order = (Order) ois3.readObject();
					
					// Process the object.
					// Generate a receipt in a file.
					orderManager.generateReceipt(order);
					
					socket.close();
					
					System.out.println("The order's receipt has been "
							+ "generated.");
					
				}
				
				
				// Request 4 = Generate order details.
				else if (request == 4) {
					
					// Generate order details in a file.
					orderManager.displayOrderDetails(order2);
					
					// Create stream to send object to client.
					OutputStream outputStream4 = socket.getOutputStream();
					ObjectOutputStream oos4 = new 
							ObjectOutputStream(outputStream4);
					oos4.writeObject(order2);
					
					socket.close();
					
					System.out.println("The order's details has been "
							+ "generated.");
					
				}
				
				
				// Request 5 = Update order status from "Preparing" to "Ready"
				// and generate stickers.
				else if (request == 5) {
					
					// Create stream to get object from client.
					ObjectInputStream ois5 = new ObjectInputStream(inputStream);
					Order order = (Order) ois5.readObject();
					
					// Generate stickers in a file.
					orderManager.generateStickers(order);
					
					socket.close();
					
					// Update the order status from "Preparing" to "Ready".
					orderManager.updateOrderStatus(order);
					
					System.out.println("The order's stickers has been "
							+ "generated.");
					
				}

			}
			
		} catch (IOException | ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		}
	}

}
