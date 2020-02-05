package edu.mccc.cos210.ds.fp.bugattitng;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import javax.sound.midi.Sequencer;
import javax.swing.JFrame;
import javax.swing.JPanel;
//import edu.mccc.cos210.bugatti.util.MidiPlayer;



public class Bugatti5 implements Runnable {
	private String playerName;
	private static final double OUT_OF_BOUNDS_SPEED = 4.0;
	private static final double TURN_RATE_IN_RADIANS = Math.toRadians(120.0);
	private int MAX_PLAYER = 4;
	private final Ellipse2D marker = new Ellipse2D.Double(-16.0, -16.0, 32.0, 32.0);
	private double turnRateInRadians =  0.0;
	private boolean outOfBounds = true;
	private int server_port = 5972;
	private String networkKey;
	private long startTime = 0;
	private int nextCheckpoint = 0;
	private Ellipse2D[] wrightPoints = new Ellipse2D[] {
		new Ellipse2D.Double(-5245.0, 1923.0, 250.0, 250.0),
		new Ellipse2D.Double(-2173.0, -125.0, 250.0, 250.0),
		new Ellipse2D.Double(-5245.0, -2173.0, 250.0, 250.0),
		new Ellipse2D.Double(-125.0, 0.0, 250.0, 250.0),
	};
	
	private boolean multiPlay = false;
	private boolean gameOver = false;
	private int countDownNum = 10;
	private boolean startCount = false;
	Line2D finishLine;
	private Map myMap;
	private Shape track;
	private MyCar myCar = null;
	private String BUG = "";            
	private ShowRoom sr;
	private boolean ownDesign = true;
	public Car[] other = new Car[1];
	private double finalTime = 0.0;
	private static Sequencer sequencer;
	private MyJPanel jp;
	private int networkProcess;
	private boolean isServer;
	private String ipAddress;
	private Network nw;
	private CarInfoInNet myCarInfo;
	private HighScore hs;
	
	public Network getNet() {
		return this.nw;
	}
	public boolean getIsServer() {
		return this.isServer;
	}
	public boolean getMulti() {
		return this.multiPlay;
	}
	public int getPort() {
		return this.server_port;
	}
	public String getIp() {
		return this.ipAddress;
	}
	
	public Bugatti5(String name, boolean ownDesign, boolean multi, boolean server, String ip) throws IOException {
		this.ownDesign = ownDesign;
		this.playerName = name;
		this.multiPlay = multi;
		this.hs = new HighScore();
		if (multiPlay) {
			this.isServer = server;
			String[] tmp = ip.split(":");
			this.ipAddress = tmp[0];
			this.server_port = Integer.valueOf(tmp[1]);
			this.nw = new Network(this.getIp(), this.getPort(), this.getIsServer(),1);
			if(this.isServer) {
				initMap();
				initMyCar();
				this.initSwing();
			} else {
				this.SelectCar();
				this.initSwing();
			}
		} else {
			initMap();
			initMyCar();
		}
	
		
	}
	private void drawCar(int pos) {
		if (!BUG.equals("") && myCar == null) {
			myCar = new MyCar(myMap.getInitPoint().getX(),myMap.getInitPoint().getY() + (pos + 1) * 350 ,10,BUG,myMap.getInitAngel() + Math.PI);
			BUG = "";
		}
	}
	private void SelectCar() throws IOException{
		sr = new ShowRoom();
		System.out.println("haha");
		sr.showJf();
		while (sr.tmps == "") {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		BUG = sr.tmps;
		sr.disJf();
		sr = null;
	}
	private void initMyCar() throws IOException {
		if (!this.multiPlay) {
			this.SelectCar();
			this.drawCar(-1);
		} else {   
			this.SelectCar();
			this.drawCar(-1);

		}
	}
	private void drawMap() {
		if (myMap.getCheckPoint() != null) {
			Point2D[] ckp = myMap.getCheckPoint();
			wrightPoints = new Ellipse2D[ckp.length];
			for (int i = 0; i< wrightPoints.length; i++) {
				wrightPoints[i] = new Ellipse2D.Double(ckp[i].getX()-250,ckp[i].getY()-250, 500.0, 500.0);
				System.out.println(wrightPoints[i].getCenterX() + " : " + wrightPoints[i].getCenterY());
			}
			wrightPoints[wrightPoints.length-1] = new Ellipse2D.Double(ckp[wrightPoints.length-1].getX()- 250,
													ckp[wrightPoints.length-1].getY()-250, 500.0, 500.0);

		}
		finishLine = myMap.getFinishLine();
		track = myMap.getMap();
	}
	private void initMap() {
		if (!this.multiPlay) {
			myMap = new Map(ownDesign);
			this.drawMap();
		} else {  
			if (this.isServer) {
				myMap = new Map(ownDesign);
				this.drawMap();
			} else {
				this.drawMap();
			}
		}
		
	}
	
	
	public void initSwing() { 
		JFrame jf = new JFrame("BugattiTNG");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jp = new MyJPanel();
		jf.add(jp, BorderLayout.CENTER);
		jf.setSize(1024, 768);
		jf.setResizable(false);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		
		
		javax.swing.Timer t = new javax.swing.Timer(
			33,
			ae -> {
				if (multiPlay) {
					if (myCar != null) {
						this.nw.setMyCar(myCar);
					}
					this.networkProcess = this.nw.getProcess();
					if (this.networkProcess < 61 ) {
						this.nw.setBug(BUG);
					}
					if (this.networkProcess == 80  && !this.isServer) {
						this.myMap = this.nw.getMap();
						int pos = this.nw.getInitPos(); 
						this.initMap();
						this.drawCar(pos);
						this.nw.setMyCar(myCar);
						jp.repaint();
					}
					if (this.networkProcess >= 80) {
						boolean found = false;
						String tmp = this.nw.getBacktoClient();
						if (tmp != null) {
							String[] a = tmp.split("=");
							for (String sc : a) {
								String[] datas = sc.split("\\+\\+");
								if (datas[0].equals(myCar.getID())) {
									continue;
								}
								if (this.other[0] == null) {
									this.other[0] = new Car(Double.valueOf(datas[1]),
											Double.valueOf(datas[2]),Integer.valueOf(datas[3]),
											   datas[4],Double.valueOf(datas[5]));
									jp.repaint();
									continue;
								}  
								
								for (int n = 0 ; n < a.length - 1; n++) {
									if (this.other[n].getID().equals(datas[0])) {
										this.other[n] = new Car(Double.valueOf(datas[1]),
												   Double.valueOf(datas[2]),Integer.valueOf(datas[3]),
												   datas[4],Double.valueOf(datas[5]));
									}									
									jp.repaint();			
								}
							}
						}
						jp.repaint();
					}
				}
				if ((!multiPlay || this.networkProcess > 70 || this.isServer) && (myCar != null)) {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				myCar.setHeadingInRadius(myCar.getHeadingInRadius()+Math.toRadians(turnRateInRadians));
				myCar.setCurrentPosition(new Point2D.Double(myCar.getCurrentPosition().getX() + Math.cos(myCar.getHeadingInRadius()) * myCar.getCurrentSpeed(),
															myCar.getCurrentPosition().getY() + Math.sin(myCar.getHeadingInRadius()) * myCar.getCurrentSpeed()));
				
				if (countDownNum < 1) {
					gameOver = true;
				}
				if (wrightPoints[nextCheckpoint].contains(myCar.getCurrentPosition())) {
					nextCheckpoint++;
					if (nextCheckpoint == wrightPoints.length) {
						myCar.setRacing(false);
						myCar.setCurrentSpeed(0);
						nextCheckpoint = 0;
						this.finalTime = (System.currentTimeMillis() - startTime) / 1000.0;
						System.out.printf("Finish!\n%.2f seconds\n", finalTime);
						String res = this.playerName + "  :  " + this.finalTime + "  :  " + myCar.getBugName();
						this.hs.saveResult(res);
						if (!multiPlay) {
							gameOver = true;
							jp.repaint();
						} else {   // for network
							int tmp = countDownNum;
							new Thread(new Runnable(){
								public void run(){
									startCount = true;
									for( int i=0; i<tmp; i++ ){

										countDownNum--; 
										jp.repaint();
						                try{
						                	Thread.sleep(500);
						                }
						                catch(Exception ex){
						                }
						             }
						          }
							 }).start();
							jp.repaint();
						}
					} else {
						System.out.println("Checkpoint " + nextCheckpoint);
					}
				}
				if (!track.contains(myCar.getCurrentPosition())) {
					if (!outOfBounds) {
						System.out.println("Out of Bounds!");
					}
					outOfBounds = true;
					if (myCar.getRacing()) {
						myCar.setCurrentSpeed((int)OUT_OF_BOUNDS_SPEED);
						if (myCar.getCurrentGear() != 0) {
							myCar.setCurrentGear(0);
						}
					}
				} else {
					if (outOfBounds) {
						System.out.println("On Track.");
					}
					outOfBounds = false;
				}
				jp.repaint();
			}  // if >70
		}
		);
		t.start();
		jf.addKeyListener(
			new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent ke) {
					if (ke.getKeyCode() == KeyEvent.VK_LEFT || ke.getKeyCode() == KeyEvent.VK_RIGHT) {
						turnRateInRadians = 0.0;
					}
				}
				@Override
				public void keyPressed(KeyEvent ke) {
					if (ke.getKeyCode() == KeyEvent.VK_UP) {
						if (myCar.getRacing()) {
							if (myCar.getCurrentSpeed() != 0.0) {
								myCar.setCurrentGear(myCar.getCurrentGear() + 1 > myCar.getMaxGear() ? myCar.getMaxGear() : myCar.getCurrentGear() + 1);
							}
							if (outOfBounds) {
								myCar.setCurrentSpeed((int)OUT_OF_BOUNDS_SPEED);
								myCar.setCurrentGear(0);
							} else {
								myCar.setCurrentSpeed((int)MyCar.ON_TRACK_SPEED[myCar.getCurrentGear()]);
							}
						} else {
							myCar.setCurrentGear(0);
							myCar.setCurrentSpeed((int)MyCar.ON_TRACK_SPEED[0]);
							System.out.println("Go!");
							myCar.setRacing(true);
							startTime = System.currentTimeMillis();
						}
					}
					if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
						if (myCar.getRacing()) {
							myCar.setCurrentGear(myCar.getCurrentGear() - 1 < 0 ? 0 : myCar.getCurrentGear() - 1);
							if (outOfBounds) {
								myCar.setCurrentSpeed((int)OUT_OF_BOUNDS_SPEED);
								myCar.setCurrentGear(0);
							} else {
								myCar.setCurrentSpeed((int)MyCar.ON_TRACK_SPEED[myCar.getCurrentGear()]);
							}
						}
					}
					if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
						if (myCar.getCurrentSpeed() != 0.0) { 
							turnRateInRadians = TURN_RATE_IN_RADIANS;
						}
					}
					if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
						if (myCar.getCurrentSpeed() != 0.0) {
							turnRateInRadians = -TURN_RATE_IN_RADIANS;
						}
					}
					if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
						System.out.println("Reset!");
						myCar.setCurrentSpeed(0);
						myCar.setCurrentPosition(new Point2D.Double(myMap.getInitPoint().getX(),myMap.getInitPoint().getY()));
						myCar.setHeadingInRadius(myMap.getInitAngel() + Math.PI);
						outOfBounds = true;
						myCar.setRacing(false);
						turnRateInRadians = 0.0;
						myCar.setCurrentGear(0);
						nextCheckpoint = 0;
					}

					if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
						try {
							sr.showJf();	
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		);
	}

	public Shape getTrack() {
		return this.track;
	}

	private class MyJPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final Stroke trackStroke = new BasicStroke(7.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] {83.0f, 83.0f}, 0.0f);
		private final Stroke finishLineStroke = new BasicStroke(17.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] {14.75f, 14.25f}, 0.0f);
		private final Stroke markerStroke = new BasicStroke(5.0f);
		
		
		public MyJPanel() {

			setBackground(new Color(0, 120, 64));
		}
	
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (!gameOver && (networkProcess > 70 || isServer || !multiPlay)) {
				Graphics2D g2d = (Graphics2D) g.create();
				AffineTransform gat = new AffineTransform();
				gat.translate(getWidth() / 2.0, getHeight() - getHeight() / 4.0);
				gat.scale(1.0, -1.0);
				g2d.transform(gat);
				
				Graphics2D g2d2 = (Graphics2D) g.create();
				g2d2.transform(gat);
				
				AffineTransform tat = new AffineTransform();

				tat.rotate(-(myCar.getHeadingInRadius() - Math.PI / 2.0));
				tat.translate(-myCar.getCurrentPosition().getX(), -myCar.getCurrentPosition().getY());
				Shape t = tat.createTransformedShape(track);
				g2d2.transform(tat);

				g2d.setPaint(Color.BLACK);
				g2d.setStroke(trackStroke);
				g2d.fill(t);
				g2d.setPaint(Color.YELLOW);
				g2d.draw(t);

				Shape fl = tat.createTransformedShape(finishLine);
				g2d.setPaint(Color.YELLOW);
				g2d.setStroke(finishLineStroke);
				g2d.draw(fl);
				
				Shape f3 = tat.createTransformedShape(marker);
				g2d.setPaint(Color.RED);
				g2d.setStroke(markerStroke);

				if(startCount) {
					Font myFont2=new Font("Calibri",Font.BOLD,38);
					g.setFont(myFont2);
					g.drawString((String.valueOf(countDownNum) + "s") ,250,250);
				}
				
				if (multiPlay) {
					if (other[0] != null) {
						Graphics2D g2dBug2 = (Graphics2D) g2d2.create();				
						for (Car a : other) {
							drawOther(g2dBug2,a);
						}
						g2dBug2.dispose();
						g2d2.dispose();
					}
				}

				Graphics2D g2dBug = (Graphics2D) g2d.create();
				drawBug(g2dBug);
				g2dBug.dispose();
				g2d.dispose();
	
			} else {
				if (gameOver) {
					System.out.println("Game over");
					setBackground(new Color(254, 120, 64, 23));
					Graphics2D g2d = (Graphics2D) g.create();
					AffineTransform gat = new AffineTransform();
					gat.translate(getWidth() / 2.0, getHeight() - getHeight() / 4.0);
					gat.scale(1.0, -1.0);
					g2d.transform(gat);
					Font myFont=new Font("Calibri",Font.BOLD,38);
					g.setFont(myFont);
					g.drawString("Game Over" ,250,250);
					g.drawString((String.valueOf(finalTime) + "s"), 500, 250);
				}
			}
		}
	}
	private void drawOther(Graphics2D g2d, Car other) {
		AffineTransform at = new AffineTransform();
		at.translate(other.getBug().getWidth() / 8.0, other.getBug().getHeight() / 8.0);
		at.rotate(Math.PI);
		at.rotate(other.getHeadingInRadius() - Math.PI / 2.0);
		at.scale(0.25, 0.25);
		g2d.drawRenderedImage(other.getBug(), at);
	}
	
	private void drawBug(Graphics2D g2d) {
		AffineTransform at = new AffineTransform();
		at.translate(myCar.getBug().getWidth() / 8.0, myCar.getBug().getHeight() / 8.0);
		at.rotate(Math.PI);
		at.scale(0.25, 0.25);
		g2d.drawRenderedImage(myCar.getBug(), at);
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(10);
				if ((this.networkProcess >= 80 || this.isServer) && other[other.length - 1] != null) {
				for (int a = 0; a < other.length; a++) {
					for (int u = a + 1; u < other.length; u++) {
						for (int i = 0; i < other[u].getPattack().size(); i++) {
							Car otherCar1 = other[a];  //ATTACK
							Car otherCar2 = other[u];  //PATTACK
							for (int n = 0; n < otherCar1.getAttack().size(); n++) {
								for (int m = 0; m < myCar.getPattack().size(); m++) {
									if (otherCar1.getAttack().get(n).intersects(otherCar2.getPattack().get(i))) {
										otherCar1.dealwithCollision(n,true);
										myCar.dealwithCollision(m, false);
									}
								}
								if (otherCar1.getAttack().get(n).intersects(otherCar2.getPattack().get(i))) {
									otherCar1.dealwithCollision(n,true);
									otherCar2.dealwithCollision(i,false);
								}
							}
						}
					}
				}
				for (int a = 0; a < other.length; a++) {
					Car otherCar = other[a];
					for (int n = 0; n < myCar.getAttack().size(); n++) {
						for (int i = 0; i < otherCar.getPattack().size(); i++) {
							if (myCar.getAttack().get(n).intersects(otherCar.getPattack().get(i))) {							
								otherCar.dealwithCollision(i,false);
								myCar.dealwithCollision(n,true);
							}
						}
					}
				}
			}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
