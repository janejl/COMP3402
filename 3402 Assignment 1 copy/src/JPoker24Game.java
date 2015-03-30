import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.*;


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
		frame.setSize(200, 200);
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
	
	private class LoginListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			userName = namefield.getText();
			userPassword = new String(passwordfield.getPassword());
			if(userName.isEmpty()){
				JOptionPane.showMessageDialog(frame, "Login name should not be empty", "Error", JOptionPane.ERROR_MESSAGE);
			}else{
				try {
					// loginService returns true for success
					int result = server.loginService(userName, userPassword);
					
					switch(result){
						case 2: 
							JOptionPane.showMessageDialog(frame, "Login failed! Duplicating login!", "Error", JOptionPane.ERROR_MESSAGE);
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
							break;
						case 0:
							JOptionPane.showMessageDialog(frame, "Login failed! User doesn't exist!", "Error", JOptionPane.ERROR_MESSAGE);
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
	
	private class RegisterListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.setTitle("Register");
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
					frame.dispose();// If click cancel, user UI will shut down
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
			mainPanel.repaint();
		}
	}
	
	// Help to display the game interface 
	public class GameUI{	
		JPanel mainPanel, menubar;
		JButton profile_butt, game_butt, board_butt, logout_butt;
		JLabel label1, label2, label3, label4, label5;
		JTable table;
		String[] columnNames={"Rank", "Player", "Games won", "Games played", "Avg.wining time"};
			
		public GameUI(String name){//////		
			userName = name;
		}
			
		public JPanel getGuiPanel(){
			mainPanel = new JPanel();
			menubar = new JPanel();
			profile_butt = new JButton("User Profile");
			game_butt = new JButton("Play Game");
			board_butt = new JButton("Leader Board");
			logout_butt = new JButton("Logout");
			
			profile_butt.addActionListener(new ProfileListener());
			game_butt.addActionListener(new GameListener());
			board_butt.addActionListener(new BoardListener());
			logout_butt.addActionListener(new LogoutListener());
					
			menubar.setLayout(new GridLayout(1, 4));
			menubar.setSize(new Dimension(400, 10));
			menubar.add(profile_butt);
			menubar.add(game_butt);
			menubar.add(board_butt);
			menubar.add(logout_butt);
			
			// Message shown on profile page.
			label1 = new JLabel();
			label1.setText("User Name");
			label2 = new JLabel();
			label2.setText("Number of wins: 10");
			label3 = new JLabel();
			label3.setText("Number of games: 20");
			label4 = new JLabel();
			label4.setText("Average time to win: 12.5s");
			label5 = new JLabel();
			label5.setText("Rank: #6");
	
			mainPanel.setLayout(new GridLayout(0, 1));
			mainPanel.add(menubar);
			mainPanel.add(label1);
			mainPanel.add(label2);
			mainPanel.add(label3);
			mainPanel.add(label4);
			mainPanel.add(label5);		
						
			return mainPanel;		
		}
		
		public class ProfileListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.add(menubar, BorderLayout.NORTH);
				
				label1.setText("User Name");
				label2.setText("Number of wins: 10");
				label3.setText("Number of games: 20");
				label4.setText("Average time to win: 12.5s");
				label5.setText("Rank: #6");
				
				mainPanel.add(label1);
				mainPanel.add(label2);
				mainPanel.add(label3);
				mainPanel.add(label4);
				mainPanel.add(label5);
				
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
				
				Object[][] data = {{"1", "Player 1", "20", "30", "10"},
									{"2", "Player 2", "19", "30", "10.1"},
									{"3", "Player 3", "18", "30", "10.2"},
									{"4", "Player 4", "17", "30", "10.3"},
									{"5", "Player 5", "16", "30", "10.4"},
									{"6", "Player 6", "15", "30", "10.5"}};
				table = new JTable(data, columnNames);
				
				mainPanel.add(table);
				
				mainPanel.validate();
				mainPanel.repaint();
			}		
		}
		
		public class LogoutListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println("Start to logout in listener: " + userName);
					boolean logout = server.logoutService(userName);
					
					if(logout){
						frame.dispose();
					}else{
						System.out.println("Logout fail!");
					}
					
				} catch (RemoteException e1) {
					System.err.println("[GameUI]Failed invoking RMI and loging out: "+e);
				}
			}		
		}
	}	
}