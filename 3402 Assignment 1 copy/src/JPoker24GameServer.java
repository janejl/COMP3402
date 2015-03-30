import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.*;
import java.rmi.server.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("serial")
public class JPoker24GameServer extends UnicastRemoteObject implements GameServer{
	
	private int numOfClients;
	private Connection conn;
	private static final String DB_FILE = "jpoker24game.sqlite"; // connection to a DB
	
	public static void main(String[] args){
		try{
			JPoker24GameServer app = new JPoker24GameServer();
			System.setSecurityManager(new SecurityManager());
			
			/* Bind the remote object app to the rmiregistry using Naming.rebind(). 
			 * The name registered ("Server") will be used by clients when look up in the rmi registry.
			 */
			Naming.rebind("Server", app);
			System.out.println("Service registered!");
		}catch (Exception e){
			System.err.println("[Server main]Exception thrown: "+e);
		}
	}
	
	// Show that RemoteException is thrown by super
	protected JPoker24GameServer() throws RemoteException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		super();
		numOfClients = 0;
		
		//Set up JDBC driver
		Class.forName("org.sqlite.JDBC").newInstance();
		conn = DriverManager.getConnection("jdbc:sqlite:"+DB_FILE);
	}
	
	/* Login service: verify user
	 * return:	1: success
	 * 			2: duplicating login
	 * 			0: user doesn't exist
	 * 			-1: invalid password
	 * 			-2: unknown error
	 */
	public synchronized int loginService(String user_name, String password) throws RemoteException{
		try{
			// Get user information from UserInfo.txt and verify			
			Scanner scan = new Scanner(new File("UserInfo.txt"));
			String[] line;
			String temp_name, temp_password;
			while(scan.hasNextLine()){
				line = scan.nextLine().split(" ");
				temp_name = line[0];
				temp_password = line[1];
				if(temp_name.equals(user_name) && temp_password.equals(password)){
					
					boolean isDuplication = checkDuplication(user_name);				
					if(!isDuplication){
						updateOnlineUser(user_name);
						++numOfClients;
						System.out.println("Login: " + user_name + " success! Number of online users: " + numOfClients);						
						scan.close();
						return 1;
					}
					System.out.println("Login fail: duplicating user!");
					scan.close();
					return 2;
				}else if(temp_name.equals(user_name) && (!temp_password.equals(password))){
					System.out.println("Login fail: invalid password!");
					scan.close();
					return -1;
				}
			}
			System.out.println("Login fail: user does not exist!");
			scan.close();
			return 0;
		}catch(Exception e){
			System.err.println("[Server loginService]Exception thrown: "+e);
			return -2;
		}
	}
	
	// Logout service: delete the specified user from OnlineUser.txt 	
	public synchronized boolean logoutService(String user_name) throws RemoteException{
		try{
			String line;
			ArrayList<String> data = new ArrayList<String>();
			Scanner scan = new Scanner(new File("OnlineUser.txt"));
			
			while(scan.hasNextLine()){
				line = scan.nextLine();
				if(!line.equals(user_name)){
					data.add(line);
				}
			}
			scan.close();
			
			// Store online users' names except the one logging out and overwrite original file
			PrintWriter out = new PrintWriter(new FileWriter("OnlineUser.txt", false));
			if(!data.isEmpty()){
				for(int i=0; i<data.size(); ++i){
					out.println(data.get(i));
				}
			}else{
				out.println("");
			}
			out.flush();
			out.close();
			
			--numOfClients;
			System.out.println("Logout " + user_name + " success! Number of online users: " + numOfClients);	
			
			return true;			
		}catch(Exception e){
			System.err.println("[Server logoutService]Exception thrown: "+e);
			return false;
		}	
	}
	
	// Register service: avoid duplicating user name with UserInfo.txt
	// also login user and update OnlineUser.txt
	
	/* Register service: avoid duplicating user name with UserInfo.txt
	 * 					 also login user and update OnlineUser.txt
	 * return:	1: success
	 * 			2: duplicating name
	 * 			-2: unknown error
	 */
	public synchronized int registerService(String user_name, String password) throws RemoteException{
		try{
			Scanner scan = new Scanner(new File("UserInfo.txt"));
			String temp_name;
			while(scan.hasNextLine()){
				temp_name = scan.nextLine().split(" ")[0];				
				if(temp_name.equals(user_name)){
					System.out.println("Register: duplicating user name!");
					scan.close();
					return 2;
				}
			}
			scan.close();
			
			// After duplication check, register user
			PrintWriter out_register = new PrintWriter(new FileWriter("UserInfo.txt", true));
			out_register.println(user_name + " " + password);
			out_register.flush();
			out_register.close();
			
			// After registration, login user
			PrintWriter out_login = new PrintWriter(new FileWriter("OnlineUser.txt", true));
			out_login.println(user_name);
			out_login.flush();
			out_login.close();
			
			++numOfClients;
			System.out.println("Register: " + user_name + " success! Number of online users: " + numOfClients);
			return 1;
		}catch(IOException e){
			System.err.println("[Server registerService]Exception thrown: "+e);
			return -2;// problem here
		}
	}
	
	// Helper of login service
	private void updateOnlineUser(String user_name) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter("OnlineUser.txt", true));
			out.println(user_name);
			out.flush();
			out.close();
		} catch(IOException e) {
			System.err.println("[Server updateOnlineUser]Exception thrown: "+e);
		}
	}

	// Helper of login service
	// Policy: different users cannot have the same user_name
	private boolean checkDuplication(String user_name) {
		try{
			Scanner scan = new Scanner(new File("OnlineUser.txt"));
			String temp_name;
			while(scan.hasNextLine()){
				temp_name = scan.nextLine();				
				if(temp_name.equals(user_name)){
					scan.close();
					return true;
				}
			}
			scan.close();
			return false;
		}catch(IOException e){
			System.err.println("[Server checkDuplication]Exception thrown: "+e);
			return true;// problem here
		}	
	}
	
	
}
