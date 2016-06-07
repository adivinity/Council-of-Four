package it.polimi.ingsw.LM_Dichio_CoF.work.field;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

;

public class TestBalcony {

	@Test
	public void councilorsInBalcony() {
		
		AvailableCouncillors availableCouncillors = new AvailableCouncillors();
		
		Balcony balcony = new Balcony (availableCouncillors, RegionName.Sea.toString()+"Balcony");
		
		ArrayList<Councillor> arrayListCouncillor = balcony.getArrayListCouncillor();
		
		System.out.println(balcony.getNameBalcony());
		for(Councillor c: arrayListCouncillor){
			System.out.println(c.getColor());
		}
		
		assertEquals(4, arrayListCouncillor.size());
		
		
	}

}
