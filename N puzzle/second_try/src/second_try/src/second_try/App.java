package second_try;

import java.util.Scanner;

public class App {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = Integer.valueOf(sc.next());
		int goalIndexOfEmptyTile = Integer.valueOf(sc.next());
		int size = (int)Math.sqrt(N + 1);
		int[][] board = new int[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				board[i][j] = sc.nextInt();
			}
		}
		sc.close();
		
		Board start = new Board(board, size, indicesOfEmptyTile(board, size)[0], indicesOfEmptyTile(board, size)[1], "start");

		IDAStar algorithm = new IDAStar(N, goalIndexOfEmptyTile);
				
		long beginning = System.currentTimeMillis();
		algorithm.execute(start);
		long end = System.currentTimeMillis();
		long duration = end - beginning;
		
		System.out.println("Duration = " + duration);
//		System.out.println("Shortest Path Length = " + minimalPathCost);
		algorithm.result();
	}
	
	private static int[] indicesOfEmptyTile(int[][] board, int size) {
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(board[i][j] == 0) {
					return new int[] {i, j};
				}
			}
		}
		return null;
	}

}
