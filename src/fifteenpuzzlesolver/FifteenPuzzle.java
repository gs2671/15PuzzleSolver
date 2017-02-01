package fifteenpuzzlesolver;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JPanel;

class FifteenPuzzle extends JPanel {
	final static int size = 4;
	int[][] tiles = new int[size][size];
	final int[][] win = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 14, 15, 0 } };

	public FifteenPuzzle() {
		final int dim = 640;
		setPreferredSize(new Dimension(dim, dim));
		setBackground(Color.white);
		setForeground(new Color(0x6495ED));
		setFont(new Font("SansSerif", Font.BOLD, 60));
		int[][] arr = new int[size][size];
		ArrayList ele = new ArrayList<>();
		for (int i = 0; i < size * size; i++)
			ele.add(i);
		do {
			Collections.shuffle(ele);
		} while (isSolvable(ele));

		int k = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				arr[i][j] = ele.indexOf(k);
				k++;
			}
		}
		tiles = arr;
	}

	public FifteenPuzzle(int[][] arr) {
		final int dim = 640;
		setPreferredSize(new Dimension(dim, dim));
		setBackground(Color.white);
		setForeground(new Color(0x6495ED));
		setFont(new Font("SansSerif", Font.BOLD, 60));
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				tiles[i][j] = arr[i][j];
	}

	public void set(int[][] arr) {
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				tiles[i][j] = arr[i][j];
	}

	// Check if the puzzle is solvable
	boolean isSolvable(ArrayList<Integer> ele) {
		int countInversions = 0;
		for (int i = 0; i < size * size; i++) {
			for (int j = 0; j < i; j++) {
				if ((int) ele.get(j) > (int) ele.get(i))
					countInversions++;
			}
		}
		return countInversions % 2 == 0;
	}

	// Draws the Grid on the screen
	void drawGrid(Graphics2D g) {
		final int dim = 640;

		int margin = 80;
		int tileSize = (dim - 2 * margin) / size;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				// If tile value is zero, dont print on screen
				if (tiles[i][j] != 0) {
					int x = margin + j * tileSize;
					int y = margin + i * tileSize;
					String text = String.valueOf(tiles[i][j]);
					g.setColor(getForeground());
					g.fillRoundRect(x, y, tileSize, tileSize, 25, 25);
					g.setColor(Color.black);
					g.drawRoundRect(x, y, tileSize, tileSize, 25, 25);
					g.setColor(Color.white);
					drawCenteredString(g, tileSize, text, x, y);
				}
			}
		}
	}

	// Sets the Text style for writing the value inside a tile
	void drawCenteredString(Graphics2D g, int tileSize, String s, int x, int y) {
		FontMetrics fm = g.getFontMetrics();
		int asc = fm.getAscent();
		int dec = fm.getDescent();
		x = x + (tileSize - fm.stringWidth(s)) / 2;
		y = y + (asc + (tileSize - (asc + dec)) / 2);
		g.drawString(s, x, y);
	}

	@Override
	public void paintComponent(Graphics gg) {
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawGrid(g);
	}
}