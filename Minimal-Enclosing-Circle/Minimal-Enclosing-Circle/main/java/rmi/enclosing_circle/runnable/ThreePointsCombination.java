package rmi.enclosing_circle.runnable;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import com.vlkan.combination.Combination;

import rmi.enclosing_circle.pojo.Circle;
import rmi.enclosing_circle.pojo.SmallestEnclosingCircle;

public class ThreePointsCombination implements Runnable {
	boolean quiet;
	int numberOfPoints;
	int start;
 	long end;
 	ArrayList<Point2D.Double> points;
 	SmallestEnclosingCircle current;
 	
	public ThreePointsCombination(boolean q, int numberOfPoints, int start, long end, ArrayList<Double> points,
			SmallestEnclosingCircle current) {
		super();
		this.quiet = q;
		this.numberOfPoints = numberOfPoints;
		this.start = start;
		this.end = end;
		this.points = points;
		this.current = current;
	}
 	
 	public void run() {
 		if(!quiet) {
			System.out.println("[INFO] Thread started: id = " + Thread.currentThread().getId() + ".");
		}
 		int index = start;
		int[] triple = new int[3];
		while(index != end) {
			Combination.get(numberOfPoints, 3, index, triple);
			index++;
			
			Point2D.Double a = extractFromPoints(triple[0]);
			Point2D.Double b = extractFromPoints(triple[1]);
			Point2D.Double c = extractFromPoints(triple[2]);
			
			Point2D.Double center = makeCenter(a, b, c);
			double radius = center.distance(a);
			Circle candidateMEC = new Circle(center, radius);
			
			boolean containsAll = true;
			for(Point2D.Double p : points) {
				if(!candidateMEC.encloses(p)) {
					containsAll = false;
					break;
				}
			}
			
			if(containsAll) {
				current.update(candidateMEC);
			}
		}
		if(!quiet) {
			System.out.println("[INFO] Thread ended: id = " + Thread.currentThread().getId() + ".");
		}
 	}
 	
 	private Point2D.Double extractFromPoints(int index) {
 		return points.get(index);
 	}
 	
 	private Point2D.Double makeCenter(Point2D.Double a, Point2D.Double b, Point2D.Double c){
 		double X = circumcenterX(a, b, c);
 		double Y = circumcenterY(a, b, c);
 		return new Point2D.Double(X,Y);
 	}
 	
 	private double circumcenterX(Point2D.Double a, Point2D.Double b, Point2D.Double c) {
		double d = 2*(a.x*(b.y-c.y) + b.x*(c.y-a.y) + c.x*(a.y - b.y));
		return (1/d)*((a.x*a.x + a.y*a.y)*(b.y-c.y) + (b.x*b.x + b.y*b.y)*(c.y-a.y) + (c.x*c.x + c.y*c.y)*(a.y-b.y));
	}
	
	private double circumcenterY(Point2D.Double a, Point2D.Double b, Point2D.Double c) {
		double d = 2*(a.x*(b.y-c.y) + b.x*(c.y - a.y) + c.x*(a.y - b.y));
		return (1/d)*((a.x*a.x + a.y*a.y)*(c.x - b.x) + (b.x*b.x + b.y*b.y)*(a.x-c.x) + (c.x*c.x + c.y*c.y)*(b.x - a.x));
	}
}
