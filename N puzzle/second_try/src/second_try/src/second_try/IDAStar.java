package second_try;

import java.util.ArrayDeque;
import java.util.Deque;

public class IDAStar {
	private static final int MAXIMUM_NUMBER_OF_CHILDREN = 4;
	private static final int COST_FROM_PARENT_TO_CHILD = 1;
	private static final int FOUND = -1;
	
	private Deque<Board> path;
	private Deque<Board> result;
	private Board goal;
	private int minimalPathSize;
	
	public IDAStar(int N, int goalIndex) {
		this.path = new ArrayDeque<>();
		this.result = new ArrayDeque<>();
		int size = (int)Math.sqrt(N+1);
		if(goalIndex == -1) {
			this.goal = new Board(size, new Tile(size - 1, size - 1));
		} else {
			this.goal = new Board(size, new Tile(goalRow(goalIndex, size), goalCol(goalIndex, size)));
		}
	}
	
	public int execute(Board start) {
		int threshold = start.getManhattanDistance();
		path.push(start);
		
		while(threshold != -1) {
			threshold = search(0, threshold);
		}
		
		return minimalPathSize;
	}
	
	public void printResult() {
		System.out.println(minimalPathSize);
		while(!result.isEmpty()) {
			Direction direction = result.pop().getDirection();
			if(direction != null) {
				System.out.println(direction.toString());
			}
		}
	}
	
	private int search(int currentPathCost, int threshold) {
		Board current = path.peek();
		int totalPathCost = currentPathCost + current.getManhattanDistance();
		if(totalPathCost > threshold) {
			return totalPathCost;
		}
		if(current.isEqualTo(goal)) {
			setResult();
			return FOUND;
		}
		int min = Integer.MAX_VALUE;
		Board child;
		for(int i = 1; i <= MAXIMUM_NUMBER_OF_CHILDREN; i++) {
			switch(i) {
			case 1: child = current.moveEmptyTileUp(); break;
			case 2: child = current.moveEmptyTileDown(); break;
			case 3: child = current.moveEmptyTileLeft(); break;
			default: child = current.moveEmptyTileRight();
			}
			if(child == null) {
				continue;
			} else {
				if(!path.contains(child)) {
					path.push(child);
					int thresholdCandidate = search(currentPathCost + COST_FROM_PARENT_TO_CHILD, threshold);
					if( thresholdCandidate == FOUND ) {
						return FOUND;
					}
					if(thresholdCandidate < min) {
						min = thresholdCandidate;
					}
					path.pop();
				}
			}
		}
		return min;
	}
	
	private void setResult() {
		minimalPathSize = path.size() - 1;
		while(!path.isEmpty()) {
			result.push(path.pop());
		}
	}
	
	private int goalRow(int number, int size) {
		return (number - 1) / size;
	}
	
	private int goalCol(int number, int size) {
		return (number - 1) % size;
	}
	
	public Board getGoal() {
		return this.goal;
	}
}
