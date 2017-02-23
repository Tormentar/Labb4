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
	private int currentState = 0;
	
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
			setChanged();
			notifyObservers();
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
	
	/** 
	 * Method used by the method isWinner that checks if you have INROW in a row
	 * @param currentINROW
	 * @return true if you get INROW in a row, false otherwise
	 */
	private boolean checkIfWinner(int currentINROW) {
		if(currentINROW == INROW) {
			return true;
			
		} else {
			return false;
		}	
	}
	
	/**
	 * Resets all counters for the method isWinner
	 */
	private void resetCounters() {
		currentINROW = 0;
		checkNewRow = 0;
		corrector = 0;
		currentState = 0;
	}
	
	/**
	 * Checks if you get INROW in a row vertically
	 * @param player the player that's checked
	 * @return true if you win, false otherwise
	 */
	private boolean checkVertical(int player) {	
		int size = this.gameGrid[0].length -1;
		for(int x = size; x >= 0; x--) {
			for(int y = 0; y <= size; y++) {
				if(this.gameGrid[y][x] == EMPTY || this.gameGrid[y][x] != player) {
					currentINROW = 0;
					
				} else if(this.gameGrid[y][x] == player) {
					currentINROW += 1;
				}
				if(checkIfWinner(currentINROW) == true) {
					resetCounters();
					return true;
				}
			}	
		}
		resetCounters();
		return false;
	}
	
	/**
	 * Checks if you get INROW in a row horizontally
	 * @param player the player that's checked
	 * @return true if you win, false otherwise
	 */
	private boolean checkHorizontal(int player) {	
		int size = this.gameGrid[0].length -1;
		for(int y = 0; y <= size; y++) {
			for(int x = size; x >= 0; x--) {
				if(this.gameGrid[y][x] == EMPTY || this.gameGrid[y][x] != player) {
					currentINROW = 0;
					
				} else if(this.gameGrid[y][x] == player) {
					currentINROW += 1;	
				}
				
				if(checkIfWinner(currentINROW) == true) {
					resetCounters();
					return true;
				}	
			}
		}
		resetCounters();
		return false;
	}
	
	/**
	 * Method used by the methods checkDiagonalDownRight and checkDiagonalDownLeft to check if you get INROW in a row
	 * @param x x coordinate in the grid
	 * @param y y coordinate in the grid
	 * @param checkNewRowVariable keeps track of when to switch column/row, i.e the end of the array
	 * @param player the player that's checked
	 * @return 1 if you've reached the end of the array. 2 if you win. 0 otherwise
	 */
	private int checkDiagonally(int x, int y, int checkNewRowVariable, int player) {
		if(this.gameGrid[y][x] == EMPTY || this.gameGrid[y][x] != player) {
			currentINROW = 0;
			corrector += 1;
			
		} else if(this.gameGrid[y][x] == player) {
			currentINROW += 1;
			corrector += 1;
		}
		
		if(checkIfWinner(currentINROW) == true) {
			resetCounters();
			return 2;
		}
		
		if(checkNewRowVariable == checkNewRow) {
			checkNewRow += 1;
			currentINROW = 0;
			corrector = 0;
			return 1;
		}
		return 0;
	}
	
	/**
	 * Checks if you get INROW in row horizonally down to the right
	 * @param player the player that's checked
	 * @return true if you win, false otherwise
	 */
	private boolean checkDiagonalDownRight(int player) {
		int size = this.gameGrid[0].length -1;
		for(int x = size; x >= 0; x--) {
			for(int y = 0; y <= size; y++) {
				currentState = checkDiagonally((x + corrector), y, y, player);
				
				if(currentState == 1) {
					currentState = 0;
					break;
				
				} else if (currentState == 2) {
					currentState = 0;
					return true;
				}		
			}
		}
		resetCounters();
		
		for(int y = size; y >= 0; y--) {
			for(int x = 0; x <= size; x++) {
				currentState = (checkDiagonally(x, (y + corrector), x, player));
				
				if(currentState == 1) {
					break;
					
				} else if (currentState == 2) {
					return true;
				}
			}	
		}
		resetCounters();
		return false;
	}

	/**
	 * Check if you get INROW in a row diagonally down to the left
	 * @param player the player that's checked
	 * @return true if you win, false otherwise
	 */
	private boolean checkDiagonalDownLeft(int player) {
		int size = this.gameGrid[0].length -1;
		for(int x = 0; x <= size; x++) {
			for(int y = 0; y <= size; y++) {
				currentState = checkDiagonally((x - corrector), y, y, player);
				
				if(currentState == 1) {
					break;
					
				} else if (currentState == 2) {
					return true;
				}
			}
		}
		resetCounters();
		checkNewRow = 0;
	
		for(int y = 0; y <= size; y++) {
			for(int x = size; x >= 0; x--) {
				if(this.gameGrid[y + corrector][x] == EMPTY || this.gameGrid[y + corrector][x] != player) {
					currentINROW = 0;
					corrector += 1;
					
				} else if(this.gameGrid[y + corrector][x] == player) {
					currentINROW += 1;
					corrector += 1;
				}
				
				if(checkIfWinner(currentINROW) == true) {
					resetCounters();
					return true;
				}
				
				if(x == checkNewRow) {
					checkNewRow += 1;
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