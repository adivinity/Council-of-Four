package it.polimi.ingsw.LM_Dichio_CoF.work.field;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import it.polimi.ingsw.LM_Dichio_CoF.work.*;

public class Field {

	Configurations config;
	Region[] arrayRegion;
	City[] arrayCity;
	Balcony[] arrayBalcony;
	AvailableCouncilor availableCouncilor;
	Route route;
	King king;
	
	public Field(Configurations config, ArrayList<Player> arrayListPlayer) {
		
		this.config=config;
		
		createCitiesAndRegions();
		
		assignNearbyCities();

		//Create one (AND ONLY FOR ALL THE GAME) instance for the Available Councilor
		availableCouncilor = new AvailableCouncilor();
		
		arrayBalcony = new Balcony[Constant.BALCONIES_NUMBER];
		
		arrayBalcony[0] = new Balcony (availableCouncilor, NameRegion.Sea.toString()+"Balcony");
		arrayBalcony[1] = new Balcony (availableCouncilor, NameRegion.Hill.toString()+"Balcony");
		arrayBalcony[2] = new Balcony (availableCouncilor, NameRegion.Mountain.toString()+"Balcony");
		arrayBalcony[3] = new KingBalcony (availableCouncilor, "KingBalcony");
		
		
		
		//GESTIONE PERCORSI
		
		
	}

	/* This method creates arrayCity[config.numberCities] assigning cities their color,
	 	then assigns them to the respective region  */
	private void createCitiesAndRegions(){

		int numberCities = config.getNumberCities();
		int numberCitiesPerRegion = numberCities/Constant.REGIONS_NUMBER;

		arrayCity = new City[numberCities];
		arrayRegion = new Region[Constant.REGIONS_NUMBER];

		City[] arrayCityPerRegion = new City[numberCitiesPerRegion];

		CityColor[] arrayCityColor = new CityColor[numberCities];

		switch (numberCities) {

			case Constant.CITIES_NUMBER_LOW:
				arrayCityColor= Constant.CITIES_COLOR[0].clone();
				break;

			case Constant.CITIES_NUMBER_MEDIUM:
				arrayCityColor= Constant.CITIES_COLOR[1].clone();
				break;

			case Constant.CITIES_NUMBER_HIGH:
				arrayCityColor= Constant.CITIES_COLOR[2].clone();
				break;

		}

		Collections.shuffle(Arrays.asList(arrayCityColor));

		int itColor=0;

		for(int itRegion = 0; itRegion<Constant.REGIONS_NUMBER; itRegion++){
			NameRegion nameRegion = NameRegion.getNameRegion(itRegion);

			for(int itCity=0; itCity<numberCitiesPerRegion; itCity++){

				CityName cityName = CityName.getNameCity(itCity + itRegion*numberCitiesPerRegion);

				if (cityName.equals(Constant.KING_CITY_INITIAL)) {
					arrayCity[itCity + itRegion * numberCitiesPerRegion] = new City(config, cityName, nameRegion, CityColor.Purple);
				}
				else {
					arrayCity[itCity + itRegion * numberCitiesPerRegion] = new City(config, cityName, nameRegion, arrayCityColor[itColor]);
					itColor++;
				}
				arrayCityPerRegion[itCity] = arrayCity[itRegion*numberCitiesPerRegion];
			}

			arrayRegion[itRegion] = new Region (nameRegion, arrayCityPerRegion, config);

		}

	}

	
	/*
	 * This method assigns to every city of the arrayCity the cities that are connected to it
	 * according to the matrix imported from .txt file
	 */
	private void assignNearbyCities(){
		
		int[][] cityLinksMatrix = importCityLinksMatrix(config.getDifficulty(), arrayCity.length);
		
		for(int row=0; row<arrayCity.length; row++){
			
        	ArrayList<City> arrayListCityConnected = new ArrayList<City>();
			for (int column=0; column<arrayCity.length; column++){
        		if(cityLinksMatrix[row][column]==1){
        			arrayListCityConnected.add(arrayCity[column]);
        		}	
        	}
			/*
			 * ArrayList to simplify the procedure
			 */
			City[] arrayCityConnected = new City[arrayListCityConnected.size()];
			arrayListCityConnected.toArray(arrayCityConnected);
			arrayCity[row].setNearbyCity(arrayCityConnected);
			
		}  

	}
	
	/*
	 * This method imports cityLinksMatrix from the respective .txt file according to parameters passed:
	 * "difficulty" is the first letter of the file, "numberCities" the second one
	 */
	private int[][] importCityLinksMatrix(char difficulty , int numberCities){
		
		int[][]cityLinksMatrix = new int[numberCities][numberCities];
		
		try {

			FileReader inFile = new FileReader("./src/nearbyCities/"+ difficulty + numberCities+".txt");
			Scanner in = new Scanner(inFile);
			
			for(int i=0; i<numberCities; i++) {
	            for(int j=0; j<numberCities; j++){
	            	String read = in.next();
	            	if (read.equals("1")){
	            		cityLinksMatrix[i][j]=1;
	            		/*
	            		 * This second assignment is for making the matrix specular,
	            		 * because in the txt file it is only upper triangular set
	            		 */
	            		cityLinksMatrix[j][i]=1;
	            	}
	            }
	            	
	        }
			
	        in.close();
	        
	        try {
				inFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		return cityLinksMatrix;
		
	}

	public City[] getArrayCity(){
		return arrayCity;
	}
	
}
