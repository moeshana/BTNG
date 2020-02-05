package edu.mccc.cos210.ds.fp.bugattitng;

public class CarInfoInNet {
	
	private int positionInList;
	private String uniqueID;
	private double x;
	private double y;
	private String BUG;
	private double heading;
	private double speed;
	
//	public static void main(String... args) {
//		CarInfoInNet a = new CarInfoInNet(1,"haha",2,3,"feiji",6,10);
//		String b =  "res : " + a;
//		String d = a.toString();
//		CarInfoInNet c = new CarInfoInNet(d);
//		System.out.println(d);
//	}
//	
	
	
	public CarInfoInNet(int positionInList, String uniqueID, double x, double y, String bUG, double heading, double speed) {
		this.positionInList = positionInList;
		this.uniqueID = uniqueID;
		this.x = x;
		this.y = y;
		this.BUG = bUG;
		this.heading = heading;
		this.speed = speed;
	}
	
	public CarInfoInNet(String a) {
		this.setClass(a);
	}
	
	public String toString() {
		String res = "";
		res = this.positionInList + "-" + this.uniqueID + "-" + this.x + "-" + this.y + "-" + this.BUG 
				+ "-" + this.heading + "-" + this.speed;
		return res;
	}
	public void setClass(String a) {
		String[] res = a.split("-");
		this.positionInList = Integer.valueOf(res[0]);
		this.uniqueID = res[1];
		this.x = Double.valueOf(res[2]);
		this.y = Double.valueOf(res[3]);
		this.BUG = res[4];
		this.heading = Double.valueOf(res[5]);
		this.speed = Double.valueOf(res[6]);
	}
	
	public int getPositionInList() {
		return positionInList;
	}
	public void setPositionInList(int positionInList) {
		this.positionInList = positionInList;
	}
	public String getUniqueID() {
		return uniqueID;
	}
	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public String getBUG() {
		return BUG;
	}
	public void setBUG(String bUG) {
		BUG = bUG;
	}
	public double getHeading() {
		return heading;
	}
	public void setHeading(double heading) {
		this.heading = heading;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	
	
}
