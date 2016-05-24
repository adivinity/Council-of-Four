package it.polimi.ingsw.LM_Dichio_CoF.work;

import it.polimi.ingsw.LM_Dichio_CoF.work.field.NameCity;

public class Constant {
	
	public static final int PORT_SOCKET=3000;
	public static final int NUMBER_COUNCILERS_PER_BALCONY=4;
	public static final int NUMBER_BALCONIES=4;
	public static final int NUMBER_OF_COLORS=6; // COUNCILORS
	public static final int TOTAL_NUMBER_COUNCILORS=24; //IT HAS TO BE A MULTIPLE OF THREE
	public static final int NUMBER_INITIAL_POLITIC_CARDS=6;
	public static final int INITIAL_RICHNESS_FIRST_PLAYER=10;
	public static final int NUMBER_OF_REGIONS=3;
	public static final int NUMBER_FACE_UP_PERMIT_CARD_PER_REGION=2;
	public static final int NUMBER_BONUS_PER_PERMIT_CARD=2;
	public static final int MAX_CITIES_PER_PERMIT_CARD=3;
	public static final int MIN_CITIES_PER_PERMIT_CARD=1;
	public static final int MIN_CITIES_PER_REGION=5;
	public static final int MAX_RICHNESS=20;
	public static final int MAX_NOBILITY=20;
	public static final int MAX_VICTORY=100;
	
	public static final NameCity INITIAL_KING_CITY= NameCity.J;

	public static final int[] CITIES_BLUE_NUMBER = {2,2,3};
	public static final int[] CITIES_BRONZE_NUMBER = {3,4,4};
	public static final int[] CITIES_GOLD_NUMBER = {5,6,6};
	public static final int[] CITIES_PURPLE_NUMBER = {1,1,1};
	public static final int[] CITIES_SILVER_NUMBER = {4,5,5};
	public static final int[] CITIES_RED_NUMBER = {0,0,2};

	public static final int CITIES_NUMBER_LOW = 15;
	public static final int CITIES_NUMBER_MEDIUM = 18;
	public static final int CITIES_NUMBER_HIGH = 21;
}
