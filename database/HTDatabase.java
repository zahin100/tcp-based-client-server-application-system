package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class manage the database connection.
 * 
 * @author Akmal Zahin bin Zulkepli
 *
 */

public class HTDatabase {
	
	static Connection conn = null;
	
	public static Connection doConnection() throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ht_db","root","");
		
		return conn;
	}
	
	
	public static void main(String[] args) {
		
		try {
			
			System.out.println(HTDatabase.doConnection());
			
		} catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}
	}
}
	
	
