import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.*;

import javax.swing.*;

// client's class
/*
  public class GameUI{
 
		protected String userName;
		GameServer server;
		
		JPanel mainPanel, menubar, gamePanel;
		JButton profile_butt, game_butt, board_butt, logout_butt;
		JLabel label1, label2, label3, label4, label5;
		JTable table;
		String[] columnNames={"Rank", "Player", "Games won", "Games played", "Avg.wining time"};
			
	public GameUI(String name, GameServer server){//////
		
		userName = name;
		this.server = server;
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
					e.
				}
				
			} catch (RemoteException e1) {
				System.err.println("[GameUI]Failed invoking RMI and loging out: "+e);
			}
		}		
	}

}

*/