package edu.mccc.cos210.ds.fp.bugattitng;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SplashScreen implements ActionListener {
	private UserInput ui = null;;
	private Server s = null;
	private TopScorers ts = null;
	private boolean multiPlay = false;
	private boolean isServer = false;
	private boolean ownDesign = false;
	private String Username = "";
	private String IP = "";
	private boolean done = false;
	private JFrame jf;
	
	
//	public static void main(String... args) {
//	SplashScreen a = new SplashScreen();
//	}
	
	public boolean isDone() {
		return this.done;
	}
	public void setDone(boolean a) {
		this.done = a;
	}
	public void prints() {
		System.out.println("server : " + this.isServer);
		System.out.println("ip: " + this.IP);
		System.out.println("do map : " + ownDesign);
		System.out.println("name : " + Username);
		System.out.println("multiplay : " + multiPlay);
	}
	
	public SplashScreen() {
		jf = new JFrame("Bugatti - The Next Generation");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel jp = new JPanel();
		jf.add(jp, BorderLayout.CENTER);
		JLabel label = new JLabel(new ImageIcon("src/edu/mccc/cos210/ds/fp/bugattitng/SplashScreen.png"));
		label.setLayout(new FlowLayout());
		JButton jb = new JButton("Single Player");
		jb.setActionCommand("Single");
		jb.addActionListener(this);
		JButton jb2 = new JButton("Multi Player");
		jb2.setActionCommand("Multi");
		jb2.addActionListener(this);
		JButton jb3 = new JButton("High Score");
		jb3.setActionCommand("High");
		jb3.addActionListener(this);
		JButton jb4 = new JButton("Exit");
		jb4.addActionListener(e -> System.exit(0));
		jf.add(label);
		label.add(jb);
		label.add(jb2);
		label.add(jb3);
		label.add(jb4);
		jf.setSize(770, 440);
		jf.setResizable(false);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		
		javax.swing.Timer t = new javax.swing.Timer(
				33,
				ae -> {
					if (ui != null) {
						if (ui.isDone()) {
							this.Username = ui.getUsername();
							this.ownDesign = ui.getDesign();
							ui.setDone(false);
//							this.prints();
							ui = null;
							this.done = true;						
						}
					}
					if (s != null) {
						if (s.isDone()) {
							this.IP = s.getIp();
							this.isServer = s.getServer();
							this.multiPlay = true;
							s.setDone(false);
							s = null;
							ui = new UserInput();
							ui.initSwing(this.isServer);
						}
					}
				});
				t.start();
	}
	public String getName() {
		return this.Username;
	}
	public boolean getMultiPlay() {
		return this.multiPlay;
	}
	public boolean getServer() {
		return this.isServer;
	}
	public boolean ownDesign() {
		return this.ownDesign;
	}
	public String getIp() {
		return this.IP;
	}
	public void disJf() {
		this.jf.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Single") {
			if (s != null) {
				s.setDis();
				s = null;
			} else {
				if (ts != null) {
					ts.setDis();
					ts = null;
				}
			} 
			if (ui == null) {
				ui = new UserInput();
				ui.initSwing(true);
				this.multiPlay = false;
			} else {
				ui.setSee();
			}
		} 
		if(e.getActionCommand() == "Multi") {
			if (ui != null) {
				ui.setDis();
				ui = null;
			} else {
				if (ts != null) {
					ts.setDis();
					ts = null;
				}
			}
			if (s == null) {
				s = new Server();
				s.initSwing();
				this.multiPlay = true;
			} else {
				s.setSee();
			}
		}
		if (e.getActionCommand() == "High") {
			if (ui != null) {
				ui.setDis();
				ui = null;
			} else {
				if (s != null) {
					s.setDis();
					s = null;
				}
			}
			if (ts == null) {
				ts = new TopScorers();
				ts.initSwing();
			} else {
				ts.setSee();
			}
		}
	}
}
