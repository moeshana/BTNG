package edu.mccc.cos210.ds.fp.bugattitng;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UserInput implements ActionListener {
	private JTextField jtf; 
	private JButton jb;
	private JButton jb1;
	private JButton jb2;
	private JFrame jf;
	private String username;
	private boolean ownDesign;
	private boolean done = false;
	
	public void setDone(boolean a) {
		this.done = a; 
	}
	public String getUsername() {
		return this.username;
	}
	public boolean getDesign() {
		return this.ownDesign;
	}
	public boolean isDone() {
		return this.done;
	}
	public void initSwing(boolean isServer) {
		jf = new JFrame("Single Player");
		JPanel jp = new JPanel();
		JLabel jl = new JLabel("Enter your name:  ");
		jp.add(jl);
		jf.add(jp, BorderLayout.CENTER);
		jtf = new JTextField(20);
		jtf.setActionCommand("jtf");
		jtf.addActionListener(this);
		jp.add(jtf);
		if (isServer) {
			jb = new JButton("Create Map");
			jb.setActionCommand("cm");
			jb.addActionListener(this);
			jb1 = new JButton("Default Map");
			jb1.setActionCommand("dm");
			jb1.addActionListener(this);
			jp.add(jb);
			jp.add(jb1);
		} else {
			jb2 = new JButton("Ok");
			jb2.setActionCommand("Ok");
			jb2.addActionListener(this);
			jp.add(jb2);
		}		


		jf.setSize(300, 125);
		jf.setResizable(false);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}
	public void setDis() {
		jf.setVisible(false);
	}
	public void setSee() {
		jf.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "cm") {
			if (jtf.getText().length() > 0 ) {
				this.username = jtf.getText();
				this.ownDesign = true;
				this.done = true;
				this.setDis();
			}
		}
		if (e.getActionCommand() == "dm") {
			if (jtf.getText().length() > 0) {
				this.username = jtf.getText();
				this.ownDesign = false;
				this.done = true;
				this.setDis();
			}
		}
		if (e.getActionCommand() == "Ok") {
			if (jtf.getText().length() > 0) {
				this.username = jtf.getText();
				this.done = true;
				this.setDis();
			}
		}
	}
}
