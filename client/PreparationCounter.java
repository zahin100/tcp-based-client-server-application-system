package client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
 * This class is the view and TCP client side preparation counter.
 * 
 * @author Siti Hajar Binti Azi Shaufi
 *
 */

public class PreparationCounter extends JFrame {

	private JPanel contentPane;
	JTextArea orderDetailsTextArea;
	JTextArea stickersTextArea;

	// Create an object of Order to set data.
	Order order;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {

					PreparationCounter frame = new PreparationCounter();
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
	public PreparationCounter() throws IOException {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 40, 932, 665);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//create label "Preparation Counter"
		JLabel HornettTeaLabel = new JLabel("Preparation Counter");
		HornettTeaLabel.setForeground(Color.BLACK);
		HornettTeaLabel.setFont(new Font("Forte", Font.PLAIN, 30));
		HornettTeaLabel.setBounds(325, 23, 300, 50);
		contentPane.add(HornettTeaLabel);
		
		//create text field for amount received
		orderDetailsTextArea = new JTextArea();
		orderDetailsTextArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
		orderDetailsTextArea.setEditable(false);
		orderDetailsTextArea.setBounds(138, 93, 611, 412);
		contentPane.add(orderDetailsTextArea);

		//create "Display Order Details" button
		JButton preparingBtn = new JButton("DISPLAY ORDER DETAILS");
		preparingBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				/**
				 * Display Order Details
				 */
				try {

					// Server information
					int serverPortNumber = 8088;
					InetAddress serverAddress = InetAddress.getLocalHost();

					// Connect to remote machine
					Socket socket = new Socket(serverAddress, serverPortNumber);

					// Create streams to send request to the server.
					OutputStream outputStream = socket.getOutputStream();
					DataOutputStream dos = new DataOutputStream(outputStream);

					// Request number is 4 so that the server can respond with 
					// the correct data.
					int request = 4;
					dos.writeInt(request);
					dos.flush();

					// Create streams to read response from the server.
					InputStream inputStream4 = socket.getInputStream();

					// Read response from the server.
					ObjectInputStream ois4 = new ObjectInputStream(inputStream4);
					order = (Order) ois4.readObject();

					// Close the socket.
					socket.close();
					
					// Display the order details.
					displayOrderDetails(order);
					
					

				} catch (IOException | ClassNotFoundException e1) {

					e1.printStackTrace();
				}
			}
		});

		preparingBtn.setFont(new Font("Tahoma", Font.BOLD, 15));
		preparingBtn.setBackground(Color.LIGHT_GRAY);
		preparingBtn.setBounds(66, 546, 235, 50);
		contentPane.add(preparingBtn);

		//create "Print Stickers" button
		JButton readyBtn = new JButton("PRINT STICKERS");
		readyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				/**
				 * TCP Connection. Generate Stickers and change status from 
				 * preparing to ready
				 */
				
				// Set the order status from "Preparing" to "Ready".
				for (int i = 0; i < order.getOrderItems().size(); i++) {
					
					order.getOrderItems().get(i).setOrderStatus("Ready");
					
				}

				try {

					// Server information
					int serverPortNumber = 8088;
					InetAddress serverAddress = InetAddress.getLocalHost();

					// Connect to remote machine
					Socket socket = new Socket(serverAddress, serverPortNumber);

					// Create streams to send request to the server.
					OutputStream outputStream5 = socket.getOutputStream();
					DataOutputStream dos = new DataOutputStream(outputStream5);

					// Request number is 5 so that the server can respond with 
					// the correct data.
					int request = 5;
					dos.writeInt(request);
					dos.flush();

					// Send requests to the server.
					ObjectOutputStream oos5 = new 
							ObjectOutputStream(outputStream5);
					oos5.writeObject(order);
					
					// Close the socket.
					socket.close();
					
					// Display the stickers has been successfully 
					// printed message.
					JOptionPane.showMessageDialog(null, "The stickers has been "
							+ "successfully printed.");
					

				} catch (IOException e1) {

					e1.printStackTrace();
				}

			}

		});
		
		readyBtn.setFont(new Font("Tahoma", Font.BOLD, 15));
		readyBtn.setBackground(Color.LIGHT_GRAY);
		readyBtn.setBounds(325, 546, 235, 50);
		contentPane.add(readyBtn);

		//create a separator
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLUE);
		separator.setBounds(138, 76, 611, 1);
		contentPane.add(separator);

		//create a separator
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.BLUE);
		separator_1.setBounds(138, 524, 611, 1);
		contentPane.add(separator_1);
		
		//create "Collect Button" button
		JButton collectOrdersBtn = new JButton("COLLECT ORDER");
		collectOrdersBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				orderDetailsTextArea.setText("");				
				
				
			}
		});
		collectOrdersBtn.setFont(new Font("Tahoma", Font.BOLD, 15));
		collectOrdersBtn.setBackground(Color.LIGHT_GRAY);
		collectOrdersBtn.setBounds(587, 546, 235, 50);
		contentPane.add(collectOrdersBtn);
		
		JLabel label = new JLabel("");		
        // if true the component paints every pixel within its bounds
        label.setOpaque(true);
        label.setBackground(Color.PINK);
		label.setBounds(0, 0, 5000, 690);
		contentPane.add(label);
		
	}
		
		
	public void displayOrderDetails (Order order) throws IOException {
		
		// Get the order number.
		int orderNumber = order.getOrderNumber();
		
		// Read the order details file.
		String file = "OrderDetails" + orderNumber; 
		FileReader fr = new FileReader(file + ".txt");
		BufferedReader ReadFileBuffer = new BufferedReader(fr);
		
		// Read the text file
		
		String strCurrentLine;

		while ((strCurrentLine = ReadFileBuffer.readLine()) != null) {
			   orderDetailsTextArea.append("\n" + strCurrentLine);
		}
		
		//Close the Readers
		ReadFileBuffer.close();
		
	}
}
