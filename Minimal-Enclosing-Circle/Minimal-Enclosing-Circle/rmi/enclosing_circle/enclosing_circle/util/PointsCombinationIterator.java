package rmi.enclosing_circle.util;

import com.vlkan.combination.CombinationIterator;

public class PointsCombinationIterator extends CombinationIterator {
	public PointsCombinationIterator(int n, int r, long k, long l) {
		super(n, r, k, l);
	}

	public PointsCombinationIterator(int n, int r, long k) {
		super(n, r, k);
	}

	public PointsCombinationIterator(int n, int r) {
		super(n, r);
	}
	
	public int[] getIndex() {
		return index;
	}
}
