package com.fmi.uni.sofia.ai.npuzzle;

import java.util.Scanner;

import com.fmi.uni.sofia.ai.npuzzle.node.Node;
import com.fmi.uni.sofia.ai.npuzzle.search.IDAStarSearch;

public class App {
	
	public static void main(String[] args) {
		
		int size, goalIndexOfEmptyTile;
		int[][] board;
		Scanner sc = new Scanner(System.in);
		int N = Integer.valueOf(sc.next());
		goalIndexOfEmptyTile = Integer.valueOf(sc.next());
		size = (int)Math.sqrt(N + 1);
		board = new int[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				board[i][j] = sc.nextInt();
			}
		}
		sc.close();
		
//		Node test = new Node(new int[][]{{1, 2, 3},{4, 5, 6},{0, 7, 8}}, 3);
		long start = System.currentTimeMillis();
		Node startNode = new Node(board, size);
		IDAStarSearch search = new IDAStarSearch(size, goalIndexOfEmptyTile);
//		System.out.println(test.getBoard().equals(search.getGoal()));
//		test.print();
//		System.out.println(search.getGoal());
		search.execute(startNode);
//		
		long end = System.currentTimeMillis();
		long execution = end - start;
		System.out.println("Exec = " + execution);
		System.out.println("Path = " + search.getMinPathSize());
	}
}
