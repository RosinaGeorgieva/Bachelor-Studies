package nQueens;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class NQueens {
	private static final int CONFLICTS_CAUSED_BY_THIS_QUEEN = 3;
	private static final int ANY_QUEEN = 0;
	private int N;
	private int[] queens;
	private int[] rowConflicts;
	private int[] mainDiagonalConflicts; 
	private int[] secondaryDiagonalConflicts;
	
	public NQueens(int N) {
		this.N = N;
		this.queens = new int[N];
		this.rowConflicts = new int[N];
		this.mainDiagonalConflicts = new int[2 * N - 1];
		this.secondaryDiagonalConflicts = new int[2 * N - 1];	
	}
	
	public void solve() {
		init();
		rearrangeQueens();
	}
	
	public void print() {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(i == queens[j]) {
					System.out.print(" * ");
				} else {
					System.out.print(" - ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	private void init() {
		int randomRow = ThreadLocalRandom.current().nextInt(0, N);
		queens[0] = randomRow;
		updateConflicts(randomRow, 0, 1);	
		
		List<Integer> rowsWithMinConflicts = new ArrayList<Integer>();
		int currentNumberOfConflicts, minNumberOfConflicts = Integer.MAX_VALUE;
		
		for(int col = 1; col < N; col++) {
			rowsWithMinConflicts =  new ArrayList<Integer>();
			for(int row = 0; row < N; row++) {
				currentNumberOfConflicts = conflictsOnPosition(row, col);
				
				if(currentNumberOfConflicts == minNumberOfConflicts) {
					rowsWithMinConflicts.add(row);
				} else if(currentNumberOfConflicts < minNumberOfConflicts) {
					randomRow = row;
					minNumberOfConflicts = currentNumberOfConflicts;
					rowsWithMinConflicts =  new ArrayList<Integer>();
					rowsWithMinConflicts.add(row);
				}
			}
			
			int size = rowsWithMinConflicts.size();
			if(size != 0) {
				randomRow = rowsWithMinConflicts.get(ThreadLocalRandom.current().nextInt(0, size));
			}
			queens[col] = randomRow;
			updateConflicts(randomRow, col, 1);
		}
	}
	
	private void rearrangeQueens() {
		int col = getMaxConflictQueen();
		int row = getMinConflictPosition(col);
		
		while(conflictsOnPosition(queens[col], col) - CONFLICTS_CAUSED_BY_THIS_QUEEN != 0) {
			updateConflicts(queens[col], col, -1);
			queens[col] = row;
			updateConflicts(queens[col], col, 1);

			col = getMaxConflictQueen();
			row = getMinConflictPosition(col);
		}
	}
	
	private int getMinConflictPosition(int col) {
		List<Integer> rowsWithMinConflicts = new ArrayList<Integer>();
		int minNumberOfConflicts = Integer.MAX_VALUE, currentNumberOfConflicts;
		
		for(int row = 0; row < N; row++) {
			currentNumberOfConflicts = conflictsOnPosition(row, col) - CONFLICTS_CAUSED_BY_THIS_QUEEN;
			
			if(currentNumberOfConflicts == minNumberOfConflicts) {
				rowsWithMinConflicts.add(row);
			} else if(currentNumberOfConflicts < minNumberOfConflicts) {
				minNumberOfConflicts = currentNumberOfConflicts;
				rowsWithMinConflicts =  new ArrayList<Integer>();
				rowsWithMinConflicts.add(row);
			}
		}
		return rowsWithMinConflicts.get(ThreadLocalRandom.current().nextInt(0, rowsWithMinConflicts.size()));
	}
	
	private int getMaxConflictQueen() {
		List<Integer> queensWithMaxNumberOfConflicts = new ArrayList<Integer>();
		int maxNumberOfConflicts = Integer.MIN_VALUE, currentNumberOfConflicts;
		
		for(int col = 0; col < N; col++) {
			currentNumberOfConflicts = conflictsOnPosition(queens[col], col) - CONFLICTS_CAUSED_BY_THIS_QUEEN;
			
			if(currentNumberOfConflicts == 0) {
				continue;
			}
			
			if(currentNumberOfConflicts == maxNumberOfConflicts) {
				queensWithMaxNumberOfConflicts.add(col); 
			} else if(currentNumberOfConflicts > maxNumberOfConflicts) {
				maxNumberOfConflicts = currentNumberOfConflicts;
				queensWithMaxNumberOfConflicts =  new ArrayList<Integer>();
				queensWithMaxNumberOfConflicts.add(col);
			}
		}
		if(maxNumberOfConflicts == Integer.MIN_VALUE) {
			return ANY_QUEEN;
		}
		return queensWithMaxNumberOfConflicts.get(ThreadLocalRandom.current().nextInt(0, queensWithMaxNumberOfConflicts.size()));
	}
	
	private int conflictsOnPosition(int row, int col) {
	    return rowConflicts[row] + mainDiagonalConflicts[toIndex(row, col, Diagonal.MAIN)] + secondaryDiagonalConflicts[toIndex(row, col, Diagonal.SECONDARY)];
	}
	
	private int toIndex(int row, int col, Diagonal diagonal) {
		if(diagonal.equals(Diagonal.MAIN)) {
			return N + col - row - 1;
		}
		return row + col;
	}
	
	private void updateConflicts(int row, int col, int update) {
		rowConflicts[queens[col]] = Math.max(0, rowConflicts[row] + update);
		mainDiagonalConflicts[toIndex(row, col, Diagonal.MAIN)] = Math.max(0, mainDiagonalConflicts[toIndex(row, col, Diagonal.MAIN)] + update);
		secondaryDiagonalConflicts[toIndex(row, col, Diagonal.SECONDARY)] = Math.max(0, secondaryDiagonalConflicts[toIndex(row, col, Diagonal.SECONDARY)] + update);
	}
}
