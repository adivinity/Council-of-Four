package it.polimi.ingsw.server.model;

public enum CityName {
	
	// Constant.KING_CITY_INITIAL is a city of this enum
	
	A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z;
	
	public static CityName getCityNameFromIndex(int index){ return values()[index]; }

}
