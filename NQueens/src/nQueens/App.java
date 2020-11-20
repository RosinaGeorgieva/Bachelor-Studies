package nQueens;

import java.util.Scanner;

public class App {
	public static void main(String[] args) {
		int N = getInput();
		
		long beginning = System.currentTimeMillis();
		NQueens puzzle = new NQueens(N);
		
		puzzle.solve();
		
		long end = System.currentTimeMillis();
		long duration = end - beginning;
		System.out.println("Duration = " + duration);
//		puzzle.print();
	}
	
	private static int getInput() {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		sc.close();
		return N;
	}
}
