package edu.mccc.cos210.ds.fp.bugattitng;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Server implements ActionListener {
	private JTextField jtf; 
	private JButton jb;
	private JFrame jf;
	private boolean done = false;
	private boolean isServer = false;
	private String Ip;
//	public static void main(String... args) {
//		Server s = new Server();
//		EventQueue.invokeLater(() -> s.initSwing());
//	}
	
	public String getIp() {
		return this.Ip;
	}
	public boolean getServer() {
		return this.isServer;
	}
	public boolean isDone() {
		return this.done;
	}
	public void setDone(boolean a) {
		this.done = a;
	}
	public void setSee() {
		jf.setVisible(true);
	}
	
	public void initSwing() {
		jf = new JFrame("Choose Server!");
		JPanel jp = new JPanel();
		JLabel jl = new JLabel("IP Address: ");
		jp.add(jl);
		jf.add(jp, BorderLayout.CENTER);
		jtf = new JTextField(20);
		jtf.setActionCommand("jtf");
		jtf.addActionListener(this);
		jp.add(jtf);
		jb = new JButton("Use Server");
		jb.setActionCommand("Server");
		jb.addActionListener(this);
		JButton jb1 = new JButton("Use Client");
		jb1.setActionCommand("Client");
		jb1.addActionListener(this);
		jp.add(jb);
		jp.add(jb1);
		jf.setSize(300, 150);
		jf.setResizable(false);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}
	public void setDis() {
		jf.setVisible(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Server") {
			if (jtf.getText().length() > 0 ) {
				this.Ip = jtf.getText();
				this.isServer = true;
				this.done = true;
				this.setDis();
			}
		}
		if (e.getActionCommand() == "Client") {
			if (jtf.getText().length() > 0 ) {
				this.Ip = jtf.getText();
				this.isServer = false;
				this.done = true;
				this.setDis();
			}
		}
	}
}
