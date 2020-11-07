package second_try;

import java.util.Arrays;

public class Board {
	private static final int MANHATTAN_DISTANCE_OF_GOAL = 0;
	
	private int[][] board;
	private int size;
	private int manhattanDistance;
	private Tile emptyTile;
	private Direction direction;
	
	public Board(int size, Tile emptyTile) {//вика се с goalCoordinates на 0-лата и за конструиране на goal
		this.size = size;
		this.emptyTile = emptyTile;
		this.board = new int[this.size][this.size];
		int number = 1;
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				if(i == this.emptyTile.getRow() && j == this.emptyTile.getCol()) {
					this.board[i][j] = 0;
				} else {
					this.board[i][j] = number;
					number++;
				}
			}
		}
		this.manhattanDistance = MANHATTAN_DISTANCE_OF_GOAL;
	}
	
	public Board(int[][] board, int size, Tile emptyTile, Direction direction) {
		this.size = size;
		this.board = new int[this.size][this.size];
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				this.board[i][j] = board[i][j];
			}
		}
		this.emptyTile = emptyTile;
		this.direction = direction;
		this.manhattanDistance = manhattanDistance();
	}
	
	public Board moveEmptyTileDown() {
		return moveTile(new Tile(emptyTile.getRow() + 1, emptyTile.getCol()), Direction.UP);
	}
	
	public Board moveEmptyTileUp() {
		return moveTile(new Tile(emptyTile.getRow() - 1, emptyTile.getCol()), Direction.DOWN);
	}
	
	public Board moveEmptyTileLeft() {
		return moveTile(new Tile(emptyTile.getRow(), emptyTile.getCol() - 1), Direction.RIGHT);
	}
	
	public Board moveEmptyTileRight() {
		return moveTile(new Tile(emptyTile.getRow(), emptyTile.getCol() + 1), Direction.LEFT);
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
	

	public Direction getDirection() {
		return this.direction;
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

	
	private Board moveTile(Tile newEmptyTile, Direction direction) {
		if(newEmptyTile.getRow() >= 0 && newEmptyTile.getCol() >= 0 && newEmptyTile.getRow() < this.size && newEmptyTile.getCol() < this.size) {
			return new Board(generateChild(newEmptyTile), this.size, newEmptyTile, direction);
		}
		return null;
	}
	
	private int[][] generateChild(Tile emptyTile){
		int[][] childBoard = new int[this.size][this.size];
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				childBoard[i][j] = this.board[i][j];
			}
		}
		int temp = this.board[emptyTile.getRow()][emptyTile.getCol()];
		childBoard[emptyTile.getRow()][emptyTile.getCol()] = 0;
		childBoard[this.emptyTile.getRow()][this.emptyTile.getCol()] = temp;
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


