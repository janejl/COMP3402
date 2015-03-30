import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class JPoker24Game {
	
	private String userName;
	private String userPassword;
	private String confirmPassword;
	GameServer server;
	
	JFrame frame;
	JPanel mainPanel;	
	JLabel name_label, pwd_label, confirm_label;
	
	JButton log_butt, reg_butt, new_reg_butt, cancel_butt;
	JTextField namefield;
	JPasswordField passwordfield, confirmfield;

	public static void main(String [] args){
		JPoker24Game app = new JPoker24Game(args[0]);
		app.go();
	}
	
	// Do the RMI look up and get the stubs
	public JPoker24Game(String host){
		try {
			Registry registry = LocateRegistry.getRegistry(host);			
			server = (GameServer)registry.lookup("Server");
		} catch (Exception e) {
			System.err.println("[User class]Failed accessing RMI: "+e);
			e.printStackTrace();
		}
	}
	
	public void go() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				generateUI();
			}
		});		
	}
	
	
	public void generateUI() {
		frame = new JFrame("Login");
		frame.setSize(200, 250);
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0, 1));
		
		name_label = new JLabel();
		name_label.setText("Login Name");
		pwd_label = new JLabel();
		pwd_label.setText("Password");
		
		log_butt = new JButton("Login");
		reg_butt = new JButton("Register");
		namefield = new JTextField(20);
		passwordfield = new JPasswordField();
		
		log_butt.addActionListener(new LoginListener());
		reg_butt.addActionListener(new RegisterListener());
		
		mainPanel.add(name_label);
		mainPanel.add(namefield);
		mainPanel.add(pwd_label);
		mainPanel.add(passwordfield);
		mainPanel.add(log_butt);
		mainPanel.add(reg_butt);
		
		frame.add(mainPanel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	// Provide login service for user
	private class LoginListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			userName = namefield.getText();
			userPassword = new String(passwordfield.getPassword());
			if(userName.isEmpty()){
				JOptionPane.showMessageDialog(frame, "Login name should not be empty", "Error", JOptionPane.ERROR_MESSAGE);
			}else if(userPassword.isEmpty()){				
				JOptionPane.showMessageDialog(frame, "Login password should not be empty", "Error", JOptionPane.ERROR_MESSAGE);
			}else{
				try {
					// loginService returns true for success
					int result = server.loginService(userName, userPassword);
					
					switch(result){
						case 2: 
							JOptionPane.showMessageDialog(frame, "Login failed! User has alreay logged in!", "Error", JOptionPane.ERROR_MESSAGE);
							break;
						case 1:
							// Game interface is needed to replace original UI
							GameUI game = new GameUI(userName);
							frame.setTitle("JPoker 24-Game");
							frame.setSize(400, 300);
							// user is logging out if close the window
							frame.addWindowListener(new frameListener());

							mainPanel.removeAll();
							mainPanel.add(game.getGuiPanel());
							mainPanel.validate();
							mainPanel.repaint();
							
							frame.pack();
							break;
						case 0:
							JOptionPane.showMessageDialog(frame, "Login failed! Invalid login name!", "Error", JOptionPane.ERROR_MESSAGE);
							break;
						case -1:
							JOptionPane.showMessageDialog(frame, "Login failed! Invalid password!", "Error", JOptionPane.ERROR_MESSAGE);
							break;
						case -2:
							JOptionPane.showMessageDialog(frame, "Login failed! Unknown error!", "Error", JOptionPane.ERROR_MESSAGE);
					}
					
				} catch (RemoteException ex) {
					JOptionPane.showMessageDialog(frame, "Login failed! User RemoteException!", "Error", JOptionPane.ERROR_MESSAGE);
					System.err.println("[User class]LoginService failed: "+ex);
				}
			}
		}
	}
	
	// Create Register interface and provide register service for user
	private class RegisterListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.setTitle("Register");
			frame.setSize(200, 300);
			mainPanel.removeAll();
			
			confirm_label = new JLabel();
			confirm_label.setText("Confirm Password");
			confirmfield = new JPasswordField();
			new_reg_butt = new JButton("Register");
			
			new_reg_butt.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					userName = namefield.getText();
					userPassword = new String(passwordfield.getPassword());
					confirmPassword = new String(confirmfield.getPassword());
					
					if(userName.isEmpty()){
						JOptionPane.showMessageDialog(frame, "Login name should not be empty", "Error", JOptionPane.ERROR_MESSAGE);
					}else if(userPassword.isEmpty()){
						JOptionPane.showMessageDialog(frame, "Login password should not be empty", "Error", JOptionPane.ERROR_MESSAGE);
					}else if(confirmPassword.equals(userPassword)){
							try {
								// registerService returns true for success
								int result = server.registerService(userName, userPassword);
								
								switch(result){
									case 2:
										JOptionPane.showMessageDialog(frame, "User name has been occupied!", "Error", JOptionPane.ERROR_MESSAGE);
										break;
									case 1:
										// Game interface is needed to replace original UI
										GameUI game = new GameUI(userName);
										frame.setTitle("JPoker 24-Game");
										frame.setSize(400, 300);

										mainPanel.removeAll();
										mainPanel.add(game.getGuiPanel());
										mainPanel.validate();
										mainPanel.repaint();
										
										frame.addWindowListener(new frameListener());// user is logging out if close the window
										frame.pack();
										break;
									case -2:
										JOptionPane.showMessageDialog(frame, "Register failed! Unknown error!", "Error", JOptionPane.ERROR_MESSAGE);
								}
								
							} catch (RemoteException ex) {
								System.err.println("[User class]LoginService failed: "+ex);
							}
					}else{
							JOptionPane.showMessageDialog(frame, "Confirm password does not match password!", "Error", JOptionPane.ERROR_MESSAGE);
					}						
				}
				}
			);
			
			cancel_butt = new JButton("Cancel");
			cancel_butt.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// If click cancel, user will go back to the login screen
					frame.setTitle("Login");
					frame.setSize(200, 250);
					mainPanel.removeAll();
					
					mainPanel.add(name_label);
					mainPanel.add(namefield);
					mainPanel.add(pwd_label);
					mainPanel.add(passwordfield);
					mainPanel.add(log_butt);
					mainPanel.add(reg_butt);
					
					mainPanel.validate();
					frame.pack();
					mainPanel.repaint();
				}
			});
			
			mainPanel.add(name_label);
			mainPanel.add(namefield);
			mainPanel.add(pwd_label);
			mainPanel.add(passwordfield);
			mainPanel.add(confirm_label);
			mainPanel.add(confirmfield);
			mainPanel.add(new_reg_butt);
			mainPanel.add(cancel_butt);
			
			mainPanel.validate();
			frame.pack();
			mainPanel.repaint();
		}
	}
	
	private class frameListener implements WindowListener{
		@Override
		public void windowClosing(WindowEvent e) {
			// use userLogoutService
			try {
				//System.out.println("Start to logout in listener: " + userName);
				boolean logout = server.logoutService(userName);
				
				if(logout){
					frame.dispose();
				}else{
					System.out.println("Logout fail!");
				}
				
			} catch (RemoteException e1) {
				System.err.println("[WindowListener]Failed invoking RMI and loging out: "+e1);
			}
		}
		@Override
		public void windowOpened(WindowEvent e) {}
		@Override
		public void windowClosed(WindowEvent e) {}
		@Override
		public void windowIconified(WindowEvent e) {}
		@Override
		public void windowDeiconified(WindowEvent e) {}
		@Override
		public void windowActivated(WindowEvent e) {}
		@Override
		public void windowDeactivated(WindowEvent e) {}
	}
	
	// Help to display the game interface 
	public class GameUI{	
		JPanel mainPanel, menubar, profileArea;
		JButton profile_butt, game_butt, board_butt, logout_butt;
		JLabel label1, label2, label3, label4, label5;
		JTable table;
		private HashMap<Integer, String> profileMap;
		private HashMap<Integer, ArrayList<String>> boardMap;
			
		public GameUI(String name){//////		
			userName = name;
		}
			
		public JPanel getGuiPanel(){
			mainPanel = new JPanel();
			menubar = new JPanel();
			profileArea = new JPanel();
			mainPanel.setPreferredSize(new Dimension(400, 300));
			menubar.setPreferredSize(new Dimension(400, 20));
						
			profile_butt = new JButton("User Profile");
			game_butt = new JButton("Play Game");
			board_butt = new JButton("Leader Board");
			logout_butt = new JButton("Logout");
			
			profile_butt.addActionListener(new ProfileListener());
			game_butt.addActionListener(new GameListener());
			board_butt.addActionListener(new BoardListener());
			logout_butt.addActionListener(new LogoutListener());
					
			menubar.setLayout(new GridLayout(1, 4));
			menubar.add(profile_butt);
			menubar.add(game_butt);
			menubar.add(board_butt);
			menubar.add(logout_butt);
			
			// Message shown on profile page.
			label1 = new JLabel();
			label2 = new JLabel();
			label3 = new JLabel();
			label4 = new JLabel();
			label5 = new JLabel();
	
			try {
				profileMap = server.userProfileService(userName);
				
				label1.setText(profileMap.get(1));
				label1.setFont(new Font("COURIER", Font.BOLD, 30));
				label2.setText("Number of wins: " + profileMap.get(2));
				label3.setText("Number of games: " + profileMap.get(3));
				label4.setText("Average time to win: " + profileMap.get(4));
				label5.setText("Rank: #" + profileMap.get(5));
				label5.setFont(new Font("COURIER", Font.BOLD, 25));
				
			} catch (RemoteException e) {
				System.err.println("[User class]userProfileService failed: "+e);
				e.printStackTrace();
			}			
			
			profileArea.setLayout(new GridLayout(0, 1));
			profileArea.add(label1);
			profileArea.add(label2);
			profileArea.add(label3);
			profileArea.add(label4);
			profileArea.add(label5);	
			mainPanel.setLayout(new BorderLayout());
			mainPanel.add(menubar, BorderLayout.NORTH);
			mainPanel.add(profileArea, BorderLayout.WEST);
						
			return mainPanel;
		}
		
		public class ProfileListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.add(menubar, BorderLayout.NORTH);
				
				// Cannot use previous profileArea directly. Player may improve during the game. 
				try {
					profileMap = server.userProfileService(userName);
					
					label1.setText(profileMap.get(1));
					label2.setText("Number of wins: " + profileMap.get(2));
					label3.setText("Number of games: " + profileMap.get(3));
					label4.setText("Average time to win: " + profileMap.get(4));
					label5.setText("Rank: #" + profileMap.get(5));
					
				} catch (RemoteException ex) {
					System.err.println("[User class]userProfileService failed: "+ex);
					ex.printStackTrace();
				}
				
				mainPanel.add(profileArea, BorderLayout.WEST);
				
				mainPanel.validate();
				mainPanel.repaint();	
			}		
		}
		
		public class GameListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.add(menubar, BorderLayout.NORTH);
	
				mainPanel.validate();
				mainPanel.repaint();
			}		
		}
		
		public class BoardListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.add(menubar, BorderLayout.NORTH);
				
				// Get userLeaderBoardService to display ranking
				try {
					boardMap = server.userLeaderBoardService(userName);
				} catch (RemoteException e1) {
					System.err.println("[User class]userLeaderBoardService failed: "+e1);
					e1.printStackTrace();
				}
				
				// Set up the table by using DefaultTableModel
				Object columnNames[] = {"Rank", "Player", "Games won", "Games played", "Avg.wining time"};
				DefaultTableModel model = new DefaultTableModel(columnNames, 10);	

				int loops = boardMap.size();
				ArrayList<String> maprow = new ArrayList<String>();
				Object[] tablerow;
				
				// Insert rows from boardMap into the table model
				for(int i=1; i<=loops; ++i){
					maprow = boardMap.get(i);
					tablerow = new Object[]{maprow.get(0), maprow.get(1), maprow.get(2), maprow.get(3), maprow.get(4)};
					model.insertRow(i, tablerow);
				}				
				
				table = new JTable(model);
				table.setEnabled(false); // Forbid user to edit the table
				table.setBackground(Color.WHITE);
				
				mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);		
				mainPanel.validate();
				mainPanel.repaint();
			}		
		}
		
		public class LogoutListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//System.out.println("Start to logout in listener: " + userName);
					boolean logout = server.logoutService(userName);
					
					if(logout){
						frame.dispose();
					}else{
						System.out.println("Logout fail!");
					}
					
				} catch (RemoteException e1) {
					System.err.println("[GameUI]Failed invoking RMI and loging out: "+e1);
				}
			}		
		}
	}

		
}