package rmi.enclosing_circle.pojo;

import java.awt.geom.Point2D;

public class Circle {
	private static final double MULTIPLICATIVE_EPSILON = 1 + 1e-14;
	
	private Point2D.Double center;
	private Double radius;
	
	public Circle(Point2D.Double center, Double radius) {
		super();
		this.center = center;
		this.radius = radius;
	}
	
	public Point2D.Double getCenter() {
		return center;
	}
	public void setCenter(Point2D.Double center) {
		this.center = center;
	}
	public Double getRadius() {
		return radius;
	}
	public void setRadius(Double radius) {
		this.radius = radius;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((center == null) ? 0 : center.hashCode());
		result = prime * result + ((radius == null) ? 0 : radius.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Circle other = (Circle) obj;
		if (center == null) {
			if (other.center != null)
				return false;
		} else if (!center.equals(other.center))
			return false;
		if (radius == null) {
			if (other.radius != null)
				return false;
		} else if (!radius.equals(other.radius))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Circle [center=" + center + ", radius=" + radius + "]";
	}
	
//	public boolean contains(Point2D<Integer> p) {
//		return  Math.hypot(center.getX() - p.getX(), center.getY() - p.getY()) <= radius * MULTIPLICATIVE_EPSILON;
//	}
//	
//	
//	public boolean contains(Collection<Point2D<Integer>> ps) {
//		for (Point2D<Integer> p : ps) {
//			if (!contains(p))
//				return false;
//		}
//		return true;
//	}
	
	public boolean isSmallerThan(Circle rhs) {
		return this.radius < rhs.radius;
	}
	
	public boolean encloses(Point2D.Double p) {
//		return center.distance(p) <= radius;
		return (center.x - p.x)*(center.x - p.x) + (center.y - p.y)*(center.y - p.y) <= radius*radius;
	}
}
