package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.control.Player;
import it.polimi.ingsw.utils.Constant;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This is the core of the Model.
 * The field contains regions, cities, balconies, nobility route, king reward tiles.
 *
 * It offers all attributes and methods to access them
 */
public class Field {

	private final Configurations config;

	private City[] arrayCity;

	private List<Integer>[] arrayCityLinks;// array of Integer Lists

	private Balcony[] arrayBalcony;

	private AvailableCouncillors availableCouncillors;

	private final ArrayList<Player> arrayListPlayer;

	private NobilityRoute nobilityRoute;

	private final Region[] arrayRegion = new Region[Constant.REGIONS_NUMBER];

	private final Deque<Integer> kingRewardTiles = new ArrayDeque<>();
	
	private King king;

	/**
	 * The constructor creates cities, regions, assigns nearby cities,
	 * creates balconies and routes
	 *
	 * @param config to assign to the field
	 * @param arrayListPlayer to assign to nobility route
     */
	public Field(Configurations config, List<Player> arrayListPlayer) {
		
		this.config=config;

		this.arrayListPlayer = (ArrayList<Player>) arrayListPlayer;

		createCitiesAndRegions();
		
		assignNearbyCities();

		createBalconies();

		createRoutes();
		
	}

	/**
	 * This method creates arrayCity[config.numberCities] assigning cities their color,
	 * then assigns them to the respective region
	 */
	private void createCitiesAndRegions(){

		kingRewardTiles.push(Constant.FIFTH_KING_REWARD_VICTORY_INCREMENT);
		kingRewardTiles.push(Constant.FOURTH_KING_REWARD_VICTORY_INCREMENT);
		kingRewardTiles.push(Constant.THIRD_KING_REWARD_VICTORY_INCREMENT);
		kingRewardTiles.push(Constant.SECOND_KING_REWARD_VICTORY_INCREMENT);
		kingRewardTiles.push(Constant.FIRST_KING_REWARD_VICTORY_INCREMENT);

		int numberCities = config.getNumberCities();

		arrayCity = new City[numberCities];
		
		int numberCitiesPerRegion = numberCities/Constant.REGIONS_NUMBER;

		CityColor[] arrayCityColor = createArrayCityColor(numberCities);

		for(int itRegion = 0, itColor=0; itRegion<Constant.REGIONS_NUMBER; itRegion++){

			RegionName regionName = RegionName.getRegionNameFromIndex(itRegion);

			City[] arrayCityPerRegion = new City[numberCitiesPerRegion];

			for(int itCityPerRegion=0; itCityPerRegion<numberCitiesPerRegion; itCityPerRegion++){

				int cityIndex = itCityPerRegion + itRegion*numberCitiesPerRegion;

				CityName cityName = CityName.getCityNameFromIndex(cityIndex);

				if (cityName.equals(Constant.KING_CITY_INITIAL)) {
					if (config.isCityBonusRandom()) {
						arrayCity[cityIndex] = new City(config, cityName, regionName, CityColor.Purple);
						this.king = new King(arrayCity[cityIndex]);
					}
					else{
						arrayCity[cityIndex] = new City(config.getArrayListCityBonus()[cityIndex], cityName, regionName, CityColor.Purple);
						this.king = new King(arrayCity[cityIndex]);
					}
				} else {
					if (config.isCityBonusRandom())
						arrayCity[cityIndex] = new City(config, cityName, regionName, arrayCityColor[itColor]);
					else
						arrayCity[cityIndex] = new City(config.getArrayListCityBonus()[cityIndex], cityName, regionName, arrayCityColor[itColor]);
					itColor++;
				}

				arrayCityPerRegion[itCityPerRegion] = arrayCity[cityIndex];

			}

			arrayRegion[itRegion] = new Region (regionName, arrayCityPerRegion, config);

		}

	}

	/**
	 * This method returns an array of randomly orderered CityColors, according to number of cities
	 *
	 * @param numberCities size of the array
	 * @return array of city colors
     */
	private CityColor[] createArrayCityColor(int numberCities){

		CityColor[] arrayCityColor;

		switch (numberCities) {

			case Constant.CITIES_NUMBER_LOW:
				arrayCityColor= Constant.CITIES_COLOR[0].clone();
				break;

			case Constant.CITIES_NUMBER_MEDIUM:
				arrayCityColor= Constant.CITIES_COLOR[1].clone();
				break;

			default: //case Constant.CITIES_NUMBER_HIGH:
				arrayCityColor= Constant.CITIES_COLOR[2].clone();
				break;

		}

		Collections.shuffle(Arrays.asList(arrayCityColor));

		return arrayCityColor;
	}


	/**
	 * This method assigns to every city in arrayCity the cities that are connected to it
	 * according to (cases):
	 * A. the matrix contained in "config" (case config.isCityLinksPreconfigured()=false)
	 * B. the matrix contained in the .txt file (selected through difficulty and number of cities)
	 * This second case is managed in "importCityLinksMatrix"
	 */
	private void assignNearbyCities(){
		
		int[][] cityLinksMatrix;
		if(config.isCityLinksPreconfigured()){
			cityLinksMatrix = importCityLinksMatrix(config.getDifficulty(), arrayCity.length);
		}else{
			cityLinksMatrix = config.getCityLinksMatrix();
		}

		arrayCityLinks= new List[arrayCity.length];
		for (int i=0; i<arrayCityLinks.length; i++)
			arrayCityLinks[i]= new LinkedList<>();

		for(int row=0; row<arrayCity.length; row++){
			
        	ArrayList<City> arrayListCityConnected = new ArrayList<>();
			for (int column=0; column<arrayCity.length; column++){
        		if(cityLinksMatrix[row][column]==1){
        			arrayListCityConnected.add(arrayCity[column]);
					arrayCityLinks[row].add(column);
        		}	
        	}

			 //ArrayList to simplify the procedure
			City[] arrayCityConnected = new City[arrayListCityConnected.size()];
			arrayListCityConnected.toArray(arrayCityConnected);
			arrayCity[row].setNearbyCity(arrayCityConnected);
			
		}

	}

	/**
	 * This method imports cityLinksMatrix from the respective .txt file according to parameters passed:
	 * "difficulty" is the first letter of the file, "numberCities" the second one
	 *
	 * @param difficulty first letter of the filename to open
	 * @param numberCities second and third letter of the filename to open
     * @return city links matrix
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

	/* This method creates four balconies and fills them with councillors */
	private void createBalconies() {
		//Create one (AND ONLY FOR ALL THE GAME) instance of AvailableCouncillors
		availableCouncillors = new AvailableCouncillors();

		arrayBalcony = new Balcony[Constant.BALCONIES_NUMBER];

		arrayBalcony[0] = new Balcony (availableCouncillors, RegionName.Sea.toString()+"Balcony");
		arrayBalcony[1] = new Balcony (availableCouncillors, RegionName.Hill.toString()+"Balcony");
		arrayBalcony[2] = new Balcony (availableCouncillors, RegionName.Mountain.toString()+"Balcony");
		arrayBalcony[3] = new Balcony (availableCouncillors, "KingBalcony");
	}
	
	/* This method creates the three routes and assign them to the field's variables*/
	private void createRoutes(){
		
		if(config.isNobilityBonusRandom()){
			this.nobilityRoute = new NobilityRoute(arrayListPlayer, config.getNobilityBonusNumber());
		}else{
			this.nobilityRoute = new NobilityRoute(arrayListPlayer);
		}
		nobilityRoute.setField(this);

	}
	
	public City[] getArrayCity(){
		return arrayCity;
	}
	public List<Integer>[] getArrayCityLinks() {return arrayCityLinks;}

	/**
	 * @param index of the region to return
	 * @return region
     */
	public Region getRegionFromIndex (int index){
		return arrayRegion[index];
	}

	public Balcony[] getArrayBalcony() {return arrayBalcony;}

	/**
	 * @param index of the balcony to return
	 * @return balcony
     */
	public Balcony getBalconyFromIndex(int index){return getArrayBalcony()[index];}

	public AvailableCouncillors getAvailableCouncillors() {return availableCouncillors; }

	public NobilityRoute getNobilityRoute() {return nobilityRoute;}

	public King getKing(){return king;}

	public Deque<Integer> getKingRewardTiles() {
		return kingRewardTiles;
	}
}
