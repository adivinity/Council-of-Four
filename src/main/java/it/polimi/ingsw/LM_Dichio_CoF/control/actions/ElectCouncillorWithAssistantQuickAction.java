package it.polimi.ingsw.LM_Dichio_CoF.control.actions;

import it.polimi.ingsw.LM_Dichio_CoF.control.Constant;
import it.polimi.ingsw.LM_Dichio_CoF.control.Message;
import it.polimi.ingsw.LM_Dichio_CoF.control.Player;
import it.polimi.ingsw.LM_Dichio_CoF.model.Color;
import it.polimi.ingsw.LM_Dichio_CoF.model.Match;
import it.polimi.ingsw.LM_Dichio_CoF.model.field.AvailableCouncillors;
import it.polimi.ingsw.LM_Dichio_CoF.model.field.Balcony;
import it.polimi.ingsw.LM_Dichio_CoF.model.field.Councillor;
import it.polimi.ingsw.LM_Dichio_CoF.model.field.Field;

import java.util.ArrayList;

public class ElectCouncillorWithAssistantQuickAction extends Action {

	private Balcony chosenBalcony;

	private Color chosenCouncillorColor;
	
    public ElectCouncillorWithAssistantQuickAction(Match match, Player player){

        this.match=match;

        this.player=player;

    }

    @Override
    public boolean preliminarySteps() throws InterruptedException{

        if (player.getAssistant()<Constant.ELECTION_ASSISTANT_COST){

            player.getBroker().println(Message.notEnoughAssistant());

            return false;

        }

        Field field=match.getField();

        Balcony[] arrayBalcony = field.getArrayBalcony();

        player.getBroker().println(Message.chooseBalcony(arrayBalcony));

        chosenBalcony = field.getBalconyFromIndex(player.getBroker().askInputNumber(1, arrayBalcony.length)-1); //-1 for array positioning

        ArrayList<Color> choosableColors = getChoosableColors();

        if (choosableColors.isEmpty()) {

            player.getBroker().println(Message.notEligibleForMove());

            return false;

        }

        player.getBroker().println(Message.askCouncillorColor(choosableColors));

        chosenCouncillorColor=choosableColors.get(player.getBroker().askInputNumber(1, choosableColors.size())-1); //-1 for array positioning

        return true;

    }

    private ArrayList<Color> getChoosableColors() {

        ArrayList<Councillor> availableCouncillors = match.getField().getAvailableCouncillors().getArrayListCouncillor();

        boolean[] seen = new boolean[Constant.COLORS_NUMBER];

        ArrayList<Color> choosableColors = new ArrayList<>();

        for (Councillor councillor: availableCouncillors)
            if (!seen[Color.valueOf(councillor.getColor().toString()).ordinal()]) {

                choosableColors.add(councillor.getColor());

                seen[Color.valueOf(councillor.getColor().toString()).ordinal()]=true;

            }

        return choosableColors;

    }
    
    @Override
    public void execute(){

    	AvailableCouncillors availableCouncillors = match.getField().getAvailableCouncillors();

		Councillor chosenCouncillor = availableCouncillors.removeAvailableCouncillor(chosenCouncillorColor);
		
		chosenBalcony.electCouncillor(chosenCouncillor,availableCouncillors);

    	player.decrementAssistant(Constant.ELECTION_ASSISTANT_COST);
    	
    	resultMsg="Player "+player.getNickname() +" elected a councillor using"
    			+ " an Assistant";

    }

    @Override
    public String getResultMsg(){return resultMsg;}

}