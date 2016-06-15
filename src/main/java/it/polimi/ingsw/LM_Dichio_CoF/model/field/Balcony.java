package it.polimi.ingsw.LM_Dichio_CoF.model.field;

import java.util.ArrayList;

import it.polimi.ingsw.LM_Dichio_CoF.control.Constant;

public class Balcony {

	private String nameBalcony;

	private ArrayList<Councillor> arrayListCouncillor = new ArrayList<>();

	/* The constructor assigns the name and adds specified councillors to the balcony*/
	public Balcony(AvailableCouncillors availableCouncillors, String nameBalcony){
		
		this.nameBalcony=nameBalcony;
		
		/*This for cycle permits to remove a councilor from availableCouncillors
		 * and put it into arrayListCouncillor.
		 * It is random, because during the creation of AvailableCouncillors (which cointains
		 * all councillors initially - in the game 24) the arrayList is shuffled
		 */
		
		for(int i = 0; i< Constant.COUNCILLORS_PER_BALCONY_NUMBER; i++){
			
			Councillor councillor = availableCouncillors.removeAvailableCouncillor();
			arrayListCouncillor.add(councillor);
		}
		
	}

	/* This method puts the councillor passed as parameter on the leftmost
	   position, slides the other councillors on the balcony,
	   adds the fallen councillor to available councillors */
	public void electCouncillor(Councillor councillor, AvailableCouncillors availableCouncillors) {
		arrayListCouncillor.add(0,councillor);
		availableCouncillors.addAvailableCouncillor(arrayListCouncillor.remove(arrayListCouncillor.size()-1));
	}

	public ArrayList<Councillor> getArrayListCouncillor(){
		return arrayListCouncillor;
	}

	public String getNameBalcony() {
		return nameBalcony;
	}

}