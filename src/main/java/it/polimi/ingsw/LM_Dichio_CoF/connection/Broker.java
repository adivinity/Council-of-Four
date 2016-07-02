package it.polimi.ingsw.LM_Dichio_CoF.connection;

import java.util.ArrayList;

import it.polimi.ingsw.LM_Dichio_CoF.control.GameSide;
import it.polimi.ingsw.LM_Dichio_CoF.control.Player;
import it.polimi.ingsw.LM_Dichio_CoF.model.Configurations;

public class Broker {

	private ConnectionWithPlayerInterface connectionWithPlayer;
	private Player player;
	
	public Broker(ConnectionWithPlayerInterface connectionWithPlayer, Player player){
		this.connectionWithPlayer=connectionWithPlayer;
		this.player=player;
	}
	
	public void login(GameSide gameSide) throws DisconnectedException{
		connectionWithPlayer.login(gameSide);
	}
			
	public Configurations getConfigurations(){
		try {
			return connectionWithPlayer.getConfigurations();
		} catch (DisconnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}return null;
	}
	
	public int askInputNumber(int lowerBound, int upperBound) throws InterruptedException{
		
		interruptedExceptionLauncher();
		
		Thread t = new Thread(new Runnable() {
		     public void run() {
		    	 try {
					connectionWithPlayer.askInputNumber(lowerBound, upperBound);
				} catch (DisconnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     }
		});  
		Object lock = connectionWithPlayer.getLock();
		t.start();
		synchronized (lock) {
			lock.wait();
		}
		return connectionWithPlayer.getIntResult();
	}
	
	public void print(String string) throws InterruptedException{
		
		interruptedExceptionLauncher();
		
		Thread t = new Thread(new Runnable() {
		     public void run() {
		    	 try {
					connectionWithPlayer.print(string);
				} catch (DisconnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     }
		});
		t.start();
	}
	
	public void println(String string) throws InterruptedException{
		CharSequence newLine = "\n";
		if(string.contains(newLine)){
			String[] arrayString = string.split("\n");
			for(String s: arrayString)
				printlnReal(s);
		}else{
			printlnReal(string);
		}
	}
	
	private void printlnReal(String string) throws InterruptedException{
		
		interruptedExceptionLauncher();
		
		try {
			connectionWithPlayer.println(string);
		} catch (DisconnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 
	private void interruptedExceptionLauncher() throws InterruptedException{
		if (Thread.interrupted())
			throw new InterruptedException();
	}
	
	
	public boolean isConnected(){
		/**DEPRECATED
		 */
		return true;
	}
	
}
