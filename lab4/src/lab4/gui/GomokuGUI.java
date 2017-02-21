package lab4.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import lab4.client.GomokuClient;
import lab4.data.GameGrid;
import lab4.data.GomokuGameState;

/*
 * The GUI class
 */

public class GomokuGUI implements Observer{

	private GomokuClient client;
	private GomokuGameState gamestate;
	private JLabel messageLabel;
	private JButton newGameButton;
	private JButton connectButton;
	private JButton disconnectButton;
	
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
		
		JFrame mainWindow = new JFrame("bla");
		JPanel mainPanel = new JPanel();
		SpringLayout spring = new SpringLayout();
		GamePanel gameGridPanel = new GamePanel(g.getGameGrid()); // NULL = PLACEHOLDER
		JLabel messageLabel = new JLabel();
		JButton newGameButton = new JButton("New Game");
		JButton connectButton = new JButton("Connect");
		JButton disconnectButton = new JButton("Disconnect");
		System.out.println("podkas");
		
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
		
		
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container mainContainer = mainWindow.getContentPane();
		Component newgame = newGameButton;
		Component connect = connectButton;
		Component disconnect = disconnectButton;
		mainContainer.setLayout(spring);
		
		mainContainer.add(newgame);
		mainContainer.add(connect);
		mainContainer.add(disconnect);
		mainContainer.add(gameGridPanel);
		
//		mainContainer.add(disconnectButton);
//		mainContainer.add(connectButton);
//		mainPanel.add(messageLabel);
		
		spring.putConstraint(SpringLayout.WEST, gameGridPanel, 5, SpringLayout.WEST, mainContainer);
		spring.putConstraint(SpringLayout.NORTH, gameGridPanel, 5, SpringLayout.NORTH, mainContainer);
		spring.putConstraint(SpringLayout.WEST, newGameButton, 10, SpringLayout.WEST, mainContainer);
		spring.putConstraint(SpringLayout.NORTH, newGameButton, 320, SpringLayout.NORTH, mainContainer);
		spring.putConstraint(SpringLayout.WEST, connectButton, 10, SpringLayout.EAST, newGameButton);
		spring.putConstraint(SpringLayout.NORTH, connectButton, 320, SpringLayout.WEST, mainPanel);
		spring.putConstraint(SpringLayout.WEST, disconnectButton, 10, SpringLayout.EAST, connectButton);
		spring.putConstraint(SpringLayout.NORTH, disconnectButton, 320, SpringLayout.WEST, mainPanel);
		
		
//		mainPanel.add(gameGridPanel);
		
		
		
		mainWindow.add(mainPanel);
//		mainWindow.pack();
		mainWindow.setSize(500, 400);
		mainWindow.setVisible(true);
		
		
		
		
		
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