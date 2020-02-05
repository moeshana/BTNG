package edu.mccc.cos210.ds.fp.bugattitng;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import edu.mccc.cos210.ds.fp.bugattitng.Interfaces.ICar;

public class Car implements ICar{
	protected double x;
	protected double y;
	protected String BugName;
	protected BufferedImage Bug = null;
	protected double HeadingInRadius;
	protected String ID;
	protected ArrayList<Rectangle> Attack = new ArrayList<Rectangle>(2);
	protected ArrayList<Rectangle> Pattack = new ArrayList<Rectangle>(8);
	protected int speed = 0;
	
	

	public Car(double x, double y, int speed, String BugName, double HeadingInRadius) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.HeadingInRadius = HeadingInRadius;
		this.BugName = BugName;
		this.Bug = setBug();
		this.ID = autoSetID();
		for (int i = 0; i < 8; i++) {
			Pattack.add(new Rectangle());
		}
		for (int i = 0; i < 2; i++) {
			Attack.add(new Rectangle());
		}
		this.updataRect();
	}
	
	public void setBugName(String news) {
		this.BugName = news;
		this.Bug = setBug();
	}
	public BufferedImage setBug() {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File("./images/bugatti/" + BugName +".png"));
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
		return bi;
	}
	private String autoSetID() {
		String ID = "Shana_"  + BugName + "_" +  (int)(Math.random() * 300000);
		return ID;
	}

	@Override
	public double getX() {
		return this.x;
	}
	public void setX(double x) {
		this.x = x;
	}
	@Override
	public double getY() {
		return this.y;
	}
	public void setY(double y) {
		this.y = y;
	}
	@Override
	public String getBugName() {
		return this.BugName;
	}

	@Override
	public BufferedImage getBug() {
		return this.Bug;
	}

	@Override
	public double getHeadingInRadius() {
		return this.HeadingInRadius;
	}
	public void setHeadingInRadius(double a) {
		this.HeadingInRadius = a;
	}
	@Override
	public String getID() {
		return this.ID;
	}

	@Override
	public ArrayList<Rectangle> getAttack() {
		return this.Attack;
	}

	@Override
	public ArrayList<Rectangle> getPattack() {
		return this.Pattack;
	}

	@Override
	public void dealwithCollision(int index, boolean isAttack) {
		switch (index) {
			case 0:
				if (isAttack) {
					this.goBack(3);
				} else {
					this.goBack(3);
				}
				break;
			case 1:
				if (isAttack) {
					this.goFront(3);
				} else {
					this.goFront(3);
				}
				break;
			case 2:
				this.goRight(2);
				this.goBack(2);
				break;
			case 3:
				this.goLeft(2);
				this.goBack(2);
				break;
			case 4:
				this.goRight(3);
				break;
			case 5:
				this.goLeft(3);
				break;
			case 6:			
				this.goRight(2);
				this.goFront(2);
				break;
			case 7:	
				this.goLeft(2);
				this.goFront(2);
				break;
		}	
	}
	public void goFront() {
		this.goFront(2);
	}
	public void goBack() {
	this.goBack(1);
	}
	public void goLeft() {
		this.goLeft(1);
	}
	public void goRight() {
		this.goRight(1);
	}
	
	protected void updataRect() {
		Rectangle carleft1 = new Rectangle((int)this.x-42,(int)this.y-91,15,45);
		Rectangle carright1 = new Rectangle((int)this.x+27,(int)this.y-91,15,45);
		
		Rectangle carleft2 = new Rectangle((int)this.x-42,(int)this.y-46,15,92);
		Rectangle carright2 = new Rectangle((int)this.x+27,(int)this.y-46,15,92);
		
		Rectangle carleft3 = new Rectangle((int)this.x-42,(int)this.y+46,15,49);
		Rectangle carright3 = new Rectangle((int)this.x+27,(int)this.y+46,15,49);
		
		Rectangle cartop1 = new Rectangle((int)this.x-27,(int)this.y-101,54,15);
		Rectangle carback1 = new Rectangle((int)this.x-27,(int)this.y+86,54,15);
		
		Rectangle cartopa = new Rectangle((int)this.x-42,(int)this.y-100,85,15);
		Rectangle carbacka = new Rectangle((int)this.x-42,(int)this.y+86,90,15);
		
		Pattack.set(2,carleft1);
		Pattack.set(3,carright1);
		Pattack.set(4,carleft2);
		Pattack.set(5,carright2);
		Pattack.set(6,carleft3);
		Pattack.set(7,carright3);
		Pattack.set(0,cartop1);
		Pattack.set(1,carback1);
		Attack.set(0,cartopa);
		Attack.set(1,carbacka);
	}
	
	public void goFront(int n) {
		this.y -= speed * n;
		this.updataRect();
	}

	public void goBack(int n) {
		this.y += speed * n;
		this.updataRect();
	}
	public void goLeft(int n) {
		this.x -= speed * n;
		this.updataRect();
	}
	public void goRight(int n) {
		this.x += speed * n;
		this.updataRect();
	}
	
}
