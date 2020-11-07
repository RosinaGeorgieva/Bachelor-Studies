package com.fmi.uni.sofia.ai.npuzzle.search;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import com.fmi.uni.sofia.ai.npuzzle.node.Node;

public class IDAStarSearch {
	int size;
	int goalIndexOfEmptyTile;
	int minPathSize;
	Deque<Node> stack;
	int[][] goal;
	private static final int FOUND = -1;
	private static final int COST_FROM_PARENT_TO_CHILD = 1;
//	private static final int MAX_NUMBER_OF_CHILDREN = 4;
	
	public IDAStarSearch(int size, int goalIndexOfEmptyTile) {
		this.goal = goal(size, goalIndexOfEmptyTile);
		stack = new ArrayDeque<>();
		this.goalIndexOfEmptyTile = goalIndexOfEmptyTile;
		this.size = size;
	}
	
	public int[][] getGoal() {
		return this.goal;
	}
	
	public int execute(Node startNode) {
		int threshold = startNode.getManhattanDistance();
		stack.push(startNode);
		
		while(threshold != -1) {
			threshold = search(0, threshold);
		}
		
		return stack.size(); // exception
	}
	
	private int search(int costToCurrentNode, int threshold) {
		Node node = stack.peek();
//		node.print();
		int costToGoal = costToCurrentNode + node.getManhattanDistance();
		if(costToGoal > threshold) {
			return costToGoal;
		} 
		if(isGoal(node)) {
			minPathSize = stack.size() - 1;
			return FOUND;
		}
		int min = Integer.MAX_VALUE;

		List<Node> children = node.children(findEmptyTile(node));
		for(Node child : children) {//!!!!!!!!!
			if(!stack.contains(child)) {
				stack.push(child);
				int thresholdCandidate = search(costToCurrentNode + COST_FROM_PARENT_TO_CHILD, threshold);
				if( thresholdCandidate == FOUND ) {
					return FOUND;
				}
				if(thresholdCandidate < min) {
					min = thresholdCandidate;
				}
				stack.pop();
			}
		}
//		return -1;
		return min;
	}
	
	private boolean isGoal(Node node) {//ot tuk moje da bavi
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(node.getBoard()[i][j] != goal[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	public int getMinPathSize() {
		return this.minPathSize;
	}
	
	private int findEmptyTile(Node node) {//vyzmojnost za optimizaciq
	int index = 1; 
	for(int i = 0; i < this.size; i++) {
		for(int j = 0; j < this.size; j++) {
			if(node.getBoard()[i][j] == 0) {
				return index;
			} else {
				index++;
			}
		}
	}
//	System.out.println(index);
	return index;
	}
	
	private static int[][] goal(int size, int goalIndexOfEmptyTile) {
		int[][] board = new int[size][size];
		int number = 1;
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(i != (goalIndexOfEmptyTile - 1) % size && j != (goalIndexOfEmptyTile - 1) % size) {
					board[i][j] = number;
					number++;
				} else {
					board[i][j] = 0;
				}
			}
		}
		if(goalIndexOfEmptyTile == -1) {
			board[size - 1][size - 1] = 0;
		}
		return board;
	}
}
