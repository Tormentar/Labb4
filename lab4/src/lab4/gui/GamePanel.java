package lab4.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import lab4.data.GameGrid;

/**
 * A panel providing a graphical view of the game board
 * @author David
 *
 */

public class GamePanel extends JPanel implements Observer{
	public static final int UNIT_SIZE = 25;
	private GameGrid grid;
	
	/**
	 * The constructor
	 * 
	 * @param grid The grid that is to be displayed
	 */
	public GamePanel(GameGrid grid){
		this.grid = grid;
		grid.addObserver(this);
		Dimension d = new Dimension(grid.getSize()*UNIT_SIZE+1, grid.getSize()*UNIT_SIZE+1);
		this.setMinimumSize(d);
		this.setPreferredSize(d);
		this.setBackground(Color.DARK_GRAY);
	}

	/**
	 * Returns a grid position given pixel coordinates
	 * of the panel
	 * 
	 * @param x the x coordinates
	 * @param y the y coordinates
	 * @return an integer array containing the [x, y] grid position
	 */
	public int[] getGridPosition(int x, int y){
		int[] array = {x / UNIT_SIZE, y / UNIT_SIZE};
		return array;
	}
	
	public void update(Observable arg0, Object arg1) {
		this.repaint();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for (int x = 0; x < grid.getSize(); x++) {
			for(int y = 0; y < grid.getSize(); y++) {
				g.setColor(Color.BLACK);
				g.drawRect(y*UNIT_SIZE, x*UNIT_SIZE, UNIT_SIZE*(y + 1), UNIT_SIZE*(x + 1));
				if (grid.getLocation(y, x) == grid.ME) {
					g.setColor(Color.BLACK);
					g.fillOval(y*UNIT_SIZE, x*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
				} 
				if(grid.getLocation(y, x) == grid.OTHER) {
					g.setColor(Color.WHITE);
					g.fillOval(y*UNIT_SIZE, x*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
				}
			}
		}
	}
}