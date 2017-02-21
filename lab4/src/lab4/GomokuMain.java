package lab4;

import lab4.data.*;
import lab4.client.*;
import lab4.gui.*;



public class GomokuMain {

	
	public static void main(String[] args) {
		
		if(args.length != 1) {
			System.out.println("You forgot to enter an argument");
			System.exit(-1);
			
		}

		
		int port = Integer.parseInt(args[0]);
		
		
		if(args.equals(null)) {
			System.out.println("The port you entered didnt exist, default port used");
			port = 5012;
			
		}
		
		GomokuClient client = new GomokuClient(port);
		GomokuGameState gameState = new GomokuGameState(client);
		System.out.println("das");
		GomokuGUI gui = new GomokuGUI(gameState, client);
		System.out.println("odka");
		
		
		
	}

}