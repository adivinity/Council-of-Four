package it.polimi.ingsw.LM_Dichio_CoF.work.field;

public class Bonus {

	BonusName bonusName;
	int increment;
		
	public BonusName getBonusName() {
		return bonusName;
	}

	public int getIncrement() {
		return increment;
	}
	
	public Bonus(){}

	/* If String in input corresponds to a bonusName, then it creates the corresponding bonus
	  with specified increment */
	public Bonus(String bonusName, int increment){
		
		if(BonusName.containString(bonusName)){
			 this.bonusName = BonusName.stringToBonusName(bonusName);
			 this.increment=increment;
		}
		
	}
	
	/* This method checks if BonusName of calling bonus is present in arrayBonus (parameter) */
	public boolean bonusNameIsIn(Bonus[] arrayBonus){
		
		for(int i=0; i<arrayBonus.length; i++){
			if(arrayBonus[i]!=null)
				if(bonusName==arrayBonus[i].getBonusName())
					return true;
		}
		return false;
	}

	public void setBonusName(BonusName bonusName) {
		this.bonusName = bonusName;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
	}
	
}
