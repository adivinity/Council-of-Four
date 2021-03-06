package it.polimi.ingsw.server.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.server.control.actions.Action;
import it.polimi.ingsw.server.control.actions.MarketBuyAction;
import it.polimi.ingsw.server.control.actions.MarketSellAction;
import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.SellingObject;
import it.polimi.ingsw.utils.Constant;
import it.polimi.ingsw.utils.ControlTimer;
import it.polimi.ingsw.utils.Message;

/**
 * This class controls the market operations. It interacts with the players asking them what they
 * want to sell and then what they want to buy.
 * When a player sells an object and this one is added to an ArrayList of SellingObject, an useful
 * abstract class that is in the model package.
 */
 
public class ControlMarket {
 
    private final ArrayList <Player> players;
    
    private final ArrayList<SellingObject> arrayListSellingObjects = new ArrayList<>();
    private final Match match;
    private Action action;
    
    private int turn;
    private Player player;
   
    public ControlMarket (List<Player> arrayListPlayer, Match match){
        this.players = new ArrayList<>(arrayListPlayer);
        this.match=match;
    }
   
    /**
     * There is a control on the number of the players, if there are more than two players, 
     * the market starts and notifies all the players.
     */
    
    public void startMarket (){
        
    	if(atLeastTwoPlayersConnected()){
    		Broadcast.printlnBroadcastAll(Message.marketHasStarted(), players);
    		startSelling();
    		
    		if(atLeastTwoPlayersConnected())
    			startBuying();
    		
    		Broadcast.printlnBroadcastAll(Message.markedHasFinished(), players);
    	}
        
    } 
    
    /**
     * This method starts the selling phase and manages the turns of the players. 
     * 
     * For each player it creates a new MarketSellAction. 
     * Then it launches the threadMarketHandler that manages the timer and the
     * eventual disconnection of the player.
     */
    private void startSelling(){
    	
    	turn = 0;
    	Broadcast.printlnBroadcastAll(Message.sellingPhase(), players);
    	do {
    		
    		player=players.get(turn);
    		
    		if(player.isConnected()){
    			Broadcast.printlnBroadcastOthers(Message.turnOf(player), players, player);
    			action = new MarketSellAction(match, player);
            	turnMarketHandler();
            	
    		}else if(!player.isMessageDisconnectedSent()){
        		Broadcast.printlnBroadcastOthers(Message.playerHasBeenKickedOut(player), players, player);
        		player.setMessageDisconnectedSent(true);
        	}
    		
            turn++;
        }
        while ((turn % players.size()) != 0);
    	
    }
   
    
    /**
     * This method starts the buying phase and manages the turns of the players.
     *  
     * For each player it creates a new MarketByuAction. 
     * Then it launches the threadMarketHandler that manages the timer and the
     * eventual disconnection of the player.
     */
    private void startBuying (){
    	
        turn = 0;
        ArrayList <Player> playersBuyPhase = new ArrayList <> (players);
        Collections.shuffle(playersBuyPhase);
        Broadcast.printlnBroadcastAll(Message.buyingPhase(), players);
       
        do {
        	
        	player = playersBuyPhase.get(turn);
        	
        	if(player.isConnected()){
        		Broadcast.printlnBroadcastOthers(Message.turnOf(player), players, player);
	            action = new MarketBuyAction(match, player);
	            turnMarketHandler();
	            
        	}else if(!player.isMessageDisconnectedSent()){
        		Broadcast.printlnBroadcastOthers(Message.playerHasBeenKickedOut(player), playersBuyPhase, player);
        		player.setMessageDisconnectedSent(true);
        	}
        	
            turn++;
        }
        while ((turn % playersBuyPhase.size()) != 0);      
    }
    
    
    /**
     * This method handles the whole turn of the player.
     * 
     * It starts a thread whose task is to call the methods to communicate with the corresponding client.
     * Then it waits until the player has completed the action or the timer to perform it expires.
     * 
	 * At last it checks the connection with the client and, if it has disconnected, it advises the 
	 * other players of that.
     */
    private void turnMarketHandler(){
    	
    	Thread turnMarketThread = new Thread(new Runnable(){
    		public void run() {
	    		try {
	    			player.getBroker().println(Message.yourTurn(Constant.TIMER_SECONDS_MARKET_ACTION));
					if (action.preliminarySteps()){
					    action.execute();
					    player.getBroker().println(Message.actionCompleted());
			    		Broadcast.printlnBroadcastOthers(action.getResultMsg(), players, player);
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
    		}
		});
    	turnMarketThread.start();
    	
    	new ControlTimer().waitForThreadUntilTimerExpires(turnMarketThread, Constant.TIMER_SECONDS_MARKET_ACTION);
    	
    	if(turnMarketThread.isAlive()){
    		turnMarketThread.interrupt();
    		while(turnMarketThread.isAlive()) 
    			;
    		player.getBroker().println(Message.turnOverBecauseTimer());
    	}
    	
    	if(!player.isConnected()){
    		Broadcast.printlnBroadcastOthers(Message.playerHasBeenKickedOut(player), players, player);
    		player.setMessageDisconnectedSent(true);
    	}
    	
    }
    
    private ArrayList<Player> getPlayersConnected(){
		ArrayList<Player> ps = new ArrayList<>();
		for(Player p: players){
			if(p.isConnected())
				ps.add(p);
		}
		return ps;
	}
    
    private boolean atLeastTwoPlayersConnected(){
    	ArrayList<Player> playersConnected = getPlayersConnected();
		return !((playersConnected == null) || (playersConnected.size() == 1));
	}
    
    public ArrayList<SellingObject> getArrayListSellingObjects() {
        return arrayListSellingObjects;
    }
   
    public void addSellingObjectToArrayList(SellingObject sellingObject){
        arrayListSellingObjects.add(sellingObject);
    }
   
}

