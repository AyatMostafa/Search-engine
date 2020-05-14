package jdbc_demo;

import java.sql.*;

public class Driver {
	public static class DB{
		
		//DB connection
		
		public static Connection myConn;
		public static Statement myStmt;
		public static ResultSet myRs;
		
		 public DB(){
			myConn = null;
			myStmt = null;
			myRs = null;
		}
	 
	public static void make_connection() throws SQLException {
		// 1. Get a connection to database
		myConn = DriverManager.getConnection("jdbc:mysql://localhost/indexer_DB","root","01021592414ahmed");
	}
	public static void execute_update_quere(String query) throws SQLException {
		// 2. Create a statement
		myStmt = myConn.createStatement();
		
		// 3. Execute SQL query
		myRs = myStmt.executeQuery(query);
	}
	public static void execute_insert_quere(String query) throws SQLException {
		// 2. Create a statement
			myStmt = myConn.createStatement();
			
			// 3. Execute SQL query
			myRs = myStmt.executeQuery(query);
	}
	public  static ResultSet execute_select_query(String query) throws SQLException {
		// 2. Create a statement
		myStmt = myConn.createStatement();
		
		// 3. Execute SQL query
		myRs = myStmt.executeQuery(query);
		return myRs;
	}
//	private void test_connection() throws SQLException {
//			try {
//				// 1. Get a connection to database
//				myConn = DriverManager.getConnection("jdbc:mysql://localhost/demo","root","01021592414ahmed");
//				// 2. Create a statement
//				myStmt = myConn.createStatement();
//				
//				// 3. Execute SQL query
//				myRs = myStmt.executeQuery("select * from employees");
//				
//				// 4. Process the result set
//				while (myRs.next()) {
//					System.out.println(myRs.getString("last_name") + ", " + myRs.getString("first_name"));
//				}
//			}
//			catch (Exception exc) {
//				exc.printStackTrace();
//			}
//			finally {
//				if (myRs != null) {
//					myRs.close();
//				}
//				
//				if (myStmt != null) {
//					myStmt.close();
//				}
//				
//				if (myConn != null) {
//					myConn.close();
//				}
//			}
//	 }
	}
//	public static void main(String[] args) throws SQLException {
//		
//		Driver d=new Driver();
//		DB tst= d.new DB();
//		tst.make_connection();
////		tst.test_connection();
//	}

}
