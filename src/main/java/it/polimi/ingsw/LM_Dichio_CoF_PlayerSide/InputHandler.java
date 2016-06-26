package it.polimi.ingsw.LM_Dichio_CoF_PlayerSide;

import java.util.ArrayList;
import java.util.Scanner;

import it.polimi.ingsw.LM_Dichio_CoF.model.field.CityName;

public class InputHandler {

	public static int inputNumber(int lowerBound, int upperBound){ //TODO throws RemoteException + spostare nella classe della CLI

		Scanner in = new Scanner(System.in);
		int inputNumber;
		boolean eligibleInput=false;

		do {
			while(!in.hasNextInt()){
				System.out.println("Insert an integer value!");
				in.nextLine();
			}
			inputNumber=in.nextInt();
			in.nextLine();

			if(inputNumber>=lowerBound && inputNumber<=upperBound)
				eligibleInput=true;
			else
				System.out.println("Insert a value between "+ lowerBound
									+ " and " + upperBound);
		} while(!eligibleInput);

		return inputNumber;

	}
	public static Character[] inputCity (CityName currentCity, CityName lastCity){
		Scanner in = new Scanner(System.in);
		String string;
		boolean correct=true;
		ArrayList <Character> temp;
		ArrayList <Character> outChar = new ArrayList<>();
		for (int i=lastCity.ordinal()+1; i<=CityName.Z.ordinal();i++){
			outChar.add(CityName.getCityNameFromIndex(i).toString().charAt(0));
		}
		
		do{
			correct=true;
			string = in.nextLine();
			char [] chars = string.toUpperCase().replaceAll("\\s+", "").toCharArray();
			temp=new ArrayList <>();
			
			if(chars.length==0){
				System.out.println("You have inserted a void input");
				System.out.println("Please insert a valid set of cities");
				correct=false;
			}else{
				for(int i=0; i<chars.length && correct; i++){
					char ch = chars[i];
					int enumPosition=(CityName.valueOf(String.valueOf(ch))).ordinal();
					if(temp.contains(ch)){
						System.out.println("You have inserted the same city more than once.");
						System.out.println("Please insert another set of cities");
						correct=false;
					}else if (enumPosition==currentCity.ordinal()){
						System.out.println("You have inserted the same city of the started.");
						System.out.println("Please insert another set of cities");
						correct=false;
					}else if(outChar.contains(ch)){
						System.out.println("You have inserted a city out of range");
						System.out.println("Please insert another set of cities");
						correct=false;	
					}else{
						temp.add(ch);
					}
				}
			}
		}while(!correct);
		
		Character [] result = temp.toArray(new Character [temp.size()]);
		return result;
	}
}
