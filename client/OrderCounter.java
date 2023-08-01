package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;

import model.ItemProduct;
import model.Order;
import model.OrderItem;

/**
 * This class manage View and TCP Client for OrderCounter .
 * 
 * @author Firzana Huda binti Azreel
 *
 */

public class OrderCounter extends JFrame {

	private JPanel contentPane;
	private JTextField productIdTextField;
	private JTextField quantityTextField;
	private JTextArea orderListTextArea;
	private JTextArea totalTextArea;
	
	public static final Color DARK_GREEN = new Color(0-153-0);
	
	// Define the values for cold beverages menu.
	private String coldMenu[] = {"[1] Signature Brown Sugar Pearl Milk Tea", 
			"[3] Original Pearl Milk Tea", "[5] Black Diamond Milk Tea",
			"[6] Red Bean Pearl Milk Tea", "[8] Earl Grey Milk Tea",
			"[10] Signature Milk Tea", "[12] Orginal Milk Tea",
			"[14] Signature Coffee", "[16] Coco Mocha","[21] Hazelnut Latte",
			"[23] Americano"};
	
	
	// Define the values for hot beverages menu.
	private String hotMenu[] = {"[2] Signature Brown Sugar Pearl Milk Tea",
			"[4] Original Pearl Milk Tea", "[7] Red Bean Pearl Milk Tea",
			"[9] Earl Grey Milk Tea", "[11] Signature Milk Tea", 
			"[13] Original Milk Tea", "[15] Signature Coffee", 
			"[17] Coco Mocha", "[22] Hazelnut Latte", "[24] Americano"};
	
	
	// Define an ArrayList to store the order items.
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	
	
	// Create objects of OrderItem to set data.
	OrderItem item1 = new OrderItem();
	OrderItem item2 = new OrderItem();
	OrderItem item3 = new OrderItem();
	OrderItem item4 = new OrderItem();
	OrderItem item5 = new OrderItem();
	OrderItem item6 = new OrderItem();
	OrderItem item7 = new OrderItem();
	OrderItem item8 = new OrderItem();
	OrderItem item9 = new OrderItem();
	OrderItem item10 = new OrderItem();
	
	
	// Create objects of ItemProduct to set data.
	ItemProduct product1 = new ItemProduct();
	ItemProduct product2 = new ItemProduct();
	ItemProduct product3 = new ItemProduct();
	ItemProduct product4 = new ItemProduct();
	ItemProduct product5 = new ItemProduct();
	ItemProduct product6 = new ItemProduct();
	ItemProduct product7 = new ItemProduct();
	ItemProduct product8 = new ItemProduct();
	ItemProduct product9 = new ItemProduct();
	ItemProduct product10 = new ItemProduct();
	
	
	// Create an object of Order to set data.
	Order order = new Order();

	
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				try {
					
					// launching OrderCounter frame
					
					OrderCounter frame = new OrderCounter(); 
					frame.setVisible(true);
					
				} catch (Exception e) {
					
					e.printStackTrace();
					
				}
			}
		});
		
	}
	

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	
	public OrderCounter() throws IOException {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(60, 20, 1244, 690);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//create label "Hornett Tea Cafe"
		JLabel HornettTeaLabel = new JLabel("Hornett Tea Cafe");
		HornettTeaLabel.setForeground(Color.BLACK);
		HornettTeaLabel.setFont(new Font("Forte", Font.PLAIN, 40));
		HornettTeaLabel.setBounds(479, 15, 337, 50);
		contentPane.add(HornettTeaLabel);
		
		//create label "Cold Drink Menu"
		JLabel coldLabel = new JLabel("COLD DRINK MENU");
		coldLabel.setForeground(Color.BLACK);
		coldLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
		coldLabel.setBounds(64, 64, 195, 42);
		contentPane.add(coldLabel);
		
		//create label "Hot Drink Menu"
		JLabel hotLabel = new JLabel("HOT DRINK MENU");
		hotLabel.setForeground(Color.BLACK);
		hotLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
		hotLabel.setBounds(366, 64, 187, 42);
		contentPane.add(hotLabel);
		
		//create cold menu text area
		JTextArea coldMenuTextArea = new JTextArea();
		coldMenuTextArea.setEditable(false);
		coldMenuTextArea.setBackground(Color.WHITE);
		coldMenuTextArea.setBounds(21, 117, 273, 348);
		
		// Set the text for cold menu text area.
		for(int i = 0; i < coldMenu.length; i++) {
			
			coldMenuTextArea.append("   " + coldMenu[i] + "\n\n");
			
		}
		
		contentPane.add(coldMenuTextArea);
		
		//create hot menu text area
		JTextArea hotMenuTextArea = new JTextArea();
		hotMenuTextArea.setEditable(false);
		hotMenuTextArea.setBackground(Color.WHITE);
		hotMenuTextArea.setBounds(317, 117, 273, 348);
		contentPane.add(hotMenuTextArea);
		
		// Set the text for hot menu text area.
		for(int i = 0; i < hotMenu.length; i++) {
			
			hotMenuTextArea.append("   " + hotMenu[i] + "\n\n");
			
		}
		
		contentPane.add(hotMenuTextArea);
		
		//create label "Product ID"
		JLabel lblProductID = new JLabel("Product ID");
		lblProductID.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblProductID.setForeground(Color.BLACK);
		lblProductID.setBounds(220, 496, 115, 22);
		contentPane.add(lblProductID);
		
		//create product ID text field
		productIdTextField = new JTextField();
		productIdTextField.setColumns(10);
		productIdTextField.setBackground(Color.WHITE);
		productIdTextField.setBounds(329, 497, 65, 22);
		contentPane.add(productIdTextField);
		
		//create label "Quantity"
		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblQuantity.setForeground(Color.BLACK);
		lblQuantity.setBounds(220, 529, 115, 22);
		contentPane.add(lblQuantity);
		
		//create quantity text field
		quantityTextField = new JTextField();
		quantityTextField.setColumns(10);
		quantityTextField.setBackground(Color.WHITE);
		quantityTextField.setBounds(329, 531, 65, 22);
		contentPane.add(quantityTextField);
		
		// Create button to add order
		JButton addOrderItemButton = new JButton("ADD TO ORDER");
		addOrderItemButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		addOrderItemButton.setForeground(new Color(51, 0, 51));
		addOrderItemButton.setBackground(Color.LIGHT_GRAY);
		addOrderItemButton.setBounds(124, 586, 169, 35);
		
		// Add product into the array list.
		addOrderItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Check if the product id's text field is empty.
				if (productIdTextField.getText().trim().equals("")) {
					
					JOptionPane.showMessageDialog(null,"Please enter the "
							+ "product's ID.");
					
				}
				
				// Check if the quantity's text field is empty.
				else if (quantityTextField.getText().trim().equals("")) {
					
					JOptionPane.showMessageDialog(null,"Please enter the"
							+ " quantity.");
					
				}
				
				else {
					
					// Get the quantity from the text field.
					int quantity = Integer.parseInt(
							quantityTextField.getText().trim());
					
					// Get the product id from the text field.
					int productId = Integer.parseInt(
							productIdTextField.getText().trim());
					
					// Add product into array list if the array list's size 
					// is less than 10 (maximum 10 type of beverages).
					if(orderItems.size() == 0) {
							
						// Set the quantity for the product.
						item1.setQuantity(quantity);
							
						// Set the product id for the product.
						product1.setItemProductId(productId);
						
						// Set the object for item product.
						item1.setItemproduct(product1);
						
						// Add the item into array list.
						orderItems.add(item1);
						
						// Display the updated the order list.
						updateOrderList(orderItems);
							
					}
						
					else if(orderItems.size() == 1) {
							
						// Set the quantity for the product.	
						item2.setQuantity(quantity);
						
						// Set the product id for the product.
						product2.setItemProductId(productId);
						
						// Set the object for item product.
						item2.setItemproduct(product2);
						
						// Add the item into array list.
						orderItems.add(item2);
						
						// Display the updated the order list.
						updateOrderList(orderItems);
					}
						
					else if(orderItems.size() == 2) {
							
						// Set the quantity for the product.	
						item3.setQuantity(quantity);
						
						// Set the product id for the product.
						product3.setItemProductId(productId);
						
						// Set the object for item product.
						item3.setItemproduct(product3);
						
						// Add the item into array list.
						orderItems.add(item3);
						
						// Display the updated the order list.
						updateOrderList(orderItems);
					}
						
					else if(orderItems.size() == 3) {
							
						// Set the quantity for the product.	
						item4.setQuantity(quantity);
						
						// Set the product id for the product.
						product4.setItemProductId(productId);
						
						// Set the object for item product.
						item4.setItemproduct(product4);
						
						// Add the item into array list.
						orderItems.add(item4);
						
						// Display the updated the order list.
						updateOrderList(orderItems);
					}
						
					else if(orderItems.size() == 4) {
							
						// Set the quantity for the product.	
						item5.setQuantity(quantity);
						
						// Set the product id for the product.
						product5.setItemProductId(productId);
						
						// Set the object for item product.
						item5.setItemproduct(product5);
						
						// Add the item into array list.
						orderItems.add(item5);
						
						// Display the updated the order list.
						updateOrderList(orderItems);
					}
						
					else if(orderItems.size() == 5) {
						
						// Set the quantity for the product.	
						item6.setQuantity(quantity);
						
						// Set the product id for the product.
						product6.setItemProductId(productId);
						
						// Set the object for item product.
						item6.setItemproduct(product6);
						
						// Add the item into array list.
						orderItems.add(item6);
						
						// Display the updated the order list.
						updateOrderList(orderItems);
					}
						
					else if(orderItems.size() == 6) {
							
						// Set the quantity for the product.	
						item7.setQuantity(quantity);
						
						// Set the product id for the product.
						product7.setItemProductId(productId);
						
						// Set the object for item product.
						item7.setItemproduct(product7);
						
						// Add the item into array list.
						orderItems.add(item7);
						
						// Display the updated the order list.
						updateOrderList(orderItems);
					}
						
					else if(orderItems.size() == 7) {
							
						// Set the quantity for the product.	
						item8.setQuantity(quantity);
						
						// Set the product id for the product.
						product8.setItemProductId(productId);
						
						// Set the object for item product.
						item8.setItemproduct(product8);
						
						// Add the item into array list.
						orderItems.add(item8);
						
						// Display the updated the order list.
						updateOrderList(orderItems);
					}
						
					else if(orderItems.size() == 8) {
							
						// Set the quantity for the product.	
						item9.setQuantity(quantity);
						
						// Set the product id for the product.
						product9.setItemProductId(productId);
						
						// Set the object for item product.
						item9.setItemproduct(product9);
						
						// Add the item into array list.
						orderItems.add(item9);
						
						// Display the updated the order list.
						updateOrderList(orderItems);
					}
						
					else if(orderItems.size() == 9) {
							
						// Set the quantity for the product.	
						item10.setQuantity(quantity);
						
						// Set the product id for the product.
						product10.setItemProductId(productId);
						
						// Set the object for item product.
						item10.setItemproduct(product10);
						
						// Add the item into array list.
						orderItems.add(item10);
						
						// Display the updated the order list.
						updateOrderList(orderItems);
					}
					
					else {
						
						JOptionPane.showMessageDialog(null, "Only maximum 10 "
								+ "type of beverages are allowed.");
					}
					
					productIdTextField.setText("");
					quantityTextField.setText("");
				}
			}
		});
		
		contentPane.add(addOrderItemButton);
		
		// create button to remove order 
		JButton removeOrderItemButton = new JButton("REMOVE FROM ORDER");
		removeOrderItemButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		removeOrderItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Check if the product id's text field is empty.
				if (productIdTextField.getText().trim().equals("")) {
					
					JOptionPane.showMessageDialog(null,"Please enter the "
							+ "product's ID.");
					
				}
				
				else {
				
					// Get the product id from the text field.
					int productId = Integer.parseInt(
							productIdTextField.getText().trim());
					
					// Find the item to be removed.
					for(int i = 0; i < orderItems.size(); i++) {
						
						if(orderItems.get(i).getItemproduct().getItemProductId() 
								== productId) {
							
							orderItems.remove(i);
							
						}
					}
					
					// Display the updated order list.
					updateOrderList(orderItems);
					productIdTextField.setText("");
					quantityTextField.setText("");
					
				}
				
			}
		});
		removeOrderItemButton.setForeground(new Color(51, 0, 51));
		removeOrderItemButton.setBackground(Color.LIGHT_GRAY);
		removeOrderItemButton.setBounds(314, 586, 169, 35);
		contentPane.add(removeOrderItemButton);
		
		//create a separator
		JSeparator separator = new JSeparator();
		separator.setBounds(100, 484, 413, 16);
		contentPane.add(separator);
		
		//create a separator
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(100, 632, 413, 10);
		contentPane.add(separator_1);
		
		//create order list text area
		orderListTextArea = new JTextArea();
		orderListTextArea.setEditable(false);
		orderListTextArea.setBackground(Color.WHITE);
		orderListTextArea.setBounds(635, 117, 549, 251);
		contentPane.add(orderListTextArea);
		
		// create button for calculate total price
		JButton calculateButton = new JButton("CALCULATE TOTAL");
		calculateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Check if the array list is empty.
				if (orderItems.isEmpty() == true) {
					
					JOptionPane.showMessageDialog(null,"Please add products "
							+ "into the order list.");
					
				}
				
				else {
					
					// Set the array list object into Order class.
					order.setOrderItems(orderItems);
					
					
					try {
						
						// Server information
						int serverPortNumber = 8088;
						InetAddress serverAddress = InetAddress.getLocalHost();
						
						// Connect to remote machine
						Socket socket = new Socket(serverAddress, 
								serverPortNumber);
						
						// Create streams to send request to the server.
						OutputStream outputStream = socket.getOutputStream();
						DataOutputStream dos = new DataOutputStream(
								outputStream);
						
						// Request number is 1 so that the server can respond 
						// with the correct data.
						int request = 1;
						dos.writeInt(request);
						dos.flush();
						
						// Send requests to the server.
						ObjectOutputStream oos = new 
								ObjectOutputStream(outputStream);
						oos.writeObject(order);
						
						// Create streams to read response from the server.
						InputStream inputStream = socket.getInputStream();
						
						// Read response from the server.
						ObjectInputStream ois = new ObjectInputStream(
								inputStream);
						order = (Order) ois.readObject();
						
						// Close the socket.
						socket.close();
						
						// Display the sub total, service tax, rounding and 
						// total.
						displayTotal(order);
						
						
					} catch (IOException | ClassNotFoundException e1) {
						
						e1.printStackTrace();
					}
					
					productIdTextField.setText("");
					quantityTextField.setText("");
					
				}
			}
		});
		
		calculateButton.setForeground(new Color(51, 0, 51));
		calculateButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		calculateButton.setBackground(Color.LIGHT_GRAY);
		calculateButton.setBounds(942, 409, 169, 35);
		contentPane.add(calculateButton);
		
		// Create button for paycash
		JButton payCashButton = new JButton("PAY CASH");
		payCashButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
							
				PayCash frame = new PayCash();
				frame.setVisible(true);
				frame.order = order;
				
			}
		}); 
		
		payCashButton.setForeground(new Color(51, 0, 51));
		payCashButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		payCashButton.setBackground(Color.LIGHT_GRAY);
		payCashButton.setBounds(825, 573, 169, 35);
		contentPane.add(payCashButton);
		
		// Create button for cancel order
		JButton cancelOrderButton = new JButton("CANCEL ORDER");
		cancelOrderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Clear the order list text area.
				orderListTextArea.setText("   PRODUCT ID " 
							+ "    QUANTITY" + "\n\n");
				
				// Empty the array list.
				orderItems.clear();
				
				// Clear the total text area, productIdTextField and 
				// quantityTextField.
				totalTextArea.setText("");
				productIdTextField.setText("");
				quantityTextField.setText("");
				
			}
		});
		cancelOrderButton.setForeground(new Color(51, 0, 51));
		cancelOrderButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		cancelOrderButton.setBackground(Color.LIGHT_GRAY);
		cancelOrderButton.setBounds(721, 409, 169, 35);
		contentPane.add(cancelOrderButton);
		
		//create a separator
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(635, 466, 549, 16);
		contentPane.add(separator_2);
		
		//create total text area
		totalTextArea = new JTextArea();
		totalTextArea.setEditable(false);
		totalTextArea.setBackground(Color.WHITE);
		totalTextArea.setBounds(635, 484, 549, 67);		
		contentPane.add(totalTextArea);
		
		//create label "Order List"
		JLabel orderListLabel = new JLabel("ORDER LIST");
		orderListLabel.setForeground(Color.BLACK);
		orderListLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
		orderListLabel.setBounds(635, 64, 169, 42);
		contentPane.add(orderListLabel);
		
		JLabel label = new JLabel("");		
        // if true the component paints every pixel within its bounds
        label.setOpaque(true);
        label.setBackground(Color.PINK);
		label.setBounds(0, 0, 5000, 690);
		contentPane.add(label);
	}
	
	
	// Method to update the order list.
	void updateOrderList(List<OrderItem> orderItems) {
		
		// Clear the text area.
		orderListTextArea.setText("   PRODUCT ID " + "    QUANTITY" + "\n\n");
		
		// Display the updated order list.
		for(OrderItem orderItem : orderItems) {
			
			orderListTextArea.append("          [" +
					orderItem.getItemproduct().getItemProductId() + "]" 
					+ "\t                " + orderItem.getQuantity() + "\n");
		}
	}
	
	
	// Method to display the total.
	void displayTotal(Order order) {
		
		// Display value in 2 decimal places.
		DecimalFormat df1 = new DecimalFormat("###.00");
		String subTotal = df1.format(order.getSubTotal());
		
		DecimalFormat df2 = new DecimalFormat("##0.00");
		String serviceTax = df2.format(order.getServiceTax());
		
		DecimalFormat df3 = new DecimalFormat("##0.00");
		String rounding = df3.format(order.getRounding());
		
		DecimalFormat df4 = new DecimalFormat("###.00");
		String grandTotal = df4.format(order.getGrandTotal());
		
		// Set the values for the subtotal and total price.
		totalTextArea.append("      Subtotal: RM " + subTotal + "\n" + 
				"Service Tax: RM " + serviceTax + "\n" + "    Rounding: RM "
				+ rounding + "\n" + "           Total: RM " + grandTotal);
	}
	
	
}


	