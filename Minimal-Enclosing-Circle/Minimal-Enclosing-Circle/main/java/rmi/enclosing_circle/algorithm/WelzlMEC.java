//package rmi.enclosing_circle.algorithm;
//
//import java.awt.Point;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Random;
//
//import rmi.enclosing_circle.pojo.Circle;
//import rmi.enclosing_circle.pojo.Point2D; 
//
//public class WelzlMEC {
//	private ArrayList<Point2D<Integer>> points;
//	private int threads;
//
//	/* 
//	 * Returns the smallest circle that encloses all the given points. Runs in expected O(n) time, randomized.
//	 * Note: If 0 points are given, null is returned. If 1 point is given, a circle of radius 0 is returned.
//	 */
//	// Initially: No boundary points known
//	
//	public WelzlMEC(ArrayList<Point2D<Integer>> points, int threads) {
//		super();
//		this.points = points;
//		this.threads = threads;
//	}
//	
//	public Circle makeCircle() {
//		// Clone list to preserve the caller's data, randomize order
//		ArrayList<Point2D<Integer>> shuffled = new ArrayList<Point2D<Integer>>(points);
//		Collections.shuffle(shuffled, new Random());
//		
//		// Progressively add points to circle or recompute circle
//		Circle c = null;
//		for (int i = 0; i < shuffled.size(); i++) {
//			Point2D<Integer> p = shuffled.get(i);
//			if (c == null || !c.contains(p))
//				points = new ArrayList<Point2D<Integer>>(shuffled.subList(0, i + 1));
//				c = makeCircleOnePoint(p);
//		}
//		return c;
//	}
//
//	// One boundary point known
//	private Circle makeCircleOnePoint(Point2D<Integer> p) {
//		Circle c = new Circle(new Point2D<Double>(new Double(p.getX()), new Double(p.getY())), 0.0);
//		for (int i = 0; i < points.size(); i++) {
//			Point2D<Integer> q = points.get(i);
//			if (!c.contains(q)) {
//				if (c.getRadius() == 0)
//					c = makeDiameter(p, q);
//				else
//					points = (ArrayList<Point2D<Integer>>) points.subList(0, i + 1);
//					c = makeCircleTwoPoints(p, q);
//			}
//		}
//		return c;
//	}
//	
//	
//	// Two boundary points known
//	private Circle makeCircleTwoPoints(Point2D<Integer> p, Point2D<Integer> q) {
//		Circle circ = makeDiameter(p, q);
//		Circle left  = null;
//		Circle right = null;
//		
//		// For each point not in the two-point circle
//		Point2D<Integer> pq = new Point2D<Integer>(q.getX() - p.getX(), q.getY() - p.getY());
//		for (Point2D<Integer> r : points) {
//			if (circ.contains(r))
//				continue;
//			
//			// Form a circumcircle and classify it on left or right side
//			double cross = pq.cross(new Point2D<Double>((double)(r.getX() - p.getX()), (double)(r.getY() - p.getY())));
//			Circle c = makeCircumcircle(p, q, r);
//			if (c == null)
//				continue;
//			else if (cross > 0 && (left == null || pq.cross(c.getCenter().subtract(p)) > pq.cross(left.getCenter().subtract(p)))) // new Point2D<Double>(c.getCenter().getX() - p.getX(), c.getCenter().getY() - p.getX())
//				left = c;
//			else if (cross < 0 && (right == null || pq.cross(c.getCenter().subtract(p)) < pq.cross(right.getCenter().subtract(p))))
//				right = c;
//		}
//		
//		// Select which circle to return
//		if (left == null && right == null)
//			return circ;
//		else if (left == null)
//			return right;
//		else if (right == null)
//			return left;
//		else
//			return left.getRadius() <= right.getRadius() ? left : right;
//	}
//	
//	
//	private Circle makeDiameter(Point2D<Integer> a, Point2D<Integer> b) {
//		Point2D<Double> c = new Point2D<Double>((new Double(a.getX() + b.getX()) / 2), new Double(a.getY() + b.getY()) / 2);
//		return new Circle(c, Math.max(Math.hypot(c.getX() - a.getX(), c.getY() - a.getY()), Math.hypot(c.getX() - b.getX(), c.getY() - b.getY())));
//	}
//	
//	
//	static Circle makeCircumcircle(Point2D<Integer> a, Point2D<Integer> b, Point2D<Integer> c) {
//		// Mathematical algorithm from Wikipedia: Circumscribed circle
//		double ox = (Math.min(Math.min(a.getX(), b.getX()), c.getX()) + Math.max(Math.max(a.getX(), b.getX()), c.getX())) / 2;
//		double oy = (Math.min(Math.min(a.getY(), b.getY()), c.getY()) + Math.max(Math.max(a.getY(), b.getY()), c.getY())) / 2;
//		double ax = a.getX() - ox,  ay = a.getY() - oy;
//		double bx = b.getX() - ox,  by = b.getY() - oy;
//		double cx = c.getX() - ox,  cy = c.getY() - oy;
//		double d = (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by)) * 2;
//		if (d == 0)
//			return null;
//		double x = ((ax*ax + ay*ay) * (by - cy) + (bx*bx + by*by) * (cy - ay) + (cx*cx + cy*cy) * (ay - by)) / d;
//		double y = ((ax*ax + ay*ay) * (cx - bx) + (bx*bx + by*by) * (ax - cx) + (cx*cx + cy*cy) * (bx - ax)) / d;
//		Point2D<Double> p = new Point2D<Double>(ox + x, oy + y);
//		double r = Math.max(Math.max(p.distance(new Point2D<Double>(new Double(a.getX()), new Double(a.getY()))), p.distance(new Point2D<Double>( new Double(b.getX()), new Double(b.getY()) ) )), 
//												p.distance(new Point2D<Double>(new Double(c.getX()), new Double(c.getY()))));
//		return new Circle(p, r);
//	}
//	
//}
