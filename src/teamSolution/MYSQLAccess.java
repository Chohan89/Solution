//Author: David Singh
package teamSolution;
import java.sql.*;

public class MYSQLAccess {
	
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/SolutionSite";
	
	static // init JDBC's connection object
	Connection conn = null;
	static Statement stmt = null; 
	
	// Database credentials
	static final String USER = "root";
	static final String PASS = "root"; //future: do IO for pass

//	---------------------------------------------------------------------Contact and Disconnect methods only-----------------------------------------------------
	/** Contact: will establish a connection to MYSQL DB and set network to true if 
    connection is successful
*/

private static void Contact() {
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

private static boolean Disconnect() {
	//clean-up network environment
	try {
		conn.close();
		conn = null; //set conn to null, still may be pointing to something
	} catch (SQLException se) {
		se.printStackTrace();
		}
	return true;
}//end function Disconnect	
//--------------------------------------------------------------Validate User---------------------------------------------------------------------------------


/** ValidateUser: will create SQL string, contact DB, execute SQL, use logic to validate or refute user, disconnect contact and pass boolean 
*/

public static boolean ValidateUser(String InEmail, String InPassword) {
	String Email,Password,Email_db = null,Password_db = null;		
	Email = InEmail.toUpperCase();
	Password = InPassword;
	String sql = "SELECT * FROM Accounts WHERE Email='" + Email + "';";		
	boolean vali =false; //boolean we return

	Contact(); //establish connection to DB
	
	try {
	stmt = conn.createStatement();
	ResultSet rs = stmt.executeQuery(sql); //execute SQL, expecting email and password
	
	while (rs.next()) {
			
		// Retrieve by column names in DB
		Email_db = rs.getNString("Email");	
		Password_db = rs.getNString("Password"); 

	}//end while

	if(Email_db != null && Password_db.equals(Password)) {
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
		finally{}
	}//end first finally
	
	Disconnect(); //close DB connection
	return vali;

}//end ValidateUser
//-------------------------------------------------------------------------------------------------------------------------------------------------

//--------------------------------------------------------UserExists method only-------------------------------------------------------------------


/** UserExists: takes in an Email and an SQL query is done to determine if user exists or not.
 * 				Returns boolean depending on existence.  
 * 	Notes: DB connection and disconnection should be callers problem not this routines
 * 
 * @param Email
 * @return
 */
private static boolean UserExists(String InEmail) {
	boolean UserExists = false;
	String Email_db = null;
	String Email = InEmail.toUpperCase();
	String sql = "SELECT * FROM Accounts WHERE Email='" + Email + "';";
	
	//establish connection to DB
	// "Contact(); //made contact to DB"  commented out because caller should deal w/ contact and disconnect 
	
	try{
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql); //execute SQL, expecting back email only
		
		while(rs.next()) {
			Email_db = rs.getNString("Email"); //retrieve by column name
		}//end while
		
		if(Email_db != null && Email_db == Email) { //compare what was retrieved to what was sent
			UserExists = true;
		}//end if
	} catch (SQLException se2) {
	}// nothing we can do
	finally{}
	// "Disconnect(); //close DB connection" commented out because caller should deal w/ contact and disconnect 
	return UserExists;
	
}//end UserExists method
//-----------------------------------------------------------------------------------------------------------------------------------------------------

//----------------------------------------------------------CreatUser method only ---------------------------------------------------------------------

/** CreateUser: Will be passed in order -> Email, Password, FirstName, LastName. Call internal methods to connect to db. SQL query to make sure email does not exist.
 *  If user is new, do necessary SQL queries to create user. Will call Validate(pass email and password given during creation) to make sure user exists 
 *  after SQL insertions. 
 */

  public static boolean CreateUser(String InEmail, String InPassword, String InFirstName, String InLastName) {
	boolean creHappen = false;
	String Email = InEmail.toUpperCase();
	String Password = InPassword;
	String FirstName = InFirstName;
	String LastName = InLastName;  
	boolean Exists = false;
	String sql = "INSERT INTO Accounts(Email,Password,FirstName,LastName) VALUES(" +Email+ "," +Password+ "," +FirstName+ "," +LastName+ ");";
	
	//establish connection
	Contact();
	
	//Call UserExists to see if email ALREADY exists or not
	Exists = UserExists(Email); //
	if(Exists == false) {
		try{
			stmt = conn.createStatement();
			stmt.executeQuery(sql); //execute your query its an INSERT so nothing returning -.- just sending INSERT params that are in our string sql here locally
			Exists = true;
		} catch(SQLException se2) {
			//nothing we can do
		} finally{}
		Disconnect(); //close DB connection
	}
	
	return creHappen;
}//end CreatUser









 }//end class