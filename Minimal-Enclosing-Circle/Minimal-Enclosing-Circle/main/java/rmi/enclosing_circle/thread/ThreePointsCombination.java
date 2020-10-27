package rmi.enclosing_circle.thread;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Arrays;

import rmi.enclosing_circle.algorithm.PointsCombinationIterator;
import rmi.enclosing_circle.pojo.Circle;
import rmi.enclosing_circle.pojo.SmallestEnclosingCircle;

public class ThreePointsCombination implements Runnable {
	private ArrayList<Point2D.Double> points;
	private int numberOfPoints;
	private SmallestEnclosingCircle current;
	private long begin;
	private long end;
	

	public ThreePointsCombination(ArrayList<Double> points, SmallestEnclosingCircle current, long begin, long end) {
		super();
		this.points = points;
		this.numberOfPoints = points.size();
		this.current = current;
		this.begin = begin;
		this.end = end;
	}

	public SmallestEnclosingCircle getCurrent() {
		return this.current;
	}
	
	@Override
	public void run() {
		PointsCombinationIterator c = new PointsCombinationIterator(numberOfPoints, 3, begin, end);
		c.next();
		while(c.hasNext()) {
			
			Point2D.Double a = points.get(c.getIndex()[0]);
			Point2D.Double b = points.get(c.getIndex()[1]);
			Point2D.Double d = points.get(c.getIndex()[2]);
			
			Point2D.Double center = new Point2D.Double(circumcenterX(a, b, d), circumcenterY(a, b, d));
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
//			System.out.println(Thread.currentThread() + Arrays.toString(c.next())) ;
			c.next();
		}
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
