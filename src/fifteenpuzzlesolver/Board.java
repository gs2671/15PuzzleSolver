package fifteenpuzzlesolver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;

public class Board {
	private int[][] board;

	public Board(int[][] blocks) {
		board = new int[blocks.length][blocks.length];
		for (int i = 0; i < blocks.length; i++)
			for (int j = 0; j < blocks.length; j++) {
				board[i][j] = blocks[i][j];
			}
	}

	public int getHammingDistancePattern(int[][] pattern) {
		int hamming = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				int value = board[i][j];

				if ((value != pattern[i][j]) & (value != 0))
					hamming++;
			}
		}
		return hamming;
	}

	public int findIfExistingBoardsMatch(int[][] pattern, int ActualCost) {

		// Check if Arrays are equal
		int cost = 0;
		int hamming = 0;

		hamming = getHammingDistancePattern(pattern);

		if ((hamming == 0))
			cost = ActualCost + getManhattanLinearConflictDistance();
		// System.out.printf("%d and %d\n", cost, hamming);
		return cost;

	}

	public int getPatternMatchingHeuristic(ArrayList<int[][]> pattern, ArrayList<Integer> Cost) {
		int count = 0;
		/*
		 * To Do move the learned heuristics into a file and call that file here
		 * instead of initialising
		 */
		/* to do move the costs learnt into the above file */
		for (int i = 0; i < 4; i++) {
			count = findIfExistingBoardsMatch(pattern.get(i), Cost.get(i));
			if (count != 0)
				return count;
		}
		// if pattern not found return manhattan
		count = getManhattanLinearConflictDistance();
		return count;
	}

	public int getCombinedManhattanHammingDistance() {

		int threshold = 6;
		if (getManhattanDistance() < threshold)
			return (Math.max(getManhattanDistance(), getHammingDistance()));
		else
			return getManhattanDistance();
	}

	// Calculate the Manhattan Distance of the current board
	public int getManhattanDistance() {
		int count = 0;
		int expected = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				int value = board[i][j];
				expected++;
				if (value != 0 && value != expected) {
					count = count + Math.abs(i - ((value - 1) / board.length))
							+ Math.abs(j - ((value - 1) % board.length));
				}
			}
		}
		return count;
	}

	public int getHammingDistance() {
		int hamming = 0;
		if (hamming != -1)
			return hamming;
		hamming = 0;
		int i = 1;
		for (int[] block : board)
			for (int b : block)
				if (i++ != b && b != 0) {
					hamming++;
				}
		return hamming;
	}

	public int getManhattanLinearConflictDistance() {
		int count = 0;
		int expected = 0;
		final int[][] win = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 14, 15, 0 } };
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				int value = board[i][j];
				expected++;
				if (value != 0 && value != expected) {
					count = count + Math.abs(i - ((value - 1) / board.length))
							+ Math.abs(j - ((value - 1) % board.length));
				}
			}
		}
		// check for linear conflict if 2 elements are in their rows but swapped
		// in postions
		for (int i = 0; i < board.length; i++) {
			ArrayList<Integer> Araay = new ArrayList<Integer>();
			// find and store those common elements
			for (int j = 0; j < board.length; j++) {
				int value = board[i][j];
				boolean contains = IntStream.of(win[i]).anyMatch(x -> x == value);
				if (contains == true)
					Araay.add(value);
			}
			// now compare and increase the manhattan distance by 2
			for (int j = 0; j < Araay.size(); j++) {
				for (int k = j; k < Araay.size(); k++) {
					if ((Araay.get(j) > Araay.get(k)) && (Araay.get(k) != 0))
						count = count + 1;
				}
			}
		}

		return count;
	}

	// Check if we have reached the goal state
	public boolean isGoal() {

		if (getManhattanLinearConflictDistance() == 0)
			return true;
		return false;
	}

	// Get all possible neighbors of the current board
	public Iterable<Board> getNeighbors() {
		Queue<Board> neighbors = new LinkedList<Board>();
		int[] zeroIndex = new int[2];

		// Find the Zero Index
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == 0) {
					zeroIndex[0] = i;
					zeroIndex[1] = j;
					break;
				}
			}
		}

		// Getting the Neighbors
		if (zeroIndex[0] > 0)// Move Zero Up
		{
			Board b = new Board(board);
			int temp = b.board[zeroIndex[0] - 1][zeroIndex[1]];
			b.board[zeroIndex[0] - 1][zeroIndex[1]] = b.board[zeroIndex[0]][zeroIndex[1]];
			b.board[zeroIndex[0]][zeroIndex[1]] = temp;
			neighbors.add(b);
		}
		if (zeroIndex[0] < board.length - 1)// Move Zero Down
		{
			Board b = new Board(board);
			int temp = b.board[zeroIndex[0] + 1][zeroIndex[1]];
			b.board[zeroIndex[0] + 1][zeroIndex[1]] = b.board[zeroIndex[0]][zeroIndex[1]];
			b.board[zeroIndex[0]][zeroIndex[1]] = temp;
			neighbors.add(b);
		}
		if (zeroIndex[1] > 0)// Move Zero Left
		{
			Board b = new Board(board);
			int temp = b.board[zeroIndex[0]][zeroIndex[1] - 1];
			b.board[zeroIndex[0]][zeroIndex[1] - 1] = b.board[zeroIndex[0]][zeroIndex[1]];
			b.board[zeroIndex[0]][zeroIndex[1]] = temp;
			neighbors.add(b);
		}
		if (zeroIndex[1] < board.length - 1)// Move Zero Right
		{
			Board b = new Board(board);
			int temp = b.board[zeroIndex[0]][zeroIndex[1] + 1];
			b.board[zeroIndex[0]][zeroIndex[1] + 1] = b.board[zeroIndex[0]][zeroIndex[1]];
			b.board[zeroIndex[0]][zeroIndex[1]] = temp;
			neighbors.add(b);
		}
		return neighbors;
	}

	// Convert the board array into a string
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board.length; j++)
				str.append(board[i][j]);
		return str.toString();
	}

	public int[][] getIntegerArray() {
		return board;
	}
}
