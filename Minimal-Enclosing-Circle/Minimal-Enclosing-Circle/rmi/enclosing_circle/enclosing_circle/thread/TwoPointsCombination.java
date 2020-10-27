package rmi.enclosing_circle.thread;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import rmi.enclosing_circle.pojo.Circle;
import rmi.enclosing_circle.util.PointsCombinationIterator;

public class TwoPointsCombination extends Thread{
	private static Circle currentMinimum;
	private static ArrayList<Point2D.Double> points;
	private static int numberOfPoints;
	private long numberOfCombinations;
	private long combinationsPerThread;
	
	public TwoPointsCombination(long numberOfCombinations, int numberOfThreads) {
		super();
		this.numberOfCombinations = numberOfCombinations;
		this.combinationsPerThread = numberOfCombinations/numberOfThreads;
	}
	
	public static Circle getCurrentMinimum() {
		return currentMinimum;
	}


	public static void setCurrentMinimum(Circle currentMinimum) {
		TwoPointsCombination.currentMinimum = currentMinimum;
	}
	
	
	public static ArrayList<Point2D.Double> getPoints() {
		return points;
	}

	public static void setPoints(ArrayList<Point2D.Double> points) {
		TwoPointsCombination.points = points;
	}

	public static int getNumberOfPoints() {
		return numberOfPoints;
	}

	public static void setNumberOfPoints(int numberOfPoints) {
		TwoPointsCombination.numberOfPoints = numberOfPoints;
	}
	
	private void combinate(long begin, long end) {
		PointsCombinationIterator c = new PointsCombinationIterator(numberOfPoints, 2, begin, end);
		c.next();
		while(c.hasNext()) {
			
			Point2D.Double a = points.get(c.getIndex()[0]);
			Point2D.Double b = points.get(c.getIndex()[1]);
			
			Point2D.Double center = new Point2D.Double((a.x + b.x) / 2,(a.y + b.y) / 2);
//			double radius = center.distance(a);
			double radius = Math.sqrt(Math.pow((center.x - a.x), 2) + Math.pow((center.y - a.y), 2));
			Circle candidateMEC = new Circle(center, radius);
			
			boolean flag = true;
			for(Point2D.Double p : points) {
				if(!candidateMEC.encloses(p)) {
					flag = false;
					break;
				} 
			}
			
			if(candidateMEC.isSmallerThan(currentMinimum) && flag) {
				currentMinimum = candidateMEC;
			}
			
			c.next();
			
		}
	}

	public void run() {
		 for(int i = 0; i < numberOfCombinations; i += combinationsPerThread) {
	        	combinate(i, (i + combinationsPerThread) > numberOfCombinations ? numberOfCombinations : i + combinationsPerThread);
		 }
	}
}
