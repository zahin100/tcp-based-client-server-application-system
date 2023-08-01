package client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.Order;
import client.InvalidCashAmountException;

/**
 * This class manage View and TCP Client for PayCash .
 * 
 * @author Firzana Huda binti Azreel
 *
 */

public class PayCash extends JFrame {
	
	private JPanel contentPane;
	private JTextField amountReceivedTextField;
	private JTextField balanceTextField;
	private JTextArea receiptTextArea;
	public Order order;
	private JTextField orderNumberTextField;

	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					// launching paycash frame
					PayCash frame = new PayCash();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public PayCash()  {
		
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 150, 500, 312);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//create label "PayCash"
		JLabel PaycashLabel = new JLabel("PayCash");
		PaycashLabel.setForeground(Color.BLACK);
		PaycashLabel.setFont(new Font("Forte", Font.PLAIN, 30));
		PaycashLabel.setBounds(180, 25, 245, 42);
		contentPane.add(PaycashLabel);
		
		//create label "Amount Received (RM)"
		JLabel lblAmountReceived = new JLabel("Amount Received (RM)");
		lblAmountReceived.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblAmountReceived.setBounds(50, 90, 190, 22);
		contentPane.add(lblAmountReceived);
		
		//create text field for amount received
		amountReceivedTextField = new JTextField();
		amountReceivedTextField.setColumns(10);
		amountReceivedTextField.setBounds(250, 90, 200, 22);
		contentPane.add(amountReceivedTextField);
		
		//create label "Balance (RM)"
		JLabel lblBalance = new JLabel("Balance (RM)");
		lblBalance.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblBalance.setBounds(127, 130, 125, 22);
		contentPane.add(lblBalance);
		
		//create text field for balance
		balanceTextField = new JTextField();
		balanceTextField.setEditable(false);
		balanceTextField.setColumns(10);
		balanceTextField.setBounds(250, 130, 200, 22);
		contentPane.add(balanceTextField);
		
		//create button confirm
		JButton btnConfirm = new JButton("DONE");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				double amount = 0;
				
				// Check if the amount received text field is empty.
				if (amountReceivedTextField.getText().trim().equals("")) {
					
					JOptionPane.showMessageDialog(null,"Please enter the "
							+ "payment amount.");
					
				}
				
				else {
					
					try {
							
						// Get the amount from the text field.
						amount = Double.parseDouble(
								amountReceivedTextField.getText().trim());
							
						// Set the amount of cash received from the customer.
						order.setTenderedCash(amount);
							
					} catch (InvalidCashAmountException e2) {
							
						JOptionPane.showMessageDialog(null, e2.getMessage());
							
					}
					
					
					if (amount >= 1) {
						
						// Set the order status as "Preparing".
						for (int i = 0; i < order.getOrderItems().size(); i++) {
							
							order.getOrderItems().get(i).setOrderStatus(
									"Preparing");
							
						}
						
						
						try {
							
							// Server information
							int serverPortNumber = 8088;
							InetAddress serverAddress = 
									InetAddress.getLocalHost();
							
							// Connect to remote machine
							Socket socket = new Socket(serverAddress, 
									serverPortNumber);
							
							// Create streams to send request to the server.
							OutputStream outputStream = 
									socket.getOutputStream();
							DataOutputStream dos = new DataOutputStream(
									outputStream);
							
							// Request number is 2 so that the server can 
							// respond with the correct data.
							int request = 2;
							dos.writeInt(request);
							dos.flush();
							
							// Send requests to the server.
							ObjectOutputStream oos2 = new 
									ObjectOutputStream(outputStream);
							oos2.writeObject(order);
							// Create streams to read response from the server.
							InputStream inputStream2 = socket.getInputStream();
							
							// Read response from the server.
							ObjectInputStream ois2 = new ObjectInputStream(
									inputStream2);
							order = (Order) ois2.readObject();
							
							// Close the socket.
							socket.close();
							
							// Display the balance
							displayBalance(order);
							
							// Display the order number.
							String orderNumber = Integer.toString(
									order.getOrderNumber());
							orderNumberTextField.setText(orderNumber);
							
						} catch (IOException | ClassNotFoundException e1) {
							
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
		btnConfirm.setBounds(88, 225, 140, 31);
		btnConfirm.setForeground(new Color(51, 0, 51));
		btnConfirm.setBackground(Color.LIGHT_GRAY);
		contentPane.add(btnConfirm);
		
		//create receipt button
		JButton btnReceipt = new JButton("PRINT RECEIPT");
		btnReceipt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					try {
					
					// Server information
					int serverPortNumber = 8088;
					InetAddress serverAddress = InetAddress.getLocalHost();
					
					// Connect to remote machine
					Socket socket = new Socket(serverAddress, serverPortNumber);
					
					// Create streams to send request to the server.
					OutputStream outputStream = socket.getOutputStream();
					DataOutputStream dos = new DataOutputStream(outputStream);
					
					// Request number is 3 so that the server can respond with 
					// the correct data.
					int request = 3;
					dos.writeInt(request);
					dos.flush();
					
					// Send requests to the server.
					ObjectOutputStream oos2 = new 
							ObjectOutputStream(outputStream);
					oos2.writeObject(order);
					
					// Close the socket.
					socket.close();
					
					// Display the receipt has been successfully 
					// printed message.
					JOptionPane.showMessageDialog(null, "The receipt has been "
							+ "successfully printed.");
					
					
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
					
			}
		});
		
		btnReceipt.setBounds(249, 225, 140, 31);
		btnReceipt.setForeground(new Color(51, 0, 51));
		btnReceipt.setBackground(Color.LIGHT_GRAY);
		contentPane.add(btnReceipt);
		
		//create label "Order Number"
		JLabel lblOrderNumber = new JLabel("Order Number");
		lblOrderNumber.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblOrderNumber.setBounds(124, 164, 140, 31);
		contentPane.add(lblOrderNumber);
		
		//create text field for order number received
		orderNumberTextField = new JTextField();
		orderNumberTextField.setEditable(false);
		orderNumberTextField.setColumns(10);
		orderNumberTextField.setBounds(250, 170, 200, 22);
		contentPane.add(orderNumberTextField);
		
		JLabel label = new JLabel("");		
        // if true the component paints every pixel within its bounds
        label.setOpaque(true);
        label.setBackground(Color.PINK);
		label.setBounds(0, 0, 5000, 690);
		contentPane.add(label);
			
	}
	
	
	// Method to display balance to the client
	void displayBalance(Order order) {
		
		// Display value in 2 decimal places 
		DecimalFormat df = new DecimalFormat("##0.00");
		String balance = df.format(order.getChange());
		
		// Set the values for the balance
		balanceTextField.setText(balance);
		
	}
	
}