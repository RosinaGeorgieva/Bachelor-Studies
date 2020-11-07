package com.fmi.uni.sofia.ai.npuzzle.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Node {
	int[][] board;
	int size;
	int manhattanDistance;
	List<Node> children;
	
	Comparator<Node> manhattanDistanceComparator = new Comparator<Node>() {
	@Override
	public int compare(Node first, Node second) {
	     return first.getManhattanDistance().compareTo(second.getManhattanDistance());
	}
	};
	
	public Node(int[][] board, int size) {
		this.size = size;
		this.board = new int[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				this.board[i][j] = board[i][j];
			}
		}
		this.manhattanDistance = manhattanDistance();
	}
	
	public Integer getManhattanDistance() {
		return this.manhattanDistance;
	}

	private int manhattanDistance() {
		int manhattanDistance = 0;
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				int tile = this.getBoard()[x][y];
				if(tile != 0) {
					manhattanDistance += manhattanDistance(getX(tile), getY(tile), x, y);
				}
			}
		}
		return manhattanDistance;
	}
	
	private int manhattanDistance(int goal_x, int goal_y, int actual_x, int actual_y) {
		return Math.abs(goal_x - actual_x) + Math.abs(goal_y - actual_y);
	}
	

	private int getX(int number) {
		return (number - 1) / size;
	}
	
	private int getY(int number) {
		return (number - 1) % size;
	}
	
	public void print() {
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				System.out.print(this.board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("- - -");
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(board);
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
		Node other = (Node) obj;
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(this.board[i][j] != other.board[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	public List<Node> children(int emptyTile) {
		children = new ArrayList<Node>();
		int emptyTileX = (emptyTile - 1) / size;
		int emptyTileY = (emptyTile - 1) % size;
			
		if(!isOutsideLowerBorder(emptyTileX)) {
			children.add(new Node(swap(emptyTileX, emptyTileY, emptyTileX - 1, emptyTileY), size));
		} 
		if (!isOutsideUpperBorder(emptyTileX)) {
			children.add(new Node(swap(emptyTileX, emptyTileY, emptyTileX + 1, emptyTileY), size));
		}
		if(!isOutsideLowerBorder(emptyTileY)) {
			children.add(new Node(swap(emptyTileX, emptyTileY, emptyTileX, emptyTileY - 1), size));
		}
		if(!isOutsideUpperBorder(emptyTileY)) {
			children.add(new Node(swap(emptyTileX, emptyTileY, emptyTileX, emptyTileY + 1), size));
		}
		Collections.sort(children, manhattanDistanceComparator);
		return children;
	}
	
//	public Node generateChild(int numberOfChild, int emptyTile) {
//		int emptyTileX = (emptyTile - 1) / size;
//		int emptyTileY = (emptyTile - 1) % size;
//		Node child = null;
//		switch(numberOfChild) {
//		case 1: 
////			System.out.println(1);
//			if(!isOutsideLowerBorder(emptyTileX)) {
//				child = new Node(swap(emptyTileX, emptyTileY, emptyTileX - 1, emptyTileY), size);
//			}
//			break;
//		case 2:
////			System.out.println(2);
//			if(!isOutsideLowerBorder(emptyTileX)) {
//				child = new Node(swap(emptyTileX, emptyTileY, emptyTileX - 1, emptyTileY), size);
//			}
//			break;
//		case 3:
////			System.out.println(3);
//			if(!isOutsideLowerBorder(emptyTileY)) {
//				child = new Node(swap(emptyTileX, emptyTileY, emptyTileX, emptyTileY - 1), size);
//			}
//			break;
//		default: 
////			System.out.println(4);
//			if(!isOutsideUpperBorder(emptyTileY)) {
//				child = new Node(swap(emptyTileX, emptyTileY, emptyTileX, emptyTileY + 1), size);
//			}
//		}
////		child.print();
//		return child;
//	}
	
	public int[][] getBoard(){
		return this.board;
	}
	
	private boolean isOutsideLowerBorder(int emptyTileIndex) {
		return emptyTileIndex - 1 < 0;
	}
	
	private boolean isOutsideUpperBorder(int emptyTileIndex) {
		return emptyTileIndex + 1 >= size;
	}
	
	private int[][] swap(int x1, int y1, int x2, int y2){
		int[][] derivativeBoard = new int[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
					derivativeBoard[i][j] = board[i][j];
			}
		}
		derivativeBoard[x1][y1] = board[x2][y2];
		derivativeBoard[x2][y2] = board[x1][y1];
		return derivativeBoard;
	}


//	@Override
//	public int compareTo(Node o) {
//		return this.manhattanDistance - ((Node)o).manhattanDistance;
//	}

}
