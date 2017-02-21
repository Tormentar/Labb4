package lab4.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import lab4.client.GomokuClient;
import lab4.data.GameGrid;
import lab4.data.GomokuGameState;

/*
 * The GUI class
 */

public class GomokuGUI implements Observer{

	private GomokuClient client;
	private GomokuGameState gamestate;
	
	/**
	 * The constructor
	 * 
	 * @param g   The game state that the GUI will visualize
	 * @param c   The client that is responsible for the communication
	 */
	public GomokuGUI(GomokuGameState g, GomokuClient c){
		this.client = c;
		this.gamestate = g;
		client.addObserver(this);
		gamestate.addObserver(this);
		
		JFrame mainWindow = new JFrame();
		GamePanel gameGridPanel = new GamePanel(g.getGameGrid()); // NULL = PLACEHOLDER
		JLabel messageLabel = new JLabel();
		JButton newGameButton = new JButton("New Game");
		JButton connectButton = new JButton("Connect");
		JButton disconnectButton = new JButton("Disconnect");
		
		newGameButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent a) { 
			    g.newGame();
			  } 
			} );
		
		disconnectButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent b) { 
			    g.disconnect();
			  } 
			} );
		
		connectButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent c) { 
			    ConnectionWindow joinGame = new ConnectionWindow(client);
			  } 
			} );
			
	}

	
	public void update(Observable arg0, Object arg1) {
		
		// Update the buttons if the connection status has changed
		if(arg0 == client){
			if(client.getConnectionStatus() == GomokuClient.UNCONNECTED){
				connectButton.setEnabled(true);
				newGameButton.setEnabled(false);
				disconnectButton.setEnabled(false);
			}else{
				connectButton.setEnabled(false);
				newGameButton.setEnabled(true);
				disconnectButton.setEnabled(true);
			}
		}
		
		// Update the status text if the gamestate has changed
		if(arg0 == gamestate){
			messageLabel.setText(gamestate.getMessageString());
		}
		
	}
	
}