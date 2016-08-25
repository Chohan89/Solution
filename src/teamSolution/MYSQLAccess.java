//Author: David Singh
package teamSolution;
import java.sql.*;

public class MYSQLAccess {
	
	// User's credentials 
	private String email,password,email_db,password_db;
	
	//User's name
	private String FirstName, LastName;
	
	// JDBC driver name and database URL
	static private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static private final String DB_URL = "jdbc:mysql://localhost:3306/SolutionSite";
	
	// init JDBC's connection object
	Connection conn = null;
	Statement stmt = null; 
	
	// Database credentials
	static private final String USER = "root";
	static private final String PASS = "root"; //future: do IO for pass
	
	/** Contact: will establish a connection to MYSQL DB and set network to true if 
	    connection is successful
	*/
	
	private void Contact() {
		if(conn != null) { 
			Disconnect(); //we have a connection open for some reason... force it closed
		}

		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}

	}// end function Contact


	/** Disconnect: will disconnect from MYSQL DB and set flag back to false for no connection.
	*/

	private boolean Disconnect() {
		//clean-up network environment
		try {
			conn.close();
			conn = null; //set conn to null, still may be pointing to something
		} catch (SQLException se) {
			se.printStackTrace();
			}
		return true;
	}//end function Disconnect



	/** ValidateUser: will create SQL string, contact DB, execute SQL, use logic to validate or refute user, disconnect contact and pass boolean 
	*/

	@SuppressWarnings("finally")
	public boolean ValidateUser(String Email, String Password) {
		this.email = Email.toUpperCase();		
		this.password = Password;
		String sql = "SELECT * FROM users WHERE email='" + email + "';";		
		boolean vali =false; //boolean we return

		Contact(); //establish connection to DB
		
		try {
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql); //execute SQL, expecting email and password
		
		while (rs.next()) {
				
			// Retrieve by column names in DB
			email_db = rs.getNString("Email");	
			password_db = rs.getNString("Password"); 

		}//end while
	
		if(email_db != null && password_db.equals(Password)) {
			System.out.println("Login successful");
			vali = true;
		}
		
		else {	
			System.out.println("Login failed");
		      }		

		} catch (SQLException se) {
			// Handles errors for JDBC
			se.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null) {	
					conn.close(); //LOOK INTO THIS. most likely you dont need
				}
			} catch (SQLException se2) {
				}// nothing we can do
		
		Disconnect(); //close DB connection
		return vali;

		}
	}//end ValidateUser
	
	
	/** CreateUser: Will be passed in order -> Email, Password, FirstName, LastName. Call internal methods to connect to db. SQL query to make sure email does not exist.
	 *  If user is new, do necessary SQL queries to create user. Will call Validate(pass email and password given during creation) to make sure user exists 
	 *  after SQL insertions. 
	 */
	
	public boolean CreatUser(String Email, String Password, String FirstName, String LastName) {
		boolean creHappen = false;
		this.email = Email.toUpperCase();
		this.password = Password;
		this.FirstName = FirstName;
		this.LastName = LastName;
		String sql = "INSERT INTO Accounts(Email,Password,FirstName,LastName) VALUES(" +email+ "," +password+ "," +FirstName+ "," +LastName+ ");";
		
		//establish connection
		Contact();
		
		//TO DO: CREAT METHOD TO SEARCH USER FOR EXISTENCE **THEN CALL IT HERE**
		
		return creHappen;
	}
	
	
	
	
	
	
	
	
	
 }//end class