package utils;

import java.util.ArrayList;
import java.util.Arrays;

import objects.ItemObject;

public class ItemsList {

	private static ArrayList<ItemObject> ITEMS = new ArrayList<ItemObject>(
			Arrays.asList(
					new ItemObject(1, "Fettarme Milch Ja! 500 ml", 20, 0.45),
					new ItemObject(2, "Bernard Matthews geflügeln 80g", 32, 0.79),
					new ItemObject(3, "Danone Frucht Zwerge 6 x 50g", 6, 0.99),
					new ItemObject(4, "Henglein Schupfnudeln 2 x 500g", 10, 1.49),
					new ItemObject(5, "Bertolli Sauce 400g", 19, 1.79),
					new ItemObject(6, "Ben and Jerry's Ice Cream 500ml", 15, 4.98)
			)
	);
	
	public static ArrayList<ItemObject> getItemsList(){
		return ITEMS;
	}
	
}
