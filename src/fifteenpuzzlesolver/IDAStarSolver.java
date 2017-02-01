package fifteenpuzzlesolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import com.opencsv.CSVWriter;

public class IDAStarSolver {
	private Stack<Board> solution = null;
	private int threshold = 7;
	private int minThreshold = Integer.MAX_VALUE;
	private ArrayList<Board> nodeList = new ArrayList<Board>();
	private ArrayList<int[][]> nodeRead = new ArrayList<int[][]>();
	private ArrayList<Integer> Cost = new ArrayList<Integer>();
	private boolean Learn = false;
	public static int countOfNodesUsed = 0;
	ArrayList<String> list = new ArrayList<String>();

	private static class SearchNode {
		private Board board;
		private int moves;
		private SearchNode previous;

		public SearchNode(Board board, SearchNode previous) {
			this.board = board;
			countOfNodesUsed++;
			if (previous != null) {
				this.moves = previous.moves + 1;
				this.previous = previous;
			}
		}

	}

	public static void Writetoafile(Board tile, int cost, CSVWriter writer) {
		try {
			ArrayList<String> s = new ArrayList<String>();
			int[][] arr = tile.getIntegerArray();
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					s.add(String.valueOf(arr[i][j]));
				}
			}
			s.add(String.valueOf(cost));
			writer.writeNext(s.toArray(new String[s.size()]));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void readFromFile(ArrayList<int[][]> Node, ArrayList<Integer> cost, BufferedReader reader) {
		try {
			String line = "";
			List<String> sVals = new ArrayList<>();
			int[][] tiles = new int[4][4];
			int Index = 0;
			while (((line = reader.readLine()) != null)) {
				sVals = Arrays.asList(line.split(","));
				for (int i = 0; i < 16; i++) {
					System.out.printf("%s", (sVals.get(i)));
					String strin = sVals.get(i);
					tiles[i / 4][i % 4] = Integer.valueOf(strin);
				}
				Node.add(tiles);
				cost.add(Index, Integer.valueOf(sVals.get((sVals.size() - 1))));
				Index++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public IDAStarSolver(Board initial) {
		SearchNode rootNode = new SearchNode(initial, null);
		CSVWriter writer = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("test.txt"));
			readFromFile(nodeRead, Cost, br);

			// System.out.println("Rootnode: "+ rootNode.moves +"
			// Content:"+rootNode.board.toString());
			while (!solve(rootNode)) {
				threshold = minThreshold;
				// System.out.println("Threshold : " + threshold);
				minThreshold = Integer.MAX_VALUE;

			}
			// write the learned data into the file
			writer = new CSVWriter(new FileWriter("test.txt"), ',', CSVWriter.NO_QUOTE_CHARACTER);
			for (int i = 0; i < nodeList.size(); i++)
				Writetoafile(nodeList.get(i), Cost.get(i), writer);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean solve(SearchNode node) {

		try {

			if (node.board.isGoal()) {
				// System.out.println("Goal reached!");
				solution = new Stack<>();
				while (node.previous != null) {
					solution.add(node.board);
					node = node.previous;
				}
				System.out.println("Solved using IDA Star!");
				return true;
			}
			// System.out.println("Goal not reached! Current val:
			// "+(node.board.getManhattanDistance() + node.moves) + "Threshold:
			// "+threshold);
			// System.out.println("Moves: "+node.moves);
			if ((node.board.getManhattanLinearConflictDistance() + node.moves) > threshold) {
				// (When online learning of heuristic is added )
				if (Learn == true) {
					Cost.add(node.moves);
					Learn = false;
				}
				if (minThreshold > (node.board.getManhattanLinearConflictDistance() + node.moves)) {
					minThreshold = node.board.getManhattanLinearConflictDistance() + node.moves;

				}

				return false;
			}

			int hashCodeOfPrevious;

			if (node.previous == null) {
				hashCodeOfPrevious = 0;
			} else {
				hashCodeOfPrevious = node.previous.toString().hashCode();
			}
			for (Board neighbor : node.board.getNeighbors()) {
				int hashCode = neighbor.toString().hashCode();
				if ((node.moves == 0) && (Learn == true)) {
					nodeList.add(neighbor); // for online learning of heuristic
					System.out.printf("learning heurisitcs flag set");
				}
				if (hashCode != hashCodeOfPrevious) {
					if (solve(new SearchNode(neighbor, node))) {
						return true;
					}

				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return false;
	}

	public Stack<Board> getSolution() {
		return solution;
	}
}
