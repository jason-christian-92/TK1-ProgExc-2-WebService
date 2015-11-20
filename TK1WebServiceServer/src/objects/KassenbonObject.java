package objects;

import java.util.ArrayList;

public class KassenbonObject {

	private ArrayList<ItemObject> boughtItems;

	private int clientId;
	
	public KassenbonObject(int clientId) {
		// TODO Auto-generated constructor stub
		this.clientId = clientId;
		boughtItems = new ArrayList<ItemObject>();
	}
	
	public void addItem(ItemObject obj){
		for(ItemObject item : boughtItems){
			if (item.getID() == obj.getID()){
				item.setAmount(obj.getAmount());
				return;
			}
		}
		boughtItems.add(obj);
	}
	
	public int getClientId(){
		return clientId;
	}
	
	public double calculateTotalPrice(int taxPercentage){
		double sum = 0;
		for (ItemObject item:boughtItems){
			sum += (item.getPrice()*item.getAmount());
		}
		return sum*(1-(taxPercentage/100));
	}
}
