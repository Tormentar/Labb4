/*
 * Created on 2007 feb 8
 */
package lab4.data;

import java.util.Observable;
import java.util.Observer;

import lab4.client.GomokuClient;

/**
 * Represents the state of the game
 * @author David
 *
 */

public class GomokuGameState extends Observable implements Observer{

   
	// Game variables
	public final int DEFAULT_SIZE = 15;
	private GameGrid gameGrid;
	private GomokuClient client;
	private String message;
    //Possible game states
	private final int NOT_STARTED = 0;
	private final int MY_TURN = 1;
    private final int OTHER_TURN = 2;
    private final int FINISHED = 3;
    private int currentState;
	
	/**
	 * The constructor
	 * 
	 * @param gc The client used to communicate with the other player
	 */
	public GomokuGameState(GomokuClient gc){
		client = gc;
		client.addObserver(this);
		gc.setGameState(this);
		currentState = NOT_STARTED;
		gameGrid = new GameGrid(DEFAULT_SIZE);
		
	}
	

	/**
	 * Returns the message string
	 * 
	 * @return the message string
	 */
	public String getMessageString(){
		return message;
	}
	
	/**
	 * Returns the game grid
	 * 
	 * @return the game grid
	 */
	public GameGrid getGameGrid(){
		return gameGrid;
	}

	/**
	 * This player makes a move at a specified location
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void move(int x, int y){
		if (currentState == MY_TURN && gameGrid.move(x, y, gameGrid.ME) == true) {
			client.sendMoveMessage(x, y);
			currentState = OTHER_TURN;
			message = "Move successful";
			if (gameGrid.isWinner(gameGrid.ME) == true) {
				currentState = FINISHED;
				message = "You Won!";
				gameGrid.clearGrid();
			}
			setChanged();
			notifyObservers();
			return;
		}
		if (currentState == OTHER_TURN) {
			message = "Wait for your turn to move.";
			setChanged();
			notifyObservers();
			return;
		}
		if(gameGrid.move(x, y, gameGrid.ME) == false || gameGrid.move(x, y, gameGrid.OTHER) == false) {
			message = "Cannot Place";
		}
		message = "No game started";
		setChanged();
		notifyObservers();
	}
	


	/**
	 * Starts a new game with the current client
	 */
	public void newGame(){
		gameGrid.clearGrid();
		currentState = OTHER_TURN;
		message = "New Game Starting..";
		client.sendNewGameMessage();
		setChanged();
		notifyObservers();
				
	}
	
	/**
	 * Other player has requested a new game, so the 
	 * game state is changed accordingly
	 */
	public void receivedNewGame(){
		currentState = MY_TURN;
		gameGrid.clearGrid();
		message = "Received New Game..";
		setChanged();
		notifyObservers();
	}
	
	/**
	 * The connection to the other player is lost, 
	 * so the game is interrupted
	 */
	public void otherGuyLeft(){
		currentState = FINISHED;
		gameGrid.clearGrid();
		message = "Opponent Left.";
		setChanged();
		notifyObservers();
	}
	
	/**
	 * The player disconnects from the client
	 */
	public void disconnect(){
		currentState = FINISHED;
		gameGrid.clearGrid();
		client.disconnect();
		setChanged();
		message = "Disconnected";
		notifyObservers();
		
	}
	
	/**+
	 * The player receives a move from the other player
	 * 
	 * @param x The x coordinate of the move
	 * @param y The y coordinate of the move
	 */
	public void receivedMove(int x, int y){
		if (currentState == OTHER_TURN && gameGrid.move(x, y, gameGrid.OTHER) == true) {
			System.out.println("hee2");
			client.sendMoveMessage(x, y);
			currentState = MY_TURN;
			message = "Your turn";
			if (gameGrid.isWinner(gameGrid.OTHER) == true) {
				currentState = FINISHED;
				message = "You Lose!";
				gameGrid.clearGrid();
			}
		}
		setChanged();
		notifyObservers();
		
	}
	/**
	 * Updates between OTHER and ME.
	 */
	public void update(Observable o, Object arg) {
		
		switch(client.getConnectionStatus()){
		case GomokuClient.CLIENT:
			message = "Game started, it is your turn!";
			currentState = MY_TURN;
			break;
		case GomokuClient.SERVER:
			message = "Game started, waiting for other player...";
			currentState = OTHER_TURN;
			break;
		}
		setChanged();
		notifyObservers();
		
		
	}
	
}