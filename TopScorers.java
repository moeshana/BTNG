package edu.mccc.cos210.ds.fp.bugattitng;


import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class TopScorers {
	private JFrame jf;
	public static String[] list = {"TOP 1 : Shana : 20 : Intimidator : GOLD",
								   "TOP 2 : Shana : 20s : Intimidator : SILVER", 
								   "TOP 3 : Shana : 20s : Intimidator : BRONZE",
								   "TOP 4 : Shana : 20s : Intimidator", 
								   "TOP 5 : Shana : 20s : Intimidator",
								   "TOP 6 : Shana : 20s : Intimidator",
								   "TOP 7 : Shana : 20s : Intimidator", 
								   "TOP 8 : Shana : 20s : Intimidator",
								   "TOP 9 : Shana : 20s : Intimidator", 
								   "TOP 10 : Shana : 20s : Intimidator"};
	public TopScorers() {
		HighScore a = new HighScore();
		list = a.getTop(10);
	}
	public void initSwing() {
		jf = new JFrame("High Score");
		JPanel jp = new JPanel();
		GridLayout gl = new GridLayout(3,1);
		gl.setVgap(0);
		jp.setLayout(gl);
		JLabel jl = new JLabel("Ranking",JLabel.CENTER);
		jl.setFont(new java.awt.Font("Dialog", 1, 25));
		jp.add(jl);
		String tmp = new String("<html>");
		for (int i = 0; i < 5; i++) {
			tmp += ( list[i] + "<br>");
		}
		tmp = tmp + "<html>";
		JLabel jl2 = new JLabel(tmp);
		jl2.setFont(new java.awt.Font("Dialog", 1, 15));
		jp.add(jl2);
		tmp = new String("<html>");
		for (int i = 5; i < 10; i++) {
			tmp += ( list[i] + "<br>");
		}
		tmp = tmp + "<html>";
		JLabel jl3 = new JLabel(tmp);
		jl3.setFont(new java.awt.Font("Dialog", 1, 15));
		jp.add(jl3);
		jf.add(jp);
		jf.setSize(450, 370);
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
//	public static void main(String... args) {
//		TopScorers ts = new TopScorers();
//		EventQueue.invokeLater(() -> ts.initSwing());
//	}
}
