package edu.mccc.cos210.ds.fp.bugattitng;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import javax.net.ssl.SNIHostName;
import edu.mccc.cos210.ds.fp.bugattitng.Interfaces.IDrawMap;
import edu.mccc.cos210.ds.fp.bugattitng.Interfaces.IMap;

public class Map implements IMap,java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private final int scaleCount = 18;
	private Shape Map;
	private Point2D[] CheckPoint;
	private Line2D FinishLine = new Line2D.Double(-96.0, 0.0, 96.0, 0.0);
	private Point2D initPoint = new Point2D.Double(0.0,0.0);
	private double initAngel = - Math.PI/2;
	public static void main(String... args) {
		Map a = new Map(true);
	}
	public Map(boolean ownDesign) {
		if (!ownDesign) {
			this.Map = this.initDefaultMap();
		} else {
			try {
				initMap();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void initMap() throws InterruptedException {
		java.util.List<Point2D> Points;
		SetMap sm = new SetMap();
		while (!sm.getSym()) {
			Thread.sleep(10);
		}
		Thread.sleep(2000);

		this.Map = sm.getFpath();            //map
		Points = sm.getPoints();
		calcCheckPoint(Points);              //checkPoint
		this.FinishLine = sm.getFinishLine();
		this.initPoint = new Point2D.Double(sm.getfirstPoint().getX() * scaleCount,
									 		sm.getfirstPoint().getY() * scaleCount);		 								
		initAngel = calcAngel(FinishLine);
		sm.disJF();
		sm = null;
	}
	
	private double calcAngel(Line2D ori) {
		double angel = 0.0;
		double k = (ori.getY2() - ori.getY1()) / (ori.getX2() - ori.getX1());
		double k2 = -1 / k;
		angel = Math.atan(k2);		
		return angel;
	}

	public double getInitAngel() {
		return this.initAngel;
	}
	private void calcCheckPoint(java.util.List<Point2D> Points) {
		CheckPoint = new Point2D[Points.size() - 1];
		for (int i = 0; i < CheckPoint.length; i++) {   // count from index of 1;
			CheckPoint[i] = new Point2D.Double(Points.get(i + 1).getX() * scaleCount,
											   Points.get(i + 1).getY() * scaleCount);
		}
	}
	private Shape initDefaultMap() {
		Path2D path = new Path2D.Double();
		Path2D centerPath = new Path2D.Double();
		Path2D innerPath = new Path2D.Double();
		Path2D outerPath = new Path2D.Double();
		centerPath.moveTo(0.0, 0.0);
		centerPath.lineTo(2.0, 0.0);
		centerPath.quadTo(3.0, 0.0, 3.0, 1.0);
		centerPath.lineTo(3.0, 4.0);
		centerPath.quadTo(3.0, 5.0, 2.0, 5.0);
		centerPath.quadTo(1.0, 5.0, 1.0, 4.0);
		centerPath.lineTo(1.0, 3.0);
		centerPath.quadTo(1.0, 2.0, 0.0, 2.0);
		centerPath.quadTo(-1.0, 2.0, -1.0, 3.0);
		centerPath.lineTo(-1.0, 4.0);
		centerPath.quadTo(-1.0, 5.0, -2.0, 5.0);
		centerPath.quadTo(-3.0, 5.0, -3.0, 4.0);
		centerPath.lineTo(-3.0, 1.0);
		centerPath.quadTo(-3.0, 0.0, -2.0, 0.0);
		centerPath.closePath();
		innerPath.moveTo(0.0, 0.1);
		innerPath.lineTo(2.0, 0.1);
		innerPath.quadTo(2.9, 0.1, 2.9, 1.0);
		innerPath.lineTo(2.9, 4.0);
		innerPath.quadTo(2.9, 4.9, 2.0, 4.9);
		innerPath.quadTo(1.1, 4.9, 1.1, 4.0);
		innerPath.lineTo(1.1, 3.0);
		innerPath.quadTo(1.1, 1.9, 0.0, 1.9);
		innerPath.quadTo(-1.1, 1.9, -1.1, 3.0);
		innerPath.lineTo(-1.1, 4.0);
		innerPath.quadTo(-1.1, 4.9, -2.0, 4.9);
		innerPath.quadTo(-2.9, 4.9, -2.9, 4.0);
		innerPath.lineTo(-2.9, 1.0);
		innerPath.quadTo(-2.9, 0.1, -2.0, 0.1);
		innerPath.closePath();
		outerPath.moveTo(0.0, -0.1);
		outerPath.lineTo(2.0, -0.1);
		outerPath.quadTo(3.1, -0.1, 3.1, 1.0);
		outerPath.lineTo(3.1, 4.0);
		outerPath.quadTo(3.1, 5.1, 2.0, 5.1);
		outerPath.quadTo(0.9, 5.1, 0.9, 4.0);
		outerPath.lineTo(0.9, 3.0);
		outerPath.quadTo(0.9, 2.1, 0.0, 2.1);
		outerPath.quadTo(-0.9, 2.1, -0.9, 3.0);
		outerPath.lineTo(-0.9, 4.0);
		outerPath.quadTo(-0.9, 5.1, -2.0, 5.1);
		outerPath.quadTo(-3.1, 5.1, -3.1, 4.0);
		outerPath.lineTo(-3.1, 1.0);
		outerPath.quadTo(-3.1, -0.1, -2.0, -0.1);
		outerPath.closePath();
		path.append(innerPath, false);
		path.append(outerPath, false);
		path.setWindingRule(Path2D.WIND_EVEN_ODD);
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(90.0));
		at.scale(1024.0, 1024.0);
		path = (Path2D) at.createTransformedShape(path);
		return path;
	}
	@Override
	public Shape getMap() {
		return this.Map;
	}
	public Point2D getInitPoint() {
		return this.initPoint;
	}
	@Override
	public Point2D[] getCheckPoint() {
		return this.CheckPoint;
	}
	@Override
	public Line2D getFinishLine() {
		return this.FinishLine;
	}
}
