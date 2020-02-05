package edu.mccc.cos210.ds.fp.bugattitng;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class MyCar extends Car{
	
//	private int x;
//	private int y;
//	private String BugName;
//	private BufferedImage Bug;
//	private double HeadingInRadius;
////	private String ID;
//	private ArrayList<Rectangle> Attack = new ArrayList<Rectangle>(2);
//	private ArrayList<Rectangle> Pattack = new ArrayList<Rectangle>(8);

	private int currentSpeed = 0;
	public static double[] ON_TRACK_SPEED;
	private int MAX_GEAR;
	private final Ellipse2D marker = new Ellipse2D.Double(-16.0, -16.0, 32.0, 32.0);
	private Point2D currentPosition;
	private int currentGear = 0;
	private boolean racing = false;
	
	
	public int getMaxGear() {
		return this.MAX_GEAR;
	}
	public int getCurrentGear() {
		return this.currentGear;
	}
	public void setCurrentGear(int news) {
		if (currentGear != news) {
			System.out.println("Gear Change: " + (news + 1));
			this.currentGear = news;
		}
	}
	public void setCurrentSpeed(int news) {
		this.currentSpeed = news;
		this.speed = this.currentSpeed * 500;
	}
	public int getCurrentSpeed() {
		return this.currentSpeed;
	}
	public boolean getRacing() {
		return this.racing;
	}
	public void setRacing(boolean news) {
		this.racing = news;
	}
	public static void main(String... args) {
		MyCar a = new MyCar(0,0,0,"Intimidator",2.0);
//		System.out.println(a.details == null);
		System.out.println(a.getID());
	}
	
	public MyCar(double x, double y, int speed, String BugName, double HeadingInRadius) {
		super(x, y, speed, BugName, HeadingInRadius);
		ON_TRACK_SPEED = getSpeeds(BugName,details);
		MAX_GEAR = ON_TRACK_SPEED.length - 1;
		currentPosition = new Point2D.Double(x,y);
	}
	public Point2D getCurrentPosition() {
		return this.currentPosition;
	}
	
	public void setBugName(String news) {
		super.setBugName(news);
		ON_TRACK_SPEED = getSpeeds(BugName,details);
		MAX_GEAR = ON_TRACK_SPEED.length - 1;
	}
	
	public void setCurrentPosition(Point2D news) {
		this.setX(news.getX());
		this.setY(news.getY());
		this.currentPosition = news;
		updataRect();
	}
	
	public double[] getSpeeds(String bug,Detail[] details) {
		double[] speeds = new double[] { 8.0, 12.0, 16.0, 24.0, 32.0 };
		for (Detail d : details) {
//			System.out.println(d);
			if (d.getBug().equals(BugName)) {
				speeds = d.getSpeeds();
				break;
			}
		}
		return speeds;
	}
	private class Detail {
		private String detailbug;
		private double[] detailspeeds;
		public Detail(String bug, double[] speeds) {
			this.detailbug = bug;
			this.detailspeeds = speeds;
		}
		public String getBug() {
			return this.detailbug;
		}
		public double[] getSpeeds() {
			return this.detailspeeds;
		}
	}
	public Detail[] details = new Detail[] {
			new Detail("Zubenelgenubi", new double[] { 8.0, 12.0, 24.0, 32.0, 48.0, 64.0, 80.0, }), 
			new Detail("FlamingBurrito", new double[] { 8.0, 16.0, 32.0, 48.0, 64.0, }),               
			new Detail("AngryPorkChop", new double[] { 8.0, 16.0, 32.0, 48.0, 64.0, }),               
			new Detail("ViralInfection", new double[] { 8.0, 16.0, 32.0, 48.0, 64.0, }),             
			new Detail("ScreamingBanshee", new double[] { 8.0, 12.0, 20.0, 32.0, 48.0, }),              
			new Detail("LethalInjection", new double[] { 8.0, 12.0, 20.0, 32.0, 48.0, }),             
			new Detail("BadMedicine", new double[] { 8.0, 12.0, 20.0, 32.0, 48.0, }),             
			new Detail("RedSquirrel", new double[] { 8.0, 12.0, 16.0, 24.0, 32.0, }),         
			new Detail("YellowSquirrel", new double[] { 8.0, 12.0, 16.0, 24.0, 32.0, }),            
			new Detail("RustySpoon", new double[] { 8.0, 12.0, 16.0, 24.0, 32.0, }),             
			new Detail("MommasBadBoy", new double[] { 8.0, 12.0, 16.0, 24.0, 32.0, }),             
			new Detail("PowerTool", new double[] { 8.0, 16.0, 24.0, 32.0, }),               
			new Detail("UnluckyBandit", new double[] { 8.0, 16.0, 24.0, 32.0, }),               
			new Detail("Intimidator", new double[] { 8.0, 12.0, 16.0, 32.0, }),                
			new Detail("WidowMaker", new double[] { 8.0, 12.0, 16.0, 32.0, }),                
			new Detail("SilentNinja", new double[] { 8.0, 16.0, 32.0, }),                 
			new Detail("Undertaker", new double[] { 8.0, 16.0, 32.0, }),                 
			new Detail("MisterLucifer", new double[] { 8.0, 16.0, 32.0, }),                 
			new Detail("Beelzebub", new double[] { 8.0, 12.0, 24.0, }),                 
			new Detail("Apocalypse", new double[] { 8.0, 12.0, 24.0, }),                 
			new Detail("LittleWillie", new double[] { 8.0, 16.0, }), 
	};
	
}
