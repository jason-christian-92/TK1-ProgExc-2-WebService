package utils;

import java.util.ArrayList;

import objects.ItemObject;

public class XMLUtil {

	public static String xmlFromItemsList(ArrayList<ItemObject> objs){
		StringBuilder xml = new StringBuilder();
		xml.append("<data>");
		for (ItemObject obj : objs){
			xml.append(xmlFromItem(obj, false));
		}
		xml.append("</data>");
		return xml.toString();
	}
	
	public static String xmlFromItem(ItemObject obj, boolean includeDataTag){
		StringBuilder xml = new StringBuilder();
		if (includeDataTag) xml.append("<data>");
		xml.append("<item>");
		xml.append("<id>"+obj.getID()+"</id>");
		xml.append("<name>"+obj.getName()+"</name>");
		xml.append("<amount>"+obj.getAmount()+"</amount>");
		xml.append("<price>"+obj.getPrice()+"</price>");
		xml.append("</item>");
		if (includeDataTag) xml.append("</data>");
		return xml.toString();
	}	
}
