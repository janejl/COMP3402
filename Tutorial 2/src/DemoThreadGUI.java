import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class DemoThreadGUI {
	public static int count = 0;

	public static void main(String [] args) {
		DemoThreadGUI app = new DemoThreadGUI();
		app.go();
	}

	public void go() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				generateGUI();
			}
		});		
	}
	public void generateGUI() {
		JFrame frame = new JFrame("Demo");
		frame.add(new MyPanel());
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	@SuppressWarnings("serial")
	class MyPanel extends JPanel implements MouseListener {
		private int x,y;
		private int r;
		public MyPanel() {
			this.setPreferredSize(new Dimension(100,100));
			this.addMouseListener(this);
			x = 50;
			y = 50;
			r = 25;
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.fillOval(x-r,y-r,r*2,r*2);
			++count;
			System.out.println("paintComponent: " + count + "/n");
		}
			
		public void mouseClicked(MouseEvent event) {
			new Animation(this, event).start();
			/*
			int targetX = event.getX();
			int targetY = event.getY();
			for(int i=0;i<10;++i) {
				x = (x+targetX)/2;
				y = (y+targetY)/2;
				repaint();
				try {
			
			       Thread.sleep(100);
				} catch (InterruptedException e) {}
			}*/
		}
		
		public void mouseEntered(MouseEvent event) {}
		public void mouseExited(MouseEvent event) {}
		public void mousePressed(MouseEvent event) {}
		public void mouseReleased(MouseEvent event) {}
		
	}

	
	class Animation extends Thread {
	    /* ... */
		private MyPanel panel;
		private MouseEvent event;
		
	    public Animation(MyPanel p, MouseEvent e) {
			// TODO Auto-generated constructor stub
	    	panel = p;
	    	event = e;
		}
		public void run() {
	            int targetX = event.getX();
	            int targetY = event.getY();
	            for(int i=0;i<10;++i) {
	                   panel.x = (panel.x+targetX)/2;
	                   panel.y = (panel.y+targetY)/2;
	                   panel.repaint();
	                   try {
	                	   Thread.sleep(100);
	                   }catch(InterruptedException e){}
	            }
	    }
	}
}



