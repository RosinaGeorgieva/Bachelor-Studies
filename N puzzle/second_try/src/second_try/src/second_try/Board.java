package second_try;

import java.util.Arrays;

public class Board {
	private int[][] board;
	private int size;
	private int rowOfEmptyTile;
	private int colOfEmptyTile;
	private int manhattanDistance;
	private String direction;
	
	public Board(int size, int xOfEmptyTile, int yOfEmptyTile) {//вика се с goalCoordinates на 0-лата и за конструиране на goal
		this.size = size;
		this.board = new int[this.size][this.size];
		int number = 1;
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				if(i == xOfEmptyTile && j == yOfEmptyTile) {
					this.board[i][j] = 0;
				} else {
					this.board[i][j] = number;
					number++;
				}
			}
		}
		this.rowOfEmptyTile = xOfEmptyTile;
		this.colOfEmptyTile = yOfEmptyTile;
		this.manhattanDistance = 0;
	}
	
	public Board(int[][] board, int size, int xOfEmptyTile, int yOfEmptyTile, String direction) {
		this.size = size;
		this.board = new int[this.size][this.size];
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				this.board[i][j] = board[i][j];
			}
		}
		this.rowOfEmptyTile = xOfEmptyTile;
		this.colOfEmptyTile = yOfEmptyTile;
		this.direction = direction;
		this.manhattanDistance = manhattanDistance();
	}
	
	public Board moveEmptyTileDown() {
		return moveTile(rowOfEmptyTile + 1, colOfEmptyTile, "up");
	}
	
	public Board moveEmptyTileUp() {
		return moveTile(rowOfEmptyTile - 1, colOfEmptyTile, "down");
	}
	
	public Board moveEmptyTileLeft() {
		return moveTile(rowOfEmptyTile, colOfEmptyTile - 1, "right");
	}
	
	public Board moveEmptyTileRight() {
		return moveTile(rowOfEmptyTile, colOfEmptyTile + 1, "left");
	}
	
	public int getManhattanDistance() {
		return this.manhattanDistance;
	}
	
	public boolean isEqualTo(Board other) {
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				if(this.board[i][j] != other.board[i][j]) {
					return false;
				}
			}
		}
		return true;
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
		Board other = (Board) obj;
		if (!Arrays.deepEquals(board, other.board))
			return false;
		return true;
	}

	public String getDirection() {
		return this.direction;
	}
	
	private Board moveTile(int newX, int newY, String direction) {
		if(newX >= 0 && newY >= 0 && newX < this.size && newY < this.size) {
			return new Board(generateChild(newX, newY), this.size, newX, newY, direction);
		}
		return null;
	}
	
	private int[][] generateChild(int xOfEmptyTile, int yOfEmptyTile){
		int[][] childBoard = new int[this.size][this.size];
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				childBoard[i][j] = this.board[i][j];
			}
		}
		int temp = this.board[xOfEmptyTile][yOfEmptyTile];
		childBoard[xOfEmptyTile][yOfEmptyTile] = 0;
		childBoard[this.rowOfEmptyTile][this.colOfEmptyTile] = temp;
		return childBoard;
	}
	
	private int manhattanDistance() {
		int manhattanDistance = 0;
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				int tile = this.board[i][j];
				if(tile != 0) {
					manhattanDistance += Math.abs(goalRow(tile) - i) + Math.abs(goalCol(tile) - j);
				}
			}
		}
		return manhattanDistance;
	}
	
	private int goalRow(int number) {
		return (number - 1) / this.size;
	}
	
	private int goalCol(int number) {
		return (number - 1) % this.size;
	}
}


