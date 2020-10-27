package rmi.enclosing_circle.pojo;

import java.awt.geom.Point2D.Double;

public class SmallestEnclosingCircle extends Circle{

	public SmallestEnclosingCircle(Double center, java.lang.Double radius) {
		super(center, radius);
	}

	synchronized public void update(Circle rhs) {
		if(rhs.isSmallerThan(this)) {
			this.center = rhs.center;
			this.radius = rhs.radius;
		}
	}
}
