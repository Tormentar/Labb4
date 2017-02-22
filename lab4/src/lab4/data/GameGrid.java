package lab4.data;

import java.util.Arrays;
import java.util.Observable;

/**
 * Represents the 2-d game grid
 */

public class GameGrid extends Observable{
	int [][] gameGrid;
	public static final int EMPTY = 0;
	public static final int OTHER = 2;
	public static final int ME = 1;
	public static int INROW = 5;
	private int currentINROW = 0;
	private int corrector = 0;
	private int checkNewRow = 0;
	
	
	/**
	 * Constructor
	 * 
	 * @param size The width/height of the game grid
	 */
	public GameGrid(int size){
		this.gameGrid = new int [size][size];
		
		for(int i = 0; i < size; i++) {
			Arrays.fill(this.gameGrid[i], EMPTY);
		}
	}
	
	/**
	 * Reads a location of the grid
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return the value of the specified location
	 */
	public int getLocation(int x, int y){
		return this.gameGrid[y][x];
		
	}
	/**
	 * Returns the size of the grid
	 * 
	 * @return the grid size
	 */
	public int getSize(){
		return this.gameGrid[0].length;
		
	}
	
	/**
	 * Enters a move in the game grid
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param player
	 * @return true if the insertion worked, false otherwise
	 */
	public boolean move(int x, int y, int player){
		if(this.gameGrid[y][x] == EMPTY) {
			this.gameGrid[y][x] = player;
			return true;
		
		} else {
			return false;
		}
		
	}
	
	/**
	 * Clears the grid of pieces
	 */
	public void clearGrid(){
		for(int i = 0; i < this.gameGrid[0].length; i++) {
			Arrays.fill(this.gameGrid[i], EMPTY);
		}
		
		setChanged();
		notifyObservers();
		
	}
	
	private boolean checkIfWinner(int currentINROW) {
		if(currentINROW == INROW) {
			return true;
		} else {
			return false;
		}
		
	}
	
	private void resetCounters() {
		currentINROW = 0;
		checkNewRow = 0;
		corrector = 0;
	}
	
/*	
	public int resetCurrentINROW() {
		return 0;
	}
*/	
	private boolean checkVertical(int player) {
		int size = this.gameGrid[0].length;
		
		for(int x = (size - 1); x >= 0; x--) {
			for(int y = 0; y <= (size - 1); y++) {
				if(this.gameGrid[y][x] == EMPTY) {
					currentINROW = 0;
					continue;
					
				} else if(this.gameGrid[y][x] == player) {
					currentINROW += 1;
					
				}
				
				if(checkIfWinner(currentINROW) == true) {
					resetCounters();
					return true;
				
				} else {
					continue;
				}
			}	
		}
		resetCounters();
		return false;
	}
	
	private boolean checkHorizontal(int player) {
		int size = this.gameGrid[0].length;
		
		for(int y = 0; y <= (size - 1); y++) {
			for(int x = (size - 1); x >= 0; x--) {
				if(this.gameGrid[y][x] == EMPTY) {
					currentINROW = 0;
					continue;
					
				} else if(this.gameGrid[y][x] == player) {
					currentINROW += 1;
					
				}
				
				if(checkIfWinner(currentINROW) == true) {
					resetCounters();
					return true;
				
				} else {
					continue;
				}
			}	
		}
		resetCounters();
		return false;
	}
	
	
	private int checkDiagonally(int x, int y, int checkNewRowVariable, int player) {
		if(this.gameGrid[y][x] == EMPTY) {
			System.out.println("empty");
			currentINROW = 0;
			corrector += 1;
			
		} else if(this.gameGrid[y][x] == player) {
			System.out.println("player");
			currentINROW += 1;
			corrector += 1;
			
		}
		
		
		if(checkIfWinner(currentINROW) == true) {
			System.out.println("should not be here");
			resetCounters();
			return 2;
		}
		
		
		
		if(checkNewRowVariable == checkNewRow) {
			System.out.println("guten");
			checkNewRow += 1;
			currentINROW = 0;
			corrector = 0;
			return 1;
		}
		
		return 0;
	}
	
	
	private boolean checkDiagonalDownRight(int player) {
		int size = this.gameGrid[0].length - 1;
		
		for(int x = size; x >= 0; x--) {
			for(int y = 0; y <= size; y++) {
				System.out.println("corrector = " + corrector);
				System.out.println("X = " + x);
				System.out.println("Y = " + y);
				System.out.println("Check row = " + checkNewRow);
				
				if(checkDiagonally((x + corrector), y, y, player) == 1) {
					System.out.println("break");
					break;
				}
				
				if(checkDiagonally((x + corrector), y, y, player) == 2) {
					return true;
				
				} else {
					corrector -= 1;
				}
					
			}
		}
		resetCounters();
		
		for(int y = size; y >= 0; y--) {
			for(int x = 0; x <= size; x++) {
				System.out.println("andra snörra");
				
				if(checkDiagonally(x, (y + corrector), x, player) == 1) {
					System.out.println("break2");
					break;
				}
				
				if(checkDiagonally(x, (y + corrector), x, player) == 2) {
					return true;
				} else {
					corrector -= 1;
				}
			}	
		}
		resetCounters();
		return false;
	}
	


	private boolean checkDiagonalDownLeft(int player) {
		int size = this.gameGrid[0].length - 1;
		
		for(int x = 0; x <= size; x++) {
			for(int y = 0; y <= size; y++) {
				System.out.println("corrector = " + corrector);
				System.out.println("X = " + x);
				System.out.println("Y = " + y);
				
				if(checkDiagonally((x - corrector), y, y, player) == 1) {
					break;
				}
				
				if(checkDiagonally((x - corrector), y, y, player) == 2) {
					return true;
					
				} else {
					corrector -= 1;
				}
			}
		}
		
		resetCounters();
		checkNewRow = size;
		
		for(int y = 1; y <= size; y++) {
			for(int x = size; x >= 0; x--) {
				if(this.gameGrid[y + corrector][x] == EMPTY) {
					currentINROW = 0;
					corrector += 1;
					continue;
					
				} else if(this.gameGrid[y + corrector][x] == player) {
					currentINROW += 1;
					corrector += 1;
					
				}
				
				if(checkIfWinner(currentINROW) == true) {
					resetCounters();
					return true;
				} 
				
				if(x == checkNewRow) {
					checkNewRow -= 1;
					currentINROW = 0;
					corrector = 0;
					break;
				}
			}	
		}
	
		resetCounters();
		return false;
		
		
	}
	
	
	/**
	 * Check if a player has 5 in row
	 * 
	 * @param player the player to check for
	 * @return true if player has 5 in row, false otherwise
	 */
											
	public boolean isWinner(int player){		
		if(checkVertical(player) == true || checkHorizontal(player) == true || checkDiagonalDownRight(player) == true || checkDiagonalDownLeft(player) == true) {
			return true;
		
		} else {
			return false;
		}
		
	}
	
}