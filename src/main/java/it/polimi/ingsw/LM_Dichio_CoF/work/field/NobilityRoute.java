package it.polimi.ingsw.LM_Dichio_CoF.work.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.polimi.ingsw.LM_Dichio_CoF.work.Constant;
import it.polimi.ingsw.LM_Dichio_CoF.work.Player;

public class NobilityRoute implements Route{

	private Map <Player,Integer> nobilityMap = new HashMap<>();
	private NobilityRouteCell [] arrayNobilityRouteCell = new NobilityRouteCell[Constant.NOBILITY_MAX];

	/* The constructor assigns creates nobilityMap with every player
	   starting at 0. Then it creates arrayNobilityRouteCell */
	public NobilityRoute(ArrayList<Player> arrayListPlayer){

		for (Player anArrayListPlayer : arrayListPlayer) {
			nobilityMap.put(anArrayListPlayer, 0);
		}
		
		for (int i = 0; i<Constant.NOBILITY_MAX; i++){
			arrayNobilityRouteCell[i] = new NobilityRouteCell(i); 
		}
		//Collections.shuffle(Arrays.asList(arrayNobilityRouteCell));
	}

	/* This method increases/decreases the specified player's nobilityMap of the increment specified */
	public void movePlayer(int increment, Player player){
		int oldValue = nobilityMap.get(player);
		if (oldValue+increment < Constant.NOBILITY_MAX)
			nobilityMap.replace(player, oldValue+increment);
		else
			nobilityMap.replace(player, Constant.NOBILITY_MAX);
	}
	
	public int getPosition(Player player){
		return nobilityMap.get(player);
	}

}
