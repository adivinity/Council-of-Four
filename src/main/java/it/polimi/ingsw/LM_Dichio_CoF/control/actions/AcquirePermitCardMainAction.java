package it.polimi.ingsw.LM_Dichio_CoF.control.actions;

import it.polimi.ingsw.LM_Dichio_CoF.control.Player;
import it.polimi.ingsw.LM_Dichio_CoF.model.Match;

public class AcquirePermitCardMainAction extends Action {

    public AcquirePermitCardMainAction(Match match, Player player){
        this.match=match;
        this.player=player;
    }

    @Override
    public boolean preliminarySteps(){

        return false;
    }

    @Override
    public void execute(){

    }

}