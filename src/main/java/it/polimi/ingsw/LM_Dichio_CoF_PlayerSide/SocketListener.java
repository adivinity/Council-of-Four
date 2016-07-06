package it.polimi.ingsw.LM_Dichio_CoF_PlayerSide;

public class SocketListener {

	String message;
	PlayerSide playerSide;
	SocketConnection socketConnection;
	boolean inputStop;
	
	public SocketListener(PlayerSide playerSide, SocketConnection socketConnection){
		
		this.socketConnection = socketConnection;
		this.playerSide=playerSide;
		this.inputStop=false;
		
		while(true){
			message = socketConnection.receiveStringFS();
			switch(message){
			
			case	"SOCKETlogin":
				playerSide.login();
				break;
				
			case	"SOCKETisCustomConfig":
				socketConnection.sendStringTS(String.valueOf(playerSide.isCustomConfig()));
				break;
				
			case	"SOCKETgetConfigurationsAsObject":
				socketConnection.sendObjectTS(playerSide.getConfigurationsAsObject());
				break;
			
			case	"SOCKETinputNumber":{
				int lowerBound = Integer.parseInt(socketConnection.receiveStringFS());
				int upperBound = Integer.parseInt(socketConnection.receiveStringFS());
				new HandleInputNumber(lowerBound, upperBound).start();
				break;
			}
			
			case	"SOCKETstopInputNumber":
				inputStop=true;
				playerSide.getInputHandler().stopInputNumber();
				break;
			
			case	"SOCKETprint":
				System.out.print(socketConnection.receiveStringFS());
				break;
			
			case	"SOCKETprintln":
				System.out.println(socketConnection.receiveStringFS());
				break;
				
			default	:
				System.out.println("Error receiving String through socket");
				break;
			}
		}
	}
	
	class HandleInputNumber extends Thread{
		int lowerBound, upperBound;
		public HandleInputNumber(int lowerBound,int upperBound){
			this.lowerBound=lowerBound;
			this.upperBound=upperBound;
		}
		public void run(){
			int result = playerSide.getInputHandler().inputNumber(lowerBound, upperBound);
			if(!inputStop)
				socketConnection.sendStringTS(String.valueOf(result));
			else
				inputStop=false;
		}
	}
	
}
