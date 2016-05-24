package it.polimi.ingsw.LM_Dichio_CoF.work.field;

import java.util.Random;

/**
 * Created by Luca on 22/05/16.
 */
public enum BonusName {

    Assistant(3),
    Richness(4),
    Nobility(1),
    Victory(5),
    Cards(3),
    MainMove(1),
    BuiltCityBonus(1),
    FaceUpPermitCard(1),
    OwnedPermitCardBonus(1),
    TwoBuiltDifferentBonus(1);

    private int maxIncrement;

    BonusName(int maxIncrement) {this.maxIncrement=maxIncrement;}

    /*
    public static BonusName getBonusName (int index){return values()[index];}
     */

    public int getMaxIncrement(BonusName bonusName){return bonusName.maxIncrement;}

    public static BonusName getRandomBonusName(){
    	Random random = new Random();
    	return values()[random.nextInt(values().length-4)];
    }
    
    public static BonusName getRandomBonusNameWithoutMainMove(){
    	Random random = new Random();
    	return values()[random.nextInt(values().length-5)];
    }
    

}
