package lab4.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

/**
 * Represents the GUI of the game.
 * @author Robin
 *
 */

public class GomokuGUI implements Observer {

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
		GamePanel gameGridPanel = new GamePanel(g.getGameGrid());
		this.messageLabel = new JLabel();
		this.newGameButton = new JButton("New Game");
		this.connectButton = new JButton("Connect");
		this.disconnectButton = new JButton("Disconnect");
		
		
		
		gameGridPanel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX() / GamePanel.UNIT_SIZE;
				int y = e.getY() / GamePanel.UNIT_SIZE; // FRÅGA OM MAN KAN ÄNDRA VISIBILITY PÅ UNIT_SIZE I GAMEPANEL SÅ MAN
										// KAN ANPASSA SIG EFTER STORLEKEN PÅ DEN
				
				gamestate.move(x, y);
				
				
			}
		
		} );
		
		
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
		Component message = messageLabel;
		mainContainer.setLayout(spring);
		
		mainContainer.add(newgame);
		mainContainer.add(connect);
		mainContainer.add(disconnect);
		mainContainer.add(gameGridPanel);
		mainContainer.add(message);
		
		
		spring.putConstraint(SpringLayout.WEST, gameGridPanel, 5, SpringLayout.WEST, mainContainer);
		spring.putConstraint(SpringLayout.NORTH, gameGridPanel, 5, SpringLayout.NORTH, mainContainer);
		spring.putConstraint(SpringLayout.WEST, newGameButton, 10, SpringLayout.WEST, mainContainer);
		spring.putConstraint(SpringLayout.NORTH, newGameButton, 10, SpringLayout.SOUTH, gameGridPanel);
		spring.putConstraint(SpringLayout.WEST, connectButton, 10, SpringLayout.EAST, newGameButton);
		spring.putConstraint(SpringLayout.NORTH, connectButton, 10, SpringLayout.SOUTH, gameGridPanel);
		spring.putConstraint(SpringLayout.WEST, disconnectButton, 10, SpringLayout.EAST, connectButton);
		spring.putConstraint(SpringLayout.NORTH, disconnectButton, 10, SpringLayout.SOUTH, gameGridPanel);
		spring.putConstraint(SpringLayout.WEST, messageLabel, 10, SpringLayout.WEST, mainContainer);
		spring.putConstraint(SpringLayout.NORTH, messageLabel, 10, SpringLayout.SOUTH, newGameButton);
		
		
		mainWindow.add(mainPanel);
		//mainWindow.pack();
		mainWindow.setSize(gamestate.DEFAULT_SIZE*GamePanel.UNIT_SIZE+ 45, gamestate.DEFAULT_SIZE*GamePanel.UNIT_SIZE+ 135);
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