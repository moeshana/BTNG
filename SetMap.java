package edu.mccc.cos210.ds.fp.bugattitng;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.event.MouseInputAdapter;
import edu.mccc.cos210.ds.fp.bugattitng.Interfaces.ISetMap;

//BUG : if u choose quadTo but not finish then choose lineTo.. it will raise a exception.  !done
//		cancel button, index -1 error when u always click it   !done
//			how to deal with -2.0, -4.0 when cancel 
//				if they continue..... like -2.0,-4.0,-2.0 in our array....
//			cancel doesn't work 
//      the first position in array of points. when scale the path to final path    !done

public class SetMap implements ISetMap, ActionListener {
	private JFrame jf;
	private java.util.List<Point2D> Points = new ArrayList<Point2D>();
	private java.util.List<Point2D> ContrPoints = new ArrayList<Point2D>();
	private java.util.List<Point2D> tmp = new ArrayList<Point2D>();
	private java.util.List<Point2D> outside = new ArrayList<Point2D>();
	
	private boolean fin = false;
	private Path2D path;
	private Path2D fpath = new Path2D.Double();
	private Path2D finalPath = new Path2D.Double();
	private JCheckBox checkBox = new JCheckBox("Edit");
	private Point2D firstPoint;
	private boolean finished=false;
	private Line2D finishLine;
	private Line2D line;
	private double initAngle;
	private boolean isLine = true;
	
	private java.util.List<Point2D> aa = new ArrayList<Point2D>();
	
	
//	public static void main(String... args) {
//		SetMap sm = new SetMap();	
//	}
	public SetMap() {
		tmp.add(new Point2D.Double(-2.0,-2.0));
		this.jf = this.initJF();
	}
		
	private JFrame initJF() {
		JFrame jf = new JFrame("Bugatti TNG Map Designer");
		JPanel jp = new MyPanel();
		JToolBar jtb = new JToolBar();
		jtb.setPreferredSize(new Dimension(1024,40));
		jtb.setBackground(new Color(102,152,252));
		checkBox.addChangeListener((a)->jf.repaint());	
		JButton buttonLine = new JButton("LineTo");
		buttonLine.setActionCommand("line");
		buttonLine.addActionListener(this);
		JButton buttonQuad = new JButton("QuadTo");
		buttonQuad.setActionCommand("quad");
		buttonQuad.addActionListener(this);
		JButton buttonCancel = new JButton("Cancel");
		buttonCancel.setActionCommand("cancel");
		buttonCancel.addActionListener(this);
		JButton buttonClear = new JButton("Clear");
		buttonClear.setActionCommand("clear");
		buttonClear.addActionListener(this);
		JButton buttonClose = new JButton("ClsoePath");
		buttonClose.setActionCommand("close");
		buttonClose.addActionListener(this);
		JButton buttonDone = new JButton("Done");
		buttonDone.setActionCommand("done");
		buttonDone.addActionListener(this);
		jtb.add(checkBox);
		jtb.add(buttonLine);
		jtb.add(buttonQuad);
		jtb.add(buttonCancel);
		jtb.add(buttonClear);
		jtb.add(buttonClose);
		jtb.add(buttonDone);
		MouseInputAdapter ml = new MyMouseInputListener();
		jp.addMouseListener(ml);
		jp.addMouseMotionListener(ml);
		jp.add(jtb);
		jf.add(jp);		
		jf.setSize(new Dimension(1024,1024));
		jf.setDefaultCloseOperation(3);
		jf.setResizable(false);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		return jf;
	}
	private Shape calcPath() {
		boolean checkLeg = false;
		Point2D startPoint = null;
		Point2D endPoint = null;
		Point2D controll1 = null;
		Points = new ArrayList<Point2D>();
		ContrPoints = new ArrayList<Point2D>();		
		path = new Path2D.Double();
		int pc = 0;
		for (int n = 0; n < tmp.size(); n++) {
			Point2D point = tmp.get(n);
			if (point.getX() == -2.0 && point.getY() == -2.0) {
				isLine = true;
				if (checkLeg) {
					tmp.remove(n-1);
					checkLeg = false;
					calcPath();
					jf.repaint();
				}
				continue;
			} else {
				if (point.getX() == -4.0 && point.getY() == -4.0) {
					isLine = false;
					continue;
				}
			}
			if (isLine) {
				if (startPoint == null) {
					startPoint = point;
					firstPoint = point;
					Points.add(point);
					path.moveTo(startPoint.getX(), startPoint.getY());
				} else {
					endPoint = point;	
					Points.add(point);
					path.lineTo(endPoint.getX(), endPoint.getY());
					startPoint = endPoint; 
				}		
			} else {   
				if (startPoint == null) {
					startPoint = point;
					firstPoint = point;
					Points.add(point);
					path.moveTo(startPoint.getX(), startPoint.getY());
					pc = 1;
					continue;
				} else {
					if (pc == 0) {
						pc = 1;
						path.moveTo(startPoint.getX(), startPoint.getY());
					}
					if (pc == 1) {
						controll1 = point;
						ContrPoints.add(point);
						checkLeg = true;
						pc = 2;
						continue;
					}
					if (pc == 2) {
						pc = 0;
						endPoint = point;	
						Points.add(point);
						path.quadTo(controll1.getX(),
									controll1.getY(),
									endPoint.getX(), 
									endPoint.getY());
						startPoint = endPoint;
						checkLeg = false;
					}		
				}								
			}  
		}
		return path;
	}
	
	
	private void haha() {
		
	}
	
	private Point2D calcTan(int m,boolean isInner,double o) {
		Point2D gotPoint = new Point2D.Double();
		double offset = o;
		boolean conpoint = false;
		Point2D d = new Point2D.Double();
		Point2D pre;// = Points.get(Points.size() - 2);
		Point2D current = tmp.get(m);
		if (ContrPoints.contains(current)) {
			conpoint = true;
		}
			
		Point2D next;
		if (m == 0) {
			int index = tmp.size() - 2;
			pre = tmp.get(index - 2);
			while (pre.getX() == -2.0 || pre.getX() == -4.0) {
				index -= 1; //loop infinitive....
//				System.out.println(index + " : " + pre);
				pre = tmp.get(index);
				//------>
				if (!conpoint) {
					if (ContrPoints.contains(pre)) {
						index -= 1;
						pre = tmp.get(index);
					}
				}
				//<------
				
			}
		} else {
			int index = m - 1;
			pre = tmp.get(index);
			while (pre.getX() == -2.0 || pre.getX() == -4.0) {
				index -= 1;
				if (index == -1) {
					index = tmp.size() - 2;
				}
				pre = tmp.get(index);
				
				//------>
			
				if (ContrPoints.contains(pre)) {
					index -= 1;
					pre = tmp.get(index);
				}
				
				//<-----
			}
		}
		int index = m + 1;
		next = tmp.get(index);
		while (next.getX() == -2.0 || next.getX() == -4.0) {
			index += 1;
			next = tmp.get(index);
			
			// ----- >>
			
			if (ContrPoints.contains(next)) {
				index += 1;
				next = tmp.get(index);
			}
			
			//< -----
		}
		
		if (conpoint) {
			gotPoint = calcContrPointShift(current,pre,next,isInner,offset);
//			System.out.println("current : " + current);
			return gotPoint;
		}
		
		
		path.setWindingRule(Path2D.WIND_NON_ZERO);
		double x = 0.0;
		double y = 0.0;
		
		double pc = current.distance(pre);   //ba
		double cn = current.distance(next);   //bc
		
		double k = pc / cn;
		x = (next.getX() - current.getX()) * k + current.getX();
		y = (next.getY() - current.getY()) * k + current.getY();
		d = new Point2D.Double(x,y);
		x = (pre.getX() + d.getX()) / 2;
		y = (pre.getY() + d.getY()) / 2;
		Point2D e = new Point2D.Double(x,y);
		
		double k1 = (e.getY() - current.getY()) / (e.getX() - current.getX());
		double b = current.getY() - k1 * current.getX();		
		x = current.getX() + offset;
		y = k1 * x + b;
		if (Math.abs(y - current.getY()) > Math.abs(offset)) {
			if (y > current.getY()) {
				y = current.getY() + offset;
				x = (y - b) / k1;
//				System.out.println("2 : " + x + " : " + y);
			} else {
				y = current.getY() - offset;
				x = (y - b) / k1;
//				System.out.println("3 : " + x + " : " + y);
			}
		}
		gotPoint = new Point2D.Double(x,y);
		
//		System.out.println("b : k " + b + " : " + k);
		if (isInner) {
//			System.out.println(path.contains(gotPoint));
			if (!path.contains(gotPoint)) {
				x = current.getX() - offset;
				y = k1 * x + b;
//				System.out.println("1 : " + x + " : " + y);
				if (Math.abs(y - current.getY()) > Math.abs(offset)) {
					if (y > current.getY()) {
						y = current.getY() + offset;
						x = (y - b) / k1;
					} else {
						y = current.getY() - offset;
						x = (y - b) / k1;
					}
				}
				gotPoint = new Point2D.Double(x,y);
			}
		} else {
//			System.out.println(path.contains(gotPoint));
			if (path.contains(gotPoint)) {
				x = current.getX() - offset;
				y = k1 * x + b;
//				System.out.println("4 : " + x + " : " + y);
				if (Math.abs(y - current.getY()) > Math.abs(offset)) {
					if (y > current.getY()) {
						y = current.getY() + offset;
						x = (y - b) / k1;
//						System.out.println("5 : " + x + " : " + y);
					} else {
						y = current.getY() - offset;
						x = (y - b) / k1;
//						System.out.println("6 : " + x + " : " + y);
					}
				}
				gotPoint = new Point2D.Double(x,y);
			}
		}
		return gotPoint;		
	}
	private void e() {
		
	}
	private Point2D calcContrPointShift(Point2D current, Point2D pre, Point2D next,boolean isInner,double offset) {
		double x = 0.0;
		double y = 0.0;
		double x1 = 0.0;
		double y1 = 0.0;
		boolean inPath = false;
		if (path.contains(current)) {
			inPath = true;
		}

		path.setWindingRule(Path2D.WIND_NON_ZERO);
		
		double k = (next.getY() - pre.getY()) / (next.getX() - pre.getX());
		// y = k * x + b1;
		double b1 = next.getY() - k * next.getX();
//		System.out.println(k);
		double k1 = -1 / k;
//		System.out.println(k1);
		double b = current.getY() - k1 * current.getX();
//		System.out.println(b);
//		System.out.println(x + " : " + y);
		x1 = (b - b1) / (k - k1);
		y1 = k1 * x1 + b;
		Point2D mid = new Point2D.Double(x1,y1);
		
		x = current.getX() + offset;
		y = k1 * x + b;
		Point2D gotPoint = new Point2D.Double(x,y);
		if (Math.abs(y - current.getY()) > Math.abs(offset)) {   
			if (y > current.getY()) {
				y = current.getY() + offset;
				x = (y - b) / k1;
			} else {
				y = current.getY() - offset;
				x = (y - b) / k1;
			}
			gotPoint = new Point2D.Double(x,y);
		}

		if (isInner) {
			if (inPath) {
				if (gotPoint.distance(mid) < current.distance(mid)) {
					x = current.getX() - offset;
					y = k1 * x + b;
					if (Math.abs(y - current.getY()) > Math.abs(offset)) {
						if (y > current.getY()) {
							y = current.getY() + offset;
							x = (y - b) / k1;
						} else {
							y = current.getY() - offset;
							x = (y - b) / k1;
						}
					}
					gotPoint = new Point2D.Double(x,y);
				}
			} else {
				if (gotPoint.distance(mid) > current.distance(mid)) {
					x = current.getX() - offset;
					y = k1 * x + b;
					if (Math.abs(y - current.getY()) > Math.abs(offset)) {
						if (y > current.getY()) {
							y = current.getY() + offset;
							x = (y - b) / k1;
						} else {
							y = current.getY() - offset;
							x = (y - b) / k1;
						}
					}
					gotPoint = new Point2D.Double(x,y);
				}
			}
		} else {	
			if (inPath) {
				if (gotPoint.distance(mid) > current.distance(mid)) {
					x = current.getX() - offset;
					y = k1 * x + b;
					if (Math.abs(y - current.getY()) > Math.abs(offset)) {
						if (y > current.getY()) {
							y = current.getY() + offset;
							x = (y - b) / k1;
						} else {
							y = current.getY() - offset;
							x = (y - b) / k1;
						}
					}
					gotPoint = new Point2D.Double(x,y);
				}
			} else {
				if (gotPoint.distance(mid) < current.distance(mid)) {
					x = current.getX() - offset;
					y = k1 * x + b;
					if (Math.abs(y - current.getY()) > Math.abs(offset)) {
						if (y > current.getY()) {
							y = current.getY() + offset;
							x = (y - b) / k1;
						} else {
							y = current.getY() - offset;
							x = (y - b) / k1;
						}
					}
					gotPoint = new Point2D.Double(x,y);
				}
			}
			
//			if (gotPoint.distance(pre) < current.distance(pre)) {
		}
		aa.add(gotPoint);
		inPath = false;
		return gotPoint;
	}

	//may be remove needed ----- >
	private void xixi() {
		
	}
	private Path2D calcShift(double offset, boolean I) {
//		finalPath = new Path2D.Double();
		outside = new ArrayList<Point2D>();
		Point2D point = null;
		double x;
		double y;
		Point2D ptmp;	
		Point2D first = null;
		outside.add(new Point2D.Double(-2.0,-2.0));
		path.setWindingRule(Path2D.WIND_NON_ZERO); //+++++++++++++++++++++++++++
//		System.out.println(tmp);
//		System.out.println("=================================================================================");
		boolean inner = I;
//		if (offset <= 0 ) {
//			inner = true;
//		}
		x = 0.0;
		y = 0.0;
		for (int m = 0; m < tmp.size() - 1; m++) {
			point = tmp.get(m);
			if (point.getX() == -2.0 && point.getY() == -2.0) {
				outside.add(new Point2D.Double(-2.0,-2.0));
				continue;
			} else {
				if (point.getX() == -4.0 && point.getY() == -4.0) {
					outside.add(new Point2D.Double(-4.0,-4.0));
					continue;
				}
			}
			ptmp = calcTan(m,inner,offset);
			x = ptmp.getX();
			y = ptmp.getY();
			outside.add(new Point2D.Double(x,y));
			if (first == null) {
				first = new Point2D.Double(x,y);
			}	
		}
		outside.add(first);
//		System.out.println("outside : " +outside);
		return drawOuter();
	} 
	private void hhe() {
	}
	//---->remove
	private Path2D drawOuter() {
		Path2D outerPath = new Path2D.Double();
		boolean checkLeg = false;
		Point2D startPoint = null;
		Point2D endPoint = null;
		Point2D controll1 = null;
		int pc = 0;
		for (int n = 0; n < outside.size(); n++) {
			Point2D point = outside.get(n);
			if (point.getX() == -2.0 && point.getY() == -2.0) {
				isLine = true;
				if (checkLeg) {
//					System.out.println(n);
					outside.remove(n-1);
					checkLeg = false;
					drawOuter();
					jf.repaint();
				}
				continue;
			} else {
				if (point.getX() == -4.0 && point.getY() == -4.0) {
					isLine = false;
					continue;
				}
			}
			if (isLine) {
				if (startPoint == null) {
					startPoint = point;
					outerPath.moveTo(startPoint.getX(), startPoint.getY());
				} else {
					endPoint = point;	
					outerPath.lineTo(endPoint.getX(), endPoint.getY());
					startPoint = endPoint; 
				}		
			} else {   
				if (startPoint == null) {
					startPoint = point;
					outerPath.moveTo(startPoint.getX(), startPoint.getY());
					pc = 1;
					continue;
				} else {
					if (pc == 0) {
						pc = 1;
						outerPath.moveTo(startPoint.getX(), startPoint.getY());
					}
					if (pc == 1) {
						controll1 = point;
						checkLeg = true;
						pc = 2;
						continue;
					}
					if (pc == 2) {
						pc = 0;
						endPoint = point;	
						outerPath.quadTo(controll1.getX(),
									controll1.getY(),
									endPoint.getX(), 
									endPoint.getY());
						startPoint = endPoint;
						checkLeg = false;
					}		
				}								
			}  
		}		
		return outerPath;
	}	
	public Point2D getfirstPoint() {
		return this.firstPoint;
	}
	public Line2D getFinishLine() {
		return this.line;
	}
	public Path2D getFpath() {
		return this.fpath;
	}
	@Override
	public java.util.List<Point2D> getPoints() {
		return this.Points;
	}
	@Override
	public void showJF() {
		this.jf.setVisible(true);
	}
	@Override
	public void disJF() {
		this.finished = false;
		this.jf.setVisible(false);
	}
	public boolean getSym() {
		return this.finished;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("line")) {
			System.out.println("This is line mode.");
			tmp.add(new Point2D.Double(-2.0,-2.0));
		} else {
			if (e.getActionCommand().equals("quad")) {
				System.out.println("This is quad mode.");
				tmp.add(new Point2D.Double(-4.0,-4.0));
			} else {
				if (e.getActionCommand().equals("clear")) {
					System.out.println("clear");
					tmp =  new ArrayList<Point2D>();
					tmp.add(new Point2D.Double(-2.0,-2.0));
					ContrPoints = new ArrayList<Point2D>();
					Points = new ArrayList<Point2D>();
					outside = new ArrayList<Point2D>();
					this.fin = false;
					jf.repaint();
				} else {
					if (e.getActionCommand().equals("done")) {
						System.out.println("all done.");
						this.fin = true;
						Path2D a = this.calcShift(10,false);
						System.out.println("outter : " + outside);
						Point2D leftPoint = outside.get(outside.size()-1);
						finalPath.append(a,false);
						Path2D b = this.calcShift(-10.0,true);
						System.out.println("inner : " + outside);
						Point2D rightPoint = outside.get(outside.size()-1);
						finalPath.append(b,false);					
						line = setFinishLine(leftPoint,rightPoint);
						finish();
						jf.repaint();
						fpath = finalPath;
						finalPath = new Path2D.Double();
					} else {
						if (e.getActionCommand().equals("close")) {
							tmp.add(new Point2D.Double(-2.0,-2.0));
							tmp.add(firstPoint);
							calcPath();
							jf.repaint();
						} else {
							if (e.getActionCommand().equals("cancel")) {
								if (tmp.size() != 0) {
									if (tmp.get(tmp.size()-1).getX() == -2.0 && tmp.get(tmp.size()-1).getY() == -2.0) {
										tmp.remove(tmp.size() - 1);
									} else {
										if (tmp.get(tmp.size()-1).getX() == -4.0 && tmp.get(tmp.size()-1).getY() == -4.0) {
											tmp.remove(tmp.size() - 1);
										}
									}
									if (tmp.size() != 0) {
										tmp.remove(tmp.size() - 1);
									}
									calcPath();
									jf.repaint();
								}
							}
						}
					}	
				}
			}
		}
	}
	private Line2D setFinishLine(Point2D left, Point2D right) {
		finishLine = new Line2D.Double(left,right);
		Line2D line = new Line2D.Double(left.getX() * 18,
										left.getY() * 18,
										right.getX() * 18,
										right.getY() * 18);
		return line;
	}

	private void finish() {
		finished = true;		
	}
	private class MyMouseInputListener extends MouseInputAdapter {
		int indexTmp = -1;
		@Override
		public void mouseDragged(MouseEvent e) {
			if (checkBox.isSelected()) {
				double x = (double) e.getX();            
				double y = (double) e.getY();
//				System.out.println(x + " : " + y);
				if (indexTmp >= 0) {
					System.out.println("hahahahahaha");
					tmp.set(this.indexTmp, new Point2D.Double(x,y));
					calcPath();
				}
			}
			jf.repaint();
		}
		private int findPoint(Point2D newPoint) {
			int index = -1;
			for (int i = 0; i < tmp.size(); i++) {
				if (tmp.get(i).distance(newPoint.getX(),newPoint.getY()) < 5 ){    
					index = i;
					break;
				}
			}
			return index;
		}
		@Override
		public void mousePressed(MouseEvent e) {
			if (fin) {							
				System.out.println(e.getX() + " : " + e.getY());
				jf.repaint();
				return;
			} else {
			if (checkBox.isSelected()) {
				Point2D choose = new Point2D.Double(e.getX(), e.getY());
				this.indexTmp = findPoint(choose);
			}
			
			jf.repaint(); }
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (!checkBox.isSelected() && !fin) {  // remove !fin
				if (tmp != null) {
					Point2D news = new Point2D.Double(e.getX(),e.getY()); 
					tmp.add(news);
					calcPath();
				}
			}
			jf.repaint();
		}
	}
	private class MyPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(new Color(102,152,252));
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setPaint(Color.YELLOW);
			if (Points.size() != 0 && !fin) {
				for (Point2D point : Points) {
					g2d.setPaint(Color.RED);
					Ellipse2D.Double ed = new Ellipse2D.Double(point.getX()-4.0, point.getY()-4.0, 8.0, 8.0);
					g2d.fill(ed);
				}				
			}
			if (ContrPoints.size() != 0 && !fin) {
				for (Point2D point : ContrPoints) {
					g2d.setPaint(Color.BLACK);
					Ellipse2D.Double ed = new Ellipse2D.Double(point.getX(), point.getY(), 8.0, 8.0);
					g2d.fill(ed);
				}				
			}
			if (tmp.size() >= 3 && !fin) {
				g2d.setPaint(Color.YELLOW);
				g2d.draw(path);
			}
			if (fin) {
				g2d.draw(path);		
				System.out.println("cotrol points : " + ContrPoints);
				System.out.println("points : " + Points);
				for (Point2D point : ContrPoints) {
					g2d.setPaint(Color.BLACK);
					Ellipse2D.Double ed = new Ellipse2D.Double(point.getX(), point.getY(), 8.0, 8.0);
					g2d.fill(ed);
				}	
				g2d.setPaint(Color.BLUE);
				g2d.draw(finishLine);
				g2d.setPaint(Color.yellow);
				for (Point2D cp : Points) {
					g2d.draw(new Ellipse2D.Double(cp.getX()-20, cp.getY()-20, 40, 40));
				}
				g2d.setPaint(Color.black);
				for (Point2D c : outside) {
					g2d.draw(new Ellipse2D.Double(c.getX()-4, c.getY()-4, 8, 8));
				}
				g2d.setPaint(Color.green);
				for (Point2D ssss : aa) {
					Ellipse2D.Double cs = new Ellipse2D.Double(ssss.getX(), ssss.getY(), 8.0, 8.0);
					g2d.fill(cs);
				}
				g2d.setPaint(Color.pink);
				Ellipse2D.Double cs1 = new Ellipse2D.Double(firstPoint.getX()-4, firstPoint.getY()-4, 8.0, 8.0);
				g2d.fill(cs1);
				aa = new ArrayList<Point2D>();
				g2d.setPaint(new Color(125,65,145,25));
				fpath.setWindingRule(Path2D.WIND_EVEN_ODD);
//				fpath.setWindingRule(Path2D.WIND_NON_ZERO);
				g2d.fill(fpath);
				g2d.setPaint(Color.red);
				g2d.draw(fpath);
				myScale(g);
			}			
			g2d.dispose();
		}
		private void myScale(Graphics g) {
			AffineTransform at = new AffineTransform();
			at.scale(18.0, 18.0);
			fpath.transform(at);
		}
	}
}
