import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.lang.SecurityManager;


public class SQLiteDemo {
	/*
	private static final String DB_HOST = "sophia";
	private static final String DB_USER = "";
	private static final String DB_PASS = "";
	private static final String DB_NAME = "";
	*/
	private static final String DB_FILE = "c3402.sqlite";
	
	public static void main(String[] args) {
		System.setSecurityManager(new SecurityManager());
		
		try {
			new SQLiteDemo().go();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			System.err.println("Connection failed: "+e);
		}
	}
	private Connection conn;
	public SQLiteDemo() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
			/*
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://"+DB_HOST+
					"/"+DB_NAME+
					"?user="+DB_USER+
					"&password="+DB_PASS);
			*/ 
			// Change driver
			Class.forName("org.sqlite.JDBC").newInstance();
			conn = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE);
			
			System.out.println("Database connection successful.");
	}
	public void go() {
		
		Scanner keyboard = new Scanner(System.in);
		String line;
		System.out.print("> ");
		while(!(line = keyboard.next()).equals("exit")) {
			if(line.equals("create")) {
				insert(keyboard.next(), keyboard.next());
			} else if(line.equals("read")) {
				read(keyboard.next());
			} else if(line.equals("birthday")) {
				findByBirthday(keyboard.next());
			} else if(line.equals("list")) {
				list();
			} else if(line.equals("update")) {
				update(keyboard.next(), keyboard.next());
			} else if(line.equals("delete")) {
				delete(keyboard.next());
			}
			System.out.print("> ");
		}
		keyboard.close();
	}
	private void insert(String name, String birthday) {
		try {
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO c3402_2015 (name, birthday) VALUES (?, DATE(?))");
			stmt.setString(1, name);
			stmt.setString(2, birthday);
			stmt.execute();
			System.out.println("Record created");
		} catch (SQLException | IllegalArgumentException e) {
			System.err.println("Error inserting record: "+e);
		}
	}
	private void read(String name) {
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT birthday FROM c3402_2015 WHERE name = ?");
			stmt.setString(1, name);
			
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				System.out.println("Birthday of "+name+" is on "+rs.getString(1));
			} else {
				System.out.println(name+" not found!");
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
		}
	}
	private void findByBirthday(String birthday) {
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT name FROM c3402_2015 WHERE birthday = DATE(?)");
			stmt.setString(1, birthday);
			
			ResultSet rs = stmt.executeQuery();
			int count = 0;
			while(rs.next()) {
				System.out.println("Birthday of "+rs.getString(1)+" is on "+birthday);
				count++;
			} 
			if(count == 0) {
				System.out.println(birthday+" not found!");
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
		}
	}
	private void list() {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT name, birthday FROM c3402_2015");
			while(rs.next()) {
				System.out.println("Birthday of "+rs.getString(1)+" is on "+rs.getString(2));
			}
		} catch (SQLException e) {
			System.err.println("Error listing records: "+e);
		}
	}
	private void update(String name, String birthday) {
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE c3402_2015 SET birthday = DATE(?) WHERE name = ?");
			stmt.setString(1, birthday);
			stmt.setString(2, name);
			
			int rows = stmt.executeUpdate();
			if(rows > 0){
				System.out.println("Birthday of "+name+" updated");
			} else {
				System.out.println(name+" not found!");
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
		}
	}
	private void delete(String name) {
		try {
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM c3402_2015 WHERE name = ?");
			stmt.setString(1, name);
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.println("Record of "+name+" removed");
			} else {
				System.out.println(name+" not found!");
			}
		} catch (SQLException | IllegalArgumentException e) {
			System.err.println("Error inserting record: "+e);
		}
	}
}