package rmi.enclosing_circle.thread;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Arrays;

import rmi.enclosing_circle.algorithm.PointsCombinationIterator;
import rmi.enclosing_circle.pojo.Circle;
import rmi.enclosing_circle.pojo.SmallestEnclosingCircle;

public class TwoPointsCombination implements Runnable {
	private ArrayList<Point2D.Double> points;
	private int numberOfPoints;
	private SmallestEnclosingCircle current;
	private long begin;
	private long end;


	public TwoPointsCombination(ArrayList<Double> points, SmallestEnclosingCircle currentMinimum, long start,
			long end) {
		super();
		this.points = points;
		this.numberOfPoints = points.size();
		this.current = currentMinimum;
		this.begin = start;
		this.end = end;
	}

	public SmallestEnclosingCircle getCurrent() {
		return this.current;
	}

	@Override
	public void run() {
//		 for(long i = 0; i < numberOfC2; i = i + grainSize) {
//	        	long begin = i;
//	        	long end = (i + grainSize) > numberOfC2 ? numberOfC2 : i + grainSize;
//	        	TwoPointsCombination t2 = new TwoPointsCombination(points, result, begin, end);
//	        	ThreePointsCombination t3 = new ThreePointsCombination(points, result, begin, end);
//	        	pool.execute(t2);
//	        	pool.execute(t3);
//	        }
//		System.out.println(Thread.currentThread());
		PointsCombinationIterator c = new PointsCombinationIterator(numberOfPoints, 2, begin, end);
		c.next();
		while(c.hasNext()) {
			
			Point2D.Double a = points.get(c.getIndex()[0]);
			Point2D.Double b = points.get(c.getIndex()[1]);
			
			Point2D.Double center = new Point2D.Double((a.x + b.x) / 2,(a.y + b.y) / 2);
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
//			c.next();
			System.out.println(Thread.currentThread() + Arrays.toString(c.next())) ;
//			System.out.println(Thread.currentThread());
		}
	}
	
	private void combine(long bIndex, long eIndex) {
		PointsCombinationIterator c = new PointsCombinationIterator(numberOfPoints, 2, begin, end);
		c.next();
		while(c.hasNext()) {
			
			Point2D.Double a = points.get(c.getIndex()[0]);
			Point2D.Double b = points.get(c.getIndex()[1]);
			
			Point2D.Double center = new Point2D.Double((a.x + b.x) / 2,(a.y + b.y) / 2);
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
			c.next();
//			System.out.println(Thread.currentThread() + Arrays.toString(c.next())) ;
//			System.out.println(Thread.currentThread());
		}
	}
}
