package rmi.enclosing_circle.pojo;

public class Point2D<T> {
	private T x;
	private T y;
	
	public Point2D(T x, T y) {
		this.x = x;
		this.y = y;
	}

	public T getX() {
		return x;
	}

	public void setX(T x) {
		this.x = x;
	}

	public T getY() {
		return y;
	}

	public void setY(T y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
	   if (!(obj instanceof Point2D<?>))
	        return false;
	    if (obj == this)
	        return true;

	    Point2D other = (Point2D) obj;    
	    return (this.x != null && this.x.equals(other.x) 
	            && this.y != null && this.y.equals(other.y));
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((x == null) ? 0 : x.hashCode());
	    result = prime * result + ((y == null) ? 0 : y.hashCode());
	    return result;
	}

	@Override
	public String toString() {
		return  "[x=" + x + ", y=" + y + "]";
	}
}
