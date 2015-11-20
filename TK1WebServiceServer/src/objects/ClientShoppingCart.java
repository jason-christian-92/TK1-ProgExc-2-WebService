package objects;

import java.util.ArrayList;

public class ClientShoppingCart {

	private int clientID;
	private ArrayList<ItemObject> items;
	
	public ClientShoppingCart(){
		items = new ArrayList<ItemObject>();
	}
	
	public static ClientShoppingCart createFalseCart(){
		ClientShoppingCart cart = new ClientShoppingCart();
		cart.setClientID(-1);
		return cart;
	}
	
	public void clearCart(){
		items.clear();
	}
	
	public void setClientID(int id){
		clientID = id;
	}
	
	public int getClientID(){
		return clientID;
	}
	
	public ArrayList<ItemObject> getItems(){
		return items;
	}
	
	public int removeItem(int itemId){
		for (int i = 0 ; i < items.size() ; i++){
			if (itemId == items.get(i).getID()) {
				return items.remove(i).getAmount();
			}
		}
		return -1;
	}
	
	public ItemObject getItemById(int itemId){
		for (ItemObject obj:items){
			if (itemId == obj.getID()) return obj;
		}
		return null;
	}
	
	public void addItem(ItemObject obj){
		for(ItemObject item : items){
			if (item.getID() == obj.getID()){
				item.setAmount(obj.getAmount());
				return;
			}
		}
		items.add(obj);
	}
	
}
