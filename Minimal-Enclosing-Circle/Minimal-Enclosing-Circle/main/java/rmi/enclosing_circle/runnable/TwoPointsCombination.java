package rmi.enclosing_circle.runnable;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import com.vlkan.combination.Combination;

import rmi.enclosing_circle.pojo.Circle;
import rmi.enclosing_circle.pojo.SmallestEnclosingCircle;

public class TwoPointsCombination  implements Runnable {
		boolean quiet;
		int numberOfPoints;
		int start;
	 	long end;
	 	ArrayList<Point2D.Double> points;
	 	SmallestEnclosingCircle current;

		public TwoPointsCombination(boolean q, int numberOfPoints, int start, long end, ArrayList<Double> points, SmallestEnclosingCircle current) {
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
			int[] pair = new int[2];
			while(index != end) {
				Combination.get(numberOfPoints, 2, index, pair);
				index++;
				
				Point2D.Double a = extractFromPoints(pair[0]);
				Point2D.Double b = extractFromPoints(pair[1]);
				
				Point2D.Double center = makeCenter(a, b);
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
		
		private Point2D.Double makeCenter(Point2D.Double a, Point2D.Double b) {
			return new Point2D.Double((a.x+b.x)/2, (a.y+b.y)/2);
		}
}
