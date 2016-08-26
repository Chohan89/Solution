package teamSolution;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

			//lets create a dummy user for testing purposes (for both connection and execution testing
			
			String email = "Dave@gmail.com", password = "dave", fname = "dave", lname = "chohan";
			boolean MakeUser=false;
			
			//lets create this amazing user =D
			
			MakeUser = MYSQLAccess.CreateUser(email,password,fname,lname);

	}

}
