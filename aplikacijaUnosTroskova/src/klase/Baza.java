package klase;

import java.sql.Connection;
import java.sql.DriverManager;

public class Baza {
	
	private static final String url="jdbc:mysql://ucka.veleri.hr/jbarbir?serverTimezone=UTC";
	private static final String username="jbarbir";
	private static final String password="11";
	
	private static Connection conn;
	
	
	
	public static Connection getConnection() {
		if(conn==null) {
			try {
				conn=DriverManager.getConnection(url,username,password);
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
		return conn;
	}
	
	public static Connection getConn() {
		return conn;
	}
	
	
}
