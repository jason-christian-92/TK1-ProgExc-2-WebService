package iface;

import java.util.ArrayList;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import objects.ItemObject;

public interface IServer {
/*
	public ItemObject getItemById(int id);
	public ArrayList<ItemObject> getItems();
	public boolean isItemAvailable(int id);
	*/
	
	public String getItemById(int id);
	public String getItems();
	public boolean isItemAvailable(int id);
	
}
