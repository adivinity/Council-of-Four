package it.polimi.ingsw.server.model;


import it.polimi.ingsw.server.control.Player;
import it.polimi.ingsw.server.model.City;
import it.polimi.ingsw.server.model.FaceUpPermitCardArea;

import it.polimi.ingsw.utils.Constant;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestFaceUpPermitCardArea {

	private Configurations config;
	private FaceUpPermitCardArea faceUpPermitCardArea;
	private ArrayList <Player> arrayListPlayer; 
	
	@Before
	public void init() {
		
		config = configurations();
		arrayListPlayer = createArrayListPlayer();
		int n = config.getNumberCities()/3;
		City[] arrayCity= new City[config.getNumberCities()/3];
		for(int i=0; i<n; i++){
			arrayCity[i] = new City(config, CityName.getCityNameFromIndex(i), RegionName.Sea, CityColor.Blue);
		}
		
		faceUpPermitCardArea = new FaceUpPermitCardArea(arrayCity, config);		
	}
	
	@Test
	public void constructor (){
		assertNotNull(faceUpPermitCardArea);
		assertEquals(Constant.FACE_UP_PERMIT_CARD_PER_REGION_NUMBER, 
				faceUpPermitCardArea.getArrayPermitCard().length);
	}
	
	@Test
	public void permitCards(){
		for (PermitCard permitCard : faceUpPermitCardArea.getArrayPermitCard()){
			assertNotNull(permitCard);
		}
	}
	
	@Test
	public void acquirePermitCard() {
		Player player = arrayListPlayer.get(1);
		PermitCard chosenPermitCard=faceUpPermitCardArea.acquirePermitCard(0);
		assertNotNull(chosenPermitCard);
		
		player.getArrayListPermitCard().add(chosenPermitCard);
		assertEquals(1, player.getArrayListPermitCard().size());
	}
	
	@Test
	public void replacePermitCard() {
		faceUpPermitCardArea.replacePermitCard(1);
		assertNotNull(faceUpPermitCardArea.getArrayPermitCard()[1]);
		assertEquals(2, faceUpPermitCardArea.getArrayPermitCard().length);
		
	}
	
	
	
	private Configurations configurations(){
		Configurations config = new Configurations();
		/*
		 * These 3 methods permit to create the configurations, write them in a file
		 * and read them. They substitute the action of the player (player side),
		 * that creates the file, and the
		 * class "Match" that reads the file
		 * If someone wants to change the configurations to see the different test cases
		 * he can do it in the method "createConfigurations"
		 */
		createConfigurations(config);
		createFileConfigurations(config);
		readFileConfigurations(config);

		return config;
	}
	
	private void createConfigurations(Configurations config){		/*
		 * Do not change this parameter and the difficulty one until we haven't create 
		 * new maps for those combination missing
		 */
		config.setCitiesNumber(18);
		
		config.setPermitCardBonusNumberMin(2);
		config.setPermitCardBonusNumberMax(3);
		
		config.setNobilityBonusRandom(false);
		if(config.isNobilityBonusRandom()==false){
			config.setNobilityBonusNumber(7);
		}
		
		config.setCityLinksPreconfigured(true);
		if(config.isCityLinksPreconfigured()==false){
			int[][]cityLinksMatrix =  new int[][]{
				{0,	1,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
				{0,	0,	1,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
				{0,	0,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0},
				{0,	0,	0,	0,	1,	1,	0,	0,	0,	0,	0,	0,	0,	0,	0},
				{0,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0,	0,	0,	0},
				{0,	0,	0,	0,	0,	0,	1,	0,	1,	0,	0,	0,	0,	0,	0},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	0,	1,	0,	0},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	0,	0,	0,	0},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	0,	0},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1,	0,	0},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	1},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0}
			};
			/*
    		 * This fir cycle is for making the matrix specular,
    		 * because in the txt file it is only upper triangular set
    		 */
			for(int i = 0; i< config.getCitiesNumber(); i++){
				for(int j = i; j< config.getCitiesNumber(); j++){
					cityLinksMatrix[j][i]=cityLinksMatrix[i][j];
				}
			}
			config.setCityLinksMatrix(cityLinksMatrix);
			
		}else{
			config.setDifficulty("h".charAt(0));
		}
		
		config.setCityBonusRandom(false);
        if(config.isCityBonusRandom()==false){
    		ArrayList<CityBonus> arrayListCityBonus[]= new ArrayList[config.getCitiesNumber()];
    		
    		for (int i=0; i<arrayListCityBonus.length; i++){
    			arrayListCityBonus[i] = new ArrayList<>();
    	    	arrayListCityBonus[i].add(new CityBonus(BonusName.Assistant, 2));
    	    	arrayListCityBonus[i].add(new CityBonus(BonusName.Richness, 1));
    		}
			config.setArrayListCityBonus(arrayListCityBonus);
        }else{
			config.setCityBonusNumberMin(2);
			config.setCityBonusNumberMax(3);
		}
		
	}
	
	private void createFileConfigurations(Configurations config){
		
		FileOutputStream fileOutputStream = null;
		
		try {
			
			//il salvataggio per ora è in locale, dovrà essere inviato al server quando ci sarà la connessione
			fileOutputStream = new FileOutputStream("./src/configurations/configTest");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

			// write something in the file
			objectOutputStream.writeObject(config);
		
		} catch (IOException e) {
			e.printStackTrace();
			
		}finally{
			// close the stream
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void readFileConfigurations(Configurations config){
		
		FileInputStream fileInputStream = null;
		
		try {
			
			//il salvataggio per ora è in locale, dovrà essere inviato al server quando ci sarà la connessione
			fileInputStream = new FileInputStream("./src/configurations/configTest");
			
			// create an ObjectInputStream for the file we created before
	         ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
	         this.config = (Configurations) objectInputStream.readObject();
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			// close the stream
			try {
				fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private ArrayList <Player> createArrayListPlayer() {
		ArrayList <Player> arrayListPlayer = new ArrayList <>();		
		
		Player player = new Player();
		player.setNickname("A");
		player.setAssistant(3);
		player.setRichness(10);
		player.getArrayListPoliticCard().add(new PoliticCard(Color.getRandomColor()));
		arrayListPlayer.add(player);

		Player player2 = new Player();
		player2.setNickname("B");
		player.setAssistant(0);
		player.setRichness(11);
		arrayListPlayer.add(player2);

		Player player3 = new Player();
		player3.setNickname("C");
		player.setAssistant(3);
		player.setRichness(12);
		arrayListPlayer.add(player3);
		
		return arrayListPlayer;
	}
	
}
