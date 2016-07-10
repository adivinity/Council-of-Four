package it.polimi.ingsw.client.connection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.client.PlayerSide;

public class RMIPlayerSide extends UnicastRemoteObject implements RMIPlayerSideInterface {

	private PlayerSide playerSide;
	
	public RMIPlayerSide(PlayerSide playerSide) throws RemoteException {
		this.playerSide=playerSide;
	}

	public void login(){
		playerSide.login();
	}
	
	public String getNickname(){
		return playerSide.getNickname();
	}
	
	public boolean isCustomConfig(){
		return playerSide.isCustomConfig();
	}
	
	public Object getConfigurationsAsObject(){
		return playerSide.getConfigurationsAsObject();
	}
	
	public void print(String string){
		System.out.print(string);
	}
	
	public void println(String string){
		System.out.println(string);
	}
	
	public int inputNumber(int lowerBound, int upperBound){
		return playerSide.getInputHandler().inputNumber(lowerBound, upperBound);
	}
	
	public void stopInputNumber(){
		playerSide.getInputHandler().stopInputNumber();
	}
	
	
}
