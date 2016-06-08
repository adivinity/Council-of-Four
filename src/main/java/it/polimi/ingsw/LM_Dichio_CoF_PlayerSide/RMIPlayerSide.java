package it.polimi.ingsw.LM_Dichio_CoF_PlayerSide;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RMIPlayerSide extends UnicastRemoteObject implements RMIPlayerSideInterface {

	PlayerSide playerSide;
	
	public RMIPlayerSide(PlayerSide playerSide) throws RemoteException {
		this.playerSide=playerSide;
	}

	public void sendString(String string){
		System.out.println(string);
	}
	
	public String receiveString (){
		
		Scanner inCLI = new Scanner(System.in);
		//Mi sa che qui ci vuole un controllo sull'input
		String string = inCLI.nextLine();
		return string;
	}
	
	public void waitForServer(){
		playerSide.waitForServer();
	}
	
	public void configure(){	
		this.playerSide.configure();	
	}
	
	public int getConfigurationsPlayersNumber(){
		return playerSide.getConfigurationsPlayersMaxNumber();
	}
	
	public Object getConfigurationsAsObject(){
		return playerSide.getConfigurationsAsObject();
	}
	
	public void startingMatch(){
		playerSide.startingMatch();
	}
	
}