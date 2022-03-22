import java.awt.*;
import javax.swing.*;

//Draws the resulting Huffman Tree
class canvas extends JFrame {

	// constuctor
	canvas(PQElement root) {
		super("canvas");

		// create a empty canvas
		Canvas c = new Canvas() {

			// paint the canvas
			public void paint(Graphics g) {
				// set color to black
				g.setColor(Color.BLACK);

				// set Font
				g.setFont(new Font("Bold", 1, 30));

				g.drawString("The Huffman Tree", 850, 30);// draws the title
				this.paintrec(g, root, 940, 100, 460, ' ', 25);// recursively paint the tree nodes
			}

			private void paintrec(Graphics g, PQElement root, int x, int y, int dx, char dir, int font) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("Bold", 1, font));
				g.drawString(root.character + " | " + root.frequency, x, y);
				g.setColor(Color.BLUE);

				// draws blue tree lines
				if (dir == 'l') {
					g.drawLine(x, y, x + 2 * dx, y - 75);
				}
				if (dir == 'r') {
					g.drawLine(x, y, x - 2 * dx, y - 75);

				}
				g.setColor(Color.RED);

				// draws red 0 or 1 on the tree line
				if (dir == 'l') {
					g.drawString("0", x + dx - 7, y - 37);
				}
				if (dir == 'r') {
					g.drawString("1", x - dx, y - 37);

				}
				// draws the node's children recursively
				if (root.left != null)
					paintrec(g, root.left, x - dx, y + 75, (int) (0.5 * dx), 'l', font - 2);
				if (root.right != null)
					paintrec(g, root.right, x + dx, y + 75, (int) (0.5 * dx), 'r', font - 2);

			}
		};

		// set white background
		c.setBackground(Color.WHITE);

		add(c);
		setSize(1920, 1080);
		setVisible(true);
	}

}